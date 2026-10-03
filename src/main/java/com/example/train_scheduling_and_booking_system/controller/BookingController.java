package com.example.train_scheduling_and_booking_system.controller;

import com.example.train_scheduling_and_booking_system.dto.ApiResponse;
import com.example.train_scheduling_and_booking_system.dto.BookingResponse;
import com.example.train_scheduling_and_booking_system.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<BookingResponse> bookings = bookingService.getUserBookings(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("User bookings retrieved successfully", bookings));
    }

    @GetMapping("/{reference}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingByReference(
            @PathVariable String reference,
            @AuthenticationPrincipal UserDetails userDetails) {
        BookingResponse booking = bookingService.getBookingByReference(reference, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Booking details retrieved", booking));
    }
}

