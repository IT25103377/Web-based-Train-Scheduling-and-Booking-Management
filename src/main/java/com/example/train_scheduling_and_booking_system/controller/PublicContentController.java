package com.example.train_scheduling_and_booking_system.controller;

import com.example.train_scheduling_and_booking_system.dto.ApiResponse;
import com.example.train_scheduling_and_booking_system.dto.PortalContentResponse;
import com.example.train_scheduling_and_booking_system.service.AdminContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicContentController {

    private final AdminContentService adminContentService;

    public PublicContentController(AdminContentService adminContentService) {
        this.adminContentService = adminContentService;
    }

    @GetMapping("/landing-content")
    public ResponseEntity<ApiResponse<List<PortalContentResponse>>> getLandingContent() {
        List<PortalContentResponse> content = adminContentService.getPublicLandingContent();
        return ResponseEntity.ok(ApiResponse.ok("Landing content retrieved successfully", content));
    }
}
