package com.example.train_scheduling_and_booking_system.service;

import com.example.train_scheduling_and_booking_system.dto.BookingResponse;
import com.example.train_scheduling_and_booking_system.entity.Booking;
import com.example.train_scheduling_and_booking_system.entity.User;
import com.example.train_scheduling_and_booking_system.exception.BadRequestException;
import com.example.train_scheduling_and_booking_system.exception.ResourceNotFoundException;
import com.example.train_scheduling_and_booking_system.repository.BookingRepository;
import com.example.train_scheduling_and_booking_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
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


