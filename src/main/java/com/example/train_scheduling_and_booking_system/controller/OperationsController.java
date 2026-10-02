package com.example.train_scheduling_and_booking_system.controller;

import com.example.train_scheduling_and_booking_system.dto.ApiResponse;
import com.example.train_scheduling_and_booking_system.dto.OperationsMetricsResponse;
import com.example.train_scheduling_and_booking_system.dto.ScheduleResponse;
import com.example.train_scheduling_and_booking_system.service.BookingService;
import com.example.train_scheduling_and_booking_system.service.StaffOperationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
public class OperationsController {

    private final StaffOperationsService staffOperationsService;
    private final BookingService bookingService;

    public OperationsController(StaffOperationsService staffOperationsService, BookingService bookingService) {
        this.staffOperationsService = staffOperationsService;
        this.bookingService = bookingService;
    }

    @GetMapping("/metrics")
    @PreAuthorize("hasAnyRole('OPERATIONS_MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<OperationsMetricsResponse>> getMetrics() {
        OperationsMetricsResponse metrics = staffOperationsService.getOperationsMetrics();
        return ResponseEntity.ok(ApiResponse.ok("Operations metrics retrieved successfully", metrics));
    }

    @GetMapping("/schedules")
    @PreAuthorize("hasAnyRole('OPERATIONS_MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> getFleetSchedules() {
        List<ScheduleResponse> schedules = bookingService.getAllSchedules();
        return ResponseEntity.ok(ApiResponse.ok("Fleet schedules retrieved successfully", schedules));
    }
}
