package com.example.train_scheduling_and_booking_system.service;

import com.example.train_scheduling_and_booking_system.dto.*;
import com.example.train_scheduling_and_booking_system.entity.*;
import com.example.train_scheduling_and_booking_system.exception.BadRequestException;
import com.example.train_scheduling_and_booking_system.exception.ResourceNotFoundException;
import com.example.train_scheduling_and_booking_system.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StaffOperationsService {

    private final TrainScheduleRepository trainScheduleRepository;
    private final BookingRepository bookingRepository;
    private final BookingPassengerRepository bookingPassengerRepository;
    private final SupportTicketRepository supportTicketRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final UserRepository userRepository;
    private final BookingService bookingService;

    public StaffOperationsService(TrainScheduleRepository trainScheduleRepository,
                                  BookingRepository bookingRepository,
                                  BookingPassengerRepository bookingPassengerRepository,
                                  SupportTicketRepository supportTicketRepository,
                                  FinancialTransactionRepository financialTransactionRepository,
                                  UserRepository userRepository,
                                  BookingService bookingService) {
        this.trainScheduleRepository = trainScheduleRepository;
        this.bookingRepository = bookingRepository;
        this.bookingPassengerRepository = bookingPassengerRepository;
        this.supportTicketRepository = supportTicketRepository;
        this.financialTransactionRepository = financialTransactionRepository;
        this.userRepository = userRepository;
        this.bookingService = bookingService;
    }

    // ==========================================
    // 1. OPERATIONS MANAGER (ROLE_OPERATIONS_MANAGER)
    // ==========================================
    public OperationsMetricsResponse getOperationsMetrics() {
        List<TrainSchedule> schedules = trainScheduleRepository.findAll();
        int totalTrains = schedules.size();
        if (totalTrains == 0) {
            return new OperationsMetricsResponse(0, 0, 100.0, 100.0, 0, Collections.emptyList());
        }

        long onTimeCount = schedules.stream().filter(s -> "ON_TIME".equalsIgnoreCase(s.getStatus())).count();
        long delayedCount = schedules.stream().filter(s -> "DELAYED".equalsIgnoreCase(s.getStatus())).count();
        long activeTrains = schedules.stream().filter(s -> !"CANCELLED".equalsIgnoreCase(s.getStatus())).count();

        double onTimePct = ((double) onTimeCount / totalTrains) * 100.0;
        double fleetAvailabilityPct = ((double) activeTrains / totalTrains) * 100.0;

        Set<String> uniqueRoutes = new HashSet<>();
        for (TrainSchedule s : schedules) {
            uniqueRoutes.add(s.getOriginStation() + " \u2192 " + s.getDestinationStation());
        }

        return new OperationsMetricsResponse(
                totalTrains,
                uniqueRoutes.size(),
                Math.round(onTimePct * 10.0) / 10.0,
                Math.round(fleetAvailabilityPct * 10.0) / 10.0,
                (int) delayedCount,
                new ArrayList<>(uniqueRoutes)
        );
    }

    // ==========================================
    // 2. CUSTOMER SERVICE SUPERVISOR (ROLE_CUSTOMER_SERVICE_SUPERVISOR)
    // ==========================================
    public List<SupportTicketResponse> getPendingTickets() {
        return supportTicketRepository.findAllByStatusOrderByCreatedAtDesc("PENDING").stream()
                .map(this::mapToSupportTicketResponse)
                .collect(Collectors.toList());
    }

    public List<SupportTicketResponse> getAllTickets() {
        return supportTicketRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToSupportTicketResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SupportTicketResponse resolveTicket(Long ticketId, TicketResolutionRequest request, String resolverUsername) {
        SupportTicket ticket = supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Support ticket not found: " + ticketId));

        User resolver = userRepository.findByUsername(resolverUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Resolver user not found: " + resolverUsername));

        String newStatus = request.getStatus().toUpperCase();
        ticket.setStatus(newStatus);
        ticket.setResolutionNotes(request.getResolutionNotes());
        ticket.setResolvedBy(resolver);

        // If refund ticket approved, approve refund transaction
        if ("REFUND".equalsIgnoreCase(ticket.getCategory()) && "APPROVED".equalsIgnoreCase(newStatus)) {
            if (ticket.getBooking() != null) {
                Booking booking = ticket.getBooking();
                List<FinancialTransaction> txns = financialTransactionRepository.findAllByOrderByCreatedAtDesc();
                for (FinancialTransaction txn : txns) {
                    if (txn.getBooking() != null && txn.getBooking().getBookingId().equals(booking.getBookingId())
                            && "REFUND".equalsIgnoreCase(txn.getTransactionType())
                            && "PENDING".equalsIgnoreCase(txn.getStatus())) {
                        txn.setStatus("SUCCESS");
                        financialTransactionRepository.save(txn);
                        break;
                    }
                }
            }
        }

        SupportTicket saved = supportTicketRepository.save(ticket);
        return mapToSupportTicketResponse(saved);
    }

    // ==========================================
    // 3. SCHEDULE COORDINATOR (ROLE_SCHEDULE_COORDINATOR)
    // ==========================================
    @Transactional
    public ScheduleResponse createSchedule(ScheduleCreateRequest request) {
        TrainSchedule schedule = new TrainSchedule();
        schedule.setTrainNumber(request.getTrainNumber());
        schedule.setTrainName(request.getTrainName());
        schedule.setOriginStation(request.getOriginStation());
        schedule.setDestinationStation(request.getDestinationStation());
        schedule.setDepartureTime(request.getDepartureTime());
        schedule.setArrivalTime(request.getArrivalTime());
        schedule.setTravelDate(request.getTravelDate());
        schedule.setTotalSeats(request.getTotalSeats() != null ? request.getTotalSeats() : 120);
        schedule.setAvailableSeats(schedule.getTotalSeats());
        schedule.setBaseFare(request.getBaseFare() != null ? request.getBaseFare() : BigDecimal.valueOf(500.00));
        schedule.setPlatformNumber(request.getPlatformNumber() != null ? request.getPlatformNumber() : "1");
        schedule.setStatus("ON_TIME");
        schedule.setDelayMinutes(0);

        TrainSchedule saved = trainScheduleRepository.save(schedule);
        return bookingService.mapToScheduleResponse(saved);
    }

    @Transactional
    public ScheduleResponse updateSchedule(Long scheduleId, ScheduleCreateRequest request) {
        TrainSchedule schedule = trainScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found: " + scheduleId));

        schedule.setTrainNumber(request.getTrainNumber());
        schedule.setTrainName(request.getTrainName());
        schedule.setOriginStation(request.getOriginStation());
        schedule.setDestinationStation(request.getDestinationStation());
        schedule.setDepartureTime(request.getDepartureTime());
        schedule.setArrivalTime(request.getArrivalTime());
        schedule.setTravelDate(request.getTravelDate());
        schedule.setTotalSeats(request.getTotalSeats());
        schedule.setBaseFare(request.getBaseFare());
        if (request.getPlatformNumber() != null) {
            schedule.setPlatformNumber(request.getPlatformNumber());
        }

        TrainSchedule saved = trainScheduleRepository.save(schedule);
        return bookingService.mapToScheduleResponse(saved);
    }

    @Transactional
    public ScheduleResponse updateDelayAndPlatform(Long scheduleId, Integer delayMinutes, String platformNumber, String status) {
        TrainSchedule schedule = trainScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found: " + scheduleId));

        if (delayMinutes != null) {
            schedule.setDelayMinutes(delayMinutes);
            if (delayMinutes > 0 && (status == null || "ON_TIME".equalsIgnoreCase(status))) {
                schedule.setStatus("DELAYED");
            } else if (delayMinutes == 0 && (status == null || "DELAYED".equalsIgnoreCase(status))) {
                schedule.setStatus("ON_TIME");
            }
        }
        if (platformNumber != null && !platformNumber.isBlank()) {
            schedule.setPlatformNumber(platformNumber);
        }
        if (status != null && !status.isBlank()) {
            schedule.setStatus(status.toUpperCase());
        }

        TrainSchedule saved = trainScheduleRepository.save(schedule);
        return bookingService.mapToScheduleResponse(saved);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId) {
        TrainSchedule schedule = trainScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found: " + scheduleId));
        trainScheduleRepository.delete(schedule);
    }

    // ==========================================
    // 4. FINANCE OFFICER (ROLE_FINANCE_OFFICER)
    // ==========================================
    public FinanceReportResponse getFinanceReport() {
        List<FinancialTransaction> transactions = financialTransactionRepository.findAllByOrderByCreatedAtDesc();

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal todayRevenue = BigDecimal.ZERO;
        BigDecimal totalRefunds = BigDecimal.ZERO;
        long successfulCount = 0;
        String todayStr = LocalDate.now().toString();

        List<Map<String, Object>> recentList = new ArrayList<>();

        for (FinancialTransaction txn : transactions) {
            if ("SUCCESS".equalsIgnoreCase(txn.getStatus())) {
                if ("PAYMENT".equalsIgnoreCase(txn.getTransactionType())) {
                    totalRevenue = totalRevenue.add(txn.getAmount());
                    if (txn.getCreatedAt() != null && txn.getCreatedAt().toLocalDate().toString().equals(todayStr)) {
                        todayRevenue = todayRevenue.add(txn.getAmount());
                    }
                } else if ("REFUND".equalsIgnoreCase(txn.getTransactionType())) {
                    totalRefunds = totalRefunds.add(txn.getAmount());
                }
                successfulCount++;
            }

            if (recentList.size() < 25) {
                Map<String, Object> map = new HashMap<>();
                map.put("transactionId", txn.getTransactionId());
                map.put("transactionRef", txn.getTransactionRef());
                map.put("amount", txn.getAmount());
                map.put("transactionType", txn.getTransactionType());
                map.put("paymentMethod", txn.getPaymentMethod());
                map.put("status", txn.getStatus());
                map.put("createdAt", txn.getCreatedAt() != null ? txn.getCreatedAt().toString() : "");
                map.put("bookingRef", txn.getBooking() != null ? txn.getBooking().getBookingReference() : "N/A");
                recentList.add(map);
            }
        }

        return new FinanceReportResponse(
                totalRevenue,
                todayRevenue,
                transactions.size(),
                successfulCount,
                totalRefunds,
                recentList
        );
    }

    // ==========================================
    // 5. STATION MASTER (ROLE_STATION_MASTER)
    // ==========================================
    public List<ScheduleResponse> getStationSchedule(String stationName) {
        if (stationName == null || stationName.isBlank()) {
            return trainScheduleRepository.findAllByOrderByDepartureTimeAsc().stream()
                    .map(bookingService::mapToScheduleResponse)
                    .collect(Collectors.toList());
        }
        return trainScheduleRepository.findByOriginStationIgnoreCaseOrDestinationStationIgnoreCase(stationName.trim(), stationName.trim())
                .stream()
                .map(bookingService::mapToScheduleResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ScheduleResponse updateStationPlatform(Long scheduleId, String platformNumber) {
        TrainSchedule schedule = trainScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found: " + scheduleId));
        schedule.setPlatformNumber(platformNumber);
        TrainSchedule saved = trainScheduleRepository.save(schedule);
        return bookingService.mapToScheduleResponse(saved);
    }

    public BoardingManifestResponse getBoardingManifest(Long scheduleId) {
        TrainSchedule schedule = trainScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found: " + scheduleId));

        List<Booking> bookings = bookingRepository.findAllByScheduleScheduleId(scheduleId);
        List<BoardingManifestResponse.ManifestPassenger> manifestPassengers = new ArrayList<>();

        int seatIndex = 1;
        for (Booking b : bookings) {
            if ("CONFIRMED".equalsIgnoreCase(b.getStatus())) {
                List<BookingPassenger> bpList = bookingPassengerRepository.findAllByBookingBookingId(b.getBookingId());
                for (BookingPassenger bp : bpList) {
                    manifestPassengers.add(new BoardingManifestResponse.ManifestPassenger(
                            b.getBookingReference(),
                            bp.getFullName(),
                            bp.getNicOrPassport(),
                            bp.getConcessionType(),
                            "Car A - S" + seatIndex++,
                            "BOARDED"
                    ));
                }
            }
        }

        int platformNum = 1;
        try {
            platformNum = Integer.parseInt(schedule.getPlatformNumber());
        } catch (Exception ignored) {}

        return new BoardingManifestResponse(
                schedule.getScheduleId(),
                schedule.getTrainNumber(),
                schedule.getTrainName(),
                schedule.getOriginStation(),
                schedule.getDestinationStation(),
                schedule.getDepartureTime(),
                schedule.getArrivalTime(),
                platformNum,
                schedule.getTotalSeats(),
                manifestPassengers.size(),
                manifestPassengers
        );
    }

    private SupportTicketResponse mapToSupportTicketResponse(SupportTicket t) {
        return new SupportTicketResponse(
                t.getTicketId(),
                t.getTicketNumber(),
                t.getUser() != null ? t.getUser().getUsername() : "N/A",
                t.getUser() != null ? t.getUser().getFullName() : "N/A",
                t.getBooking() != null ? t.getBooking().getBookingId() : null,
                t.getBooking() != null ? t.getBooking().getBookingReference() : null,
                t.getCategory(),
                t.getSubject(),
                t.getDescription(),
                t.getStatus(),
                t.getResolutionNotes(),
                t.getResolvedBy() != null ? t.getResolvedBy().getUsername() : null,
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}
