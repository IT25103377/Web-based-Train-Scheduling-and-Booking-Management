package com.example.train_scheduling_and_booking_system.controller;

import com.example.train_scheduling_and_booking_system.dto.ApiResponse;
import com.example.train_scheduling_and_booking_system.dto.PasswordChangeRequest;
import com.example.train_scheduling_and_booking_system.dto.ProfileUpdateRequest;
import com.example.train_scheduling_and_booking_system.dto.UserProfileResponse;
import com.example.train_scheduling_and_booking_system.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final PassengerService passengerService;

    public UserController(PassengerService passengerService) {
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
}
