package com.example.train_scheduling_and_booking_system.controller;

import com.example.train_scheduling_and_booking_system.dto.ApiResponse;
import com.example.train_scheduling_and_booking_system.dto.FinanceReportResponse;
import com.example.train_scheduling_and_booking_system.service.StaffOperationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final StaffOperationsService staffOperationsService;

    public FinanceController(StaffOperationsService staffOperationsService) {
        this.staffOperationsService = staffOperationsService;
    }

    @GetMapping("/report")
    @PreAuthorize("hasAnyRole('FINANCE_OFFICER', 'ADMIN')")
    public ResponseEntity<ApiResponse<FinanceReportResponse>> getFinanceReport() {
        FinanceReportResponse report = staffOperationsService.getFinanceReport();
        return ResponseEntity.ok(ApiResponse.ok("Financial report retrieved successfully", report));
    }
}
