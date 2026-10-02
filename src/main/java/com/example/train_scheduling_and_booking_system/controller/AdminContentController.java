package com.example.train_scheduling_and_booking_system.controller;

import com.example.train_scheduling_and_booking_system.dto.ApiResponse;
import com.example.train_scheduling_and_booking_system.dto.ContentUpdateRequest;
import com.example.train_scheduling_and_booking_system.dto.PortalContentResponse;
import com.example.train_scheduling_and_booking_system.service.AdminContentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminContentController {

    private final AdminContentService adminContentService;

    public AdminContentController(AdminContentService adminContentService) {
        this.adminContentService = adminContentService;
    }

    @GetMapping("/content")
    public ResponseEntity<ApiResponse<List<PortalContentResponse>>> getAllContent() {
        List<PortalContentResponse> list = adminContentService.getAllContent();
        return ResponseEntity.ok(ApiResponse.ok("All portal content retrieved successfully", list));
    }

    @GetMapping("/content/{contentKey}")
    public ResponseEntity<ApiResponse<PortalContentResponse>> getContentByKey(
            @PathVariable("contentKey") String contentKey) {
        PortalContentResponse response = adminContentService.getContentByKey(contentKey);
        return ResponseEntity.ok(ApiResponse.ok("Portal content retrieved successfully", response));
    }

    @PutMapping("/content/{contentKey}")
    public ResponseEntity<ApiResponse<PortalContentResponse>> updateContent(
            @PathVariable("contentKey") String contentKey,
            @Valid @RequestBody ContentUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        PortalContentResponse updated = adminContentService.updateOrCreateContent(
                contentKey, request, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Portal content updated successfully", updated));
    }
}
