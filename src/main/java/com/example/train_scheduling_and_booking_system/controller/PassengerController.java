package com.example.train_scheduling_and_booking_system.controller;

import com.example.train_scheduling_and_booking_system.dto.*;
import com.example.train_scheduling_and_booking_system.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passenger")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        UserProfileResponse profile = passengerService.getProfile(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Profile retrieved successfully", profile));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ProfileUpdateRequest request) {
        UserProfileResponse profile = passengerService.updateProfile(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully", profile));
    }

    @PutMapping({"/change-password", "/password"})
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PasswordChangeRequest request) {
        passengerService.changePassword(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.message("Password changed successfully"));
    }

    @GetMapping("/companions")
    public ResponseEntity<ApiResponse<List<CompanionResponse>>> getCompanions(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<CompanionResponse> companions = passengerService.getCompanions(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Companions retrieved successfully", companions));
    }

    @GetMapping("/companions/{id}")
    public ResponseEntity<ApiResponse<CompanionResponse>> getCompanionById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long id) {
        CompanionResponse companion = passengerService.getCompanionById(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Companion retrieved successfully", companion));
    }

    @PostMapping("/companions")
    public ResponseEntity<ApiResponse<CompanionResponse>> createCompanion(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CompanionRequest request) {
        CompanionResponse response = passengerService.createCompanion(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Companion added successfully", response));
    }

    @PutMapping("/companions/{id}")
    public ResponseEntity<ApiResponse<CompanionResponse>> updateCompanion(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long id,
            @Valid @RequestBody CompanionRequest request) {
        CompanionResponse response = passengerService.updateCompanion(id, request, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Companion updated successfully", response));
    }

    @DeleteMapping("/companions/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCompanion(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long id) {
        passengerService.deleteCompanion(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.message("Companion deleted successfully"));
    }
}
