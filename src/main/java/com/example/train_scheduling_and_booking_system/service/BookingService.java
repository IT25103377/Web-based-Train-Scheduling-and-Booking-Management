package com.example.train_scheduling_and_booking_system.service;

import com.example.train_scheduling_and_booking_system.dto.BookingCreateRequest;
import com.example.train_scheduling_and_booking_system.dto.BookingResponse;
import com.example.train_scheduling_and_booking_system.dto.PassengerItemRequest;
import com.example.train_scheduling_and_booking_system.dto.ScheduleResponse;
import com.example.train_scheduling_and_booking_system.entity.*;
import com.example.train_scheduling_and_booking_system.exception.BadRequestException;
import com.example.train_scheduling_and_booking_system.exception.ResourceNotFoundException;
import com.example.train_scheduling_and_booking_system.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final TrainScheduleRepository trainScheduleRepository;
    private final BookingRepository bookingRepository;
    private final BookingPassengerRepository bookingPassengerRepository;
    private final UserRepository userRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final SupportTicketRepository supportTicketRepository;

    public BookingService(TrainScheduleRepository trainScheduleRepository,
                          BookingRepository bookingRepository,
                          BookingPassengerRepository bookingPassengerRepository,
                          UserRepository userRepository,
                          FinancialTransactionRepository financialTransactionRepository,
                          SupportTicketRepository supportTicketRepository) {
        this.trainScheduleRepository = trainScheduleRepository;
        this.bookingRepository = bookingRepository;
        this.bookingPassengerRepository = bookingPassengerRepository;
        this.userRepository = userRepository;
        this.financialTransactionRepository = financialTransactionRepository;
        this.supportTicketRepository = supportTicketRepository;
    }

    public List<ScheduleResponse> searchSchedules(String origin, String destination, String travelDate, Integer passengerCount) {
        List<TrainSchedule> schedules;
        if (origin != null && !origin.isBlank() && destination != null && !destination.isBlank()) {
            schedules = trainScheduleRepository.findByOriginStationIgnoreCaseAndDestinationStationIgnoreCase(origin.trim(), destination.trim());
        } else if (origin != null && !origin.isBlank()) {
            schedules = trainScheduleRepository.findByOriginStationIgnoreCase(origin.trim());
        } else {
            schedules = trainScheduleRepository.findAllByOrderByDepartureTimeAsc();
        }

        if (passengerCount != null && passengerCount > 0) {
            schedules = schedules.stream()
                    .filter(s -> s.getAvailableSeats() >= passengerCount)
                    .collect(Collectors.toList());
        }

        return schedules.stream().map(this::mapToScheduleResponse).collect(Collectors.toList());
    }

    public List<ScheduleResponse> getAllSchedules() {
        return trainScheduleRepository.findAllByOrderByDepartureTimeAsc().stream()
                .map(this::mapToScheduleResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponse createBooking(BookingCreateRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        TrainSchedule schedule = trainScheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with ID: " + request.getScheduleId()));

        int count = request.getPassengers().size();
        if (schedule.getAvailableSeats() < count) {
            throw new BadRequestException("Not enough available seats. Remaining: " + schedule.getAvailableSeats());
        }

        BigDecimal baseFare = schedule.getBaseFare();
        BigDecimal totalGross = baseFare.multiply(BigDecimal.valueOf(count));
        BigDecimal finalTotal = BigDecimal.ZERO;

        List<BookingPassenger> passengerEntities = new ArrayList<>();
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSchedule(schedule);
        booking.setTravelDate(request.getTravelDate() != null && !request.getTravelDate().isBlank() ? request.getTravelDate() : schedule.getTravelDate());
        booking.setPassengerCount(count);
        booking.setStatus("CONFIRMED");

        String bookingRef = "BK-" + System.currentTimeMillis() + "-" + (int) (100 + Math.random() * 900);
        booking.setBookingReference(bookingRef);

        for (PassengerItemRequest p : request.getPassengers()) {
            BigDecimal passengerFare = baseFare;
            String concession = p.getConcessionType() != null ? p.getConcessionType().toUpperCase() : "NONE";

            if ("STUDENT".equals(concession)) {
                passengerFare = baseFare.multiply(new BigDecimal("0.80")).setScale(2, RoundingMode.HALF_UP);
            } else if ("SENIOR".equals(concession)) {
                passengerFare = baseFare.multiply(new BigDecimal("0.85")).setScale(2, RoundingMode.HALF_UP);
            }

            finalTotal = finalTotal.add(passengerFare);

            BookingPassenger bp = new BookingPassenger();
            bp.setBooking(booking);
            bp.setFullName(p.getFullName());
            bp.setNicOrPassport(p.getNicOrPassport());
            bp.setConcessionType(concession);
            bp.setFareApplied(passengerFare);
            passengerEntities.add(bp);
        }

        BigDecimal discountTotal = totalGross.subtract(finalTotal);
        booking.setTotalAmount(totalGross);
        booking.setDiscountAmount(discountTotal);
        booking.setFinalAmount(finalTotal);
        booking.setPassengers(passengerEntities);

        // Deduct seats
        schedule.setAvailableSeats(schedule.getAvailableSeats() - count);
        trainScheduleRepository.save(schedule);

        Booking savedBooking = bookingRepository.save(booking);

        // Create financial transaction
        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setTransactionRef("TXN-" + System.currentTimeMillis() + "-" + (int) (100 + Math.random() * 900));
        transaction.setBooking(savedBooking);
        transaction.setAmount(finalTotal);
        transaction.setTransactionType("PAYMENT");
        transaction.setPaymentMethod("CREDIT_CARD");
        transaction.setStatus("SUCCESS");
        financialTransactionRepository.save(transaction);

        return mapToBookingResponse(savedBooking);
    }

    public List<BookingResponse> getUserBookings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return bookingRepository.findAllByUserUserIdOrderByCreatedAtDesc(user.getUserId()).stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse getBookingByReference(String reference, String username) {
        Booking booking = bookingRepository.findByBookingReference(reference)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + reference));
        if (!booking.getUser().getUsername().equals(username)) {
            throw new BadRequestException("Unauthorized access to booking reference");
        }
        return mapToBookingResponse(booking);
    }

    @Transactional
    public BookingResponse cancelBooking(Long bookingId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        if (!booking.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("Unauthorized to cancel this booking");
        }

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new BadRequestException("Booking is already cancelled");
        }

        booking.setStatus("CANCELLED");
        Booking saved = bookingRepository.save(booking);

        // Return seats
        TrainSchedule schedule = booking.getSchedule();
        if (schedule != null) {
            schedule.setAvailableSeats(schedule.getAvailableSeats() + booking.getPassengerCount());
            trainScheduleRepository.save(schedule);
        }

        // Auto-create Support Ticket for Refund processing
        SupportTicket ticket = new SupportTicket();
        ticket.setTicketNumber("TKT-" + System.currentTimeMillis() + "-" + (int) (100 + Math.random() * 900));
        ticket.setUser(user);
        ticket.setBooking(booking);
        ticket.setCategory("REFUND");
        ticket.setSubject("Refund request for cancelled booking #" + booking.getBookingReference());
        ticket.setDescription("Automatic refund request for cancelled booking reference " + booking.getBookingReference() + ". Amount: LKR " + booking.getFinalAmount());
        ticket.setStatus("PENDING");
        supportTicketRepository.save(ticket);

        // Record pending refund transaction
        FinancialTransaction refundTxn = new FinancialTransaction();
        refundTxn.setTransactionRef("RFD-" + System.currentTimeMillis() + "-" + (int) (100 + Math.random() * 900));
        refundTxn.setBooking(booking);
        refundTxn.setAmount(booking.getFinalAmount());
        refundTxn.setTransactionType("REFUND");
        refundTxn.setPaymentMethod("CREDIT_CARD");
        refundTxn.setStatus("PENDING");
        financialTransactionRepository.save(refundTxn);

        return mapToBookingResponse(saved);
    }

    public ScheduleResponse mapToScheduleResponse(TrainSchedule s) {
        return new ScheduleResponse(
                s.getScheduleId(),
                s.getTrainNumber(),
                s.getTrainName(),
                s.getOriginStation(),
                s.getDestinationStation(),
                s.getDepartureTime(),
                s.getArrivalTime(),
                s.getTravelDate(),
                s.getTotalSeats(),
                s.getAvailableSeats(),
                s.getBaseFare(),
                s.getPlatformNumber(),
                s.getStatus(),
                s.getDelayMinutes()
        );
    }

    public BookingResponse mapToBookingResponse(Booking b) {
        List<BookingResponse.PassengerItemDetail> passengerDetails = b.getPassengers() != null
                ? b.getPassengers().stream()
                .map(p -> new BookingResponse.PassengerItemDetail(
                        p.getFullName(),
                        p.getNicOrPassport(),
                        p.getConcessionType(),
                        p.getFareApplied()
                )).collect(Collectors.toList())
                : new ArrayList<>();

        return new BookingResponse(
                b.getBookingId(),
                b.getBookingReference(),
                b.getSchedule() != null ? b.getSchedule().getTrainNumber() : "N/A",
                b.getSchedule() != null ? b.getSchedule().getTrainName() : "N/A",
                b.getSchedule() != null ? b.getSchedule().getOriginStation() : "N/A",
                b.getSchedule() != null ? b.getSchedule().getDestinationStation() : "N/A",
                b.getSchedule() != null ? b.getSchedule().getDepartureTime() : "N/A",
                b.getTravelDate(),
                b.getPassengerCount(),
                b.getTotalAmount(),
                b.getDiscountAmount(),
                b.getFinalAmount(),
                b.getStatus(),
                b.getCreatedAt(),
                passengerDetails
        );
    }
}
