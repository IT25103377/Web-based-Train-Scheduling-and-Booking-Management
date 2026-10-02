package com.example.train_scheduling_and_booking_system.controller;

import com.example.train_scheduling_and_booking_system.dto.ApiResponse;
import com.example.train_scheduling_and_booking_system.dto.ScheduleCreateRequest;
import com.example.train_scheduling_and_booking_system.dto.ScheduleResponse;
import com.example.train_scheduling_and_booking_system.service.BookingService;
import com.example.train_scheduling_and_booking_system.service.StaffOperationsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final BookingService bookingService;
    private final StaffOperationsService staffOperationsService;

    public ScheduleController(BookingService bookingService, StaffOperationsService staffOperationsService) {
        this.bookingService = bookingService;
        this.staffOperationsService = staffOperationsService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> getAllSchedules() {
        List<ScheduleResponse> schedules = bookingService.getAllSchedules();
        return ResponseEntity.ok(ApiResponse.ok("Train schedules retrieved successfully", schedules));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> searchSchedules(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String travelDate,
            @RequestParam(required = false) Integer passengers) {
        List<ScheduleResponse> results = bookingService.searchSchedules(origin, destination, travelDate, passengers);
        return ResponseEntity.ok(ApiResponse.ok("Schedules retrieved successfully", results));
    }

    @PostMapping("/manage")
    @PreAuthorize("hasAnyRole('SCHEDULE_COORDINATOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<ScheduleResponse>> createSchedule(
            @Valid @RequestBody ScheduleCreateRequest request) {
        ScheduleResponse created = staffOperationsService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Schedule created successfully", created));
    }

    @PutMapping("/manage/{id}")
    @PreAuthorize("hasAnyRole('SCHEDULE_COORDINATOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<ScheduleResponse>> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleCreateRequest request) {
        ScheduleResponse updated = staffOperationsService.updateSchedule(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Schedule updated successfully", updated));
    }

    @PatchMapping("/manage/{id}/delay-platform")
    @PreAuthorize("hasAnyRole('SCHEDULE_COORDINATOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<ScheduleResponse>> updateDelayAndPlatform(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        Integer delayMinutes = body.get("delayMinutes") != null ? Integer.parseInt(body.get("delayMinutes").toString()) : null;
        String platform = body.get("platformNumber") != null ? body.get("platformNumber").toString() : null;
        String status = body.get("status") != null ? body.get("status").toString() : null;

        ScheduleResponse updated = staffOperationsService.updateDelayAndPlatform(id, delayMinutes, platform, status);
        return ResponseEntity.ok(ApiResponse.ok("Schedule delay/platform updated", updated));
    }

    @DeleteMapping("/manage/{id}")
    @PreAuthorize("hasAnyRole('SCHEDULE_COORDINATOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(@PathVariable Long id) {
        staffOperationsService.deleteSchedule(id);
        return ResponseEntity.ok(ApiResponse.ok("Schedule deleted successfully", null));
    }
}
