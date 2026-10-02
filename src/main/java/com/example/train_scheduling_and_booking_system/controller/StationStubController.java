package com.example.train_scheduling_and_booking_system.controller;

import com.example.train_scheduling_and_booking_system.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/station")
public class StationStubController {

    @GetMapping("/daily-schedule/{stationCode}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDailyStationSchedule(
            @PathVariable("stationCode") String stationCode,
            @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> data = Map.of(
                "stationCode", stationCode.toUpperCase(),
                "stationName", "Colombo Fort Central",
                "officer", userDetails.getUsername(),
                "departuresCount", 14,
                "arrivalsCount", 16
        );
        return ResponseEntity.ok(ApiResponse.ok("Daily station schedule retrieved successfully", data));
    }

    @PostMapping("/verify-ticket/{ticketCode}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyTicket(
            @PathVariable("ticketCode") String ticketCode,
            @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> verification = Map.of(
                "ticketCode", ticketCode,
                "status", "VERIFIED",
                "passengerName", "Verified Passenger",
                "seatNumber", "A-12",
                "verifiedBy", userDetails.getUsername()
        );
        return ResponseEntity.ok(ApiResponse.ok("Ticket verified successfully by station staff", verification));
    }

    @PutMapping("/boarding-status/{scheduleId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateBoardingStatus(
            @PathVariable("scheduleId") Long scheduleId,
            @RequestBody Map<String, String> statusUpdate,
            @AuthenticationPrincipal UserDetails userDetails) {
        String status = statusUpdate.getOrDefault("status", "BOARDING_NOW");
        Map<String, Object> response = Map.of(
                "scheduleId", scheduleId,
                "newStatus", status,
                "updatedBy", userDetails.getUsername()
        );
        return ResponseEntity.ok(ApiResponse.ok("Boarding status updated successfully", response));
    }
}
