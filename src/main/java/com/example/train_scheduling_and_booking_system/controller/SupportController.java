package com.example.train_scheduling_and_booking_system.controller;

import com.example.train_scheduling_and_booking_system.dto.ApiResponse;
import com.example.train_scheduling_and_booking_system.dto.SupportTicketResponse;
import com.example.train_scheduling_and_booking_system.dto.TicketCreateRequest;
import com.example.train_scheduling_and_booking_system.dto.TicketResolutionRequest;
import com.example.train_scheduling_and_booking_system.entity.Booking;
import com.example.train_scheduling_and_booking_system.entity.SupportTicket;
import com.example.train_scheduling_and_booking_system.entity.User;
import com.example.train_scheduling_and_booking_system.exception.ResourceNotFoundException;
import com.example.train_scheduling_and_booking_system.repository.BookingRepository;
import com.example.train_scheduling_and_booking_system.repository.SupportTicketRepository;
import com.example.train_scheduling_and_booking_system.repository.UserRepository;
import com.example.train_scheduling_and_booking_system.service.StaffOperationsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/support")
public class SupportController {

    private final StaffOperationsService staffOperationsService;
    private final SupportTicketRepository supportTicketRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public SupportController(StaffOperationsService staffOperationsService,
                             SupportTicketRepository supportTicketRepository,
                             UserRepository userRepository,
                             BookingRepository bookingRepository) {
        this.staffOperationsService = staffOperationsService;
        this.supportTicketRepository = supportTicketRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    // --- Supervisor Endpoints ---
    @GetMapping("/supervisor/tickets")
    @PreAuthorize("hasAnyRole('CUSTOMER_SERVICE_SUPERVISOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<SupportTicketResponse>>> getSupervisorTickets(
            @RequestParam(required = false, defaultValue = "false") boolean pendingOnly) {
        List<SupportTicketResponse> tickets = pendingOnly
                ? staffOperationsService.getPendingTickets()
                : staffOperationsService.getAllTickets();
        return ResponseEntity.ok(ApiResponse.ok("Support tickets retrieved successfully", tickets));
    }

    @PostMapping("/supervisor/tickets/{id}/resolve")
    @PreAuthorize("hasAnyRole('CUSTOMER_SERVICE_SUPERVISOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<SupportTicketResponse>> resolveTicket(
            @PathVariable Long id,
            @Valid @RequestBody TicketResolutionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        SupportTicketResponse resolved = staffOperationsService.resolveTicket(id, request, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Ticket resolution recorded", resolved));
    }

    // --- Passenger Support Endpoints ---
    @PostMapping("/my-tickets")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<SupportTicketResponse>> createTicket(
            @Valid @RequestBody TicketCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userDetails.getUsername()));

        Booking booking = null;
        if (request.getBookingId() != null) {
            booking = bookingRepository.findById(request.getBookingId()).orElse(null);
        }

        SupportTicket ticket = new SupportTicket();
        ticket.setTicketNumber("TKT-" + System.currentTimeMillis() + "-" + (int) (100 + Math.random() * 900));
        ticket.setUser(user);
        ticket.setBooking(booking);
        ticket.setCategory(request.getCategory().toUpperCase());
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setStatus("PENDING");

        SupportTicket saved = supportTicketRepository.save(ticket);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Support ticket submitted", mapToSupportTicketResponse(saved)));
    }

    @GetMapping("/my-tickets")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<SupportTicketResponse>>> getMyTickets(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userDetails.getUsername()));

        List<SupportTicketResponse> list = supportTicketRepository.findAllByUserUserIdOrderByCreatedAtDesc(user.getUserId())
                .stream()
                .map(this::mapToSupportTicketResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.ok("User support tickets retrieved", list));
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
