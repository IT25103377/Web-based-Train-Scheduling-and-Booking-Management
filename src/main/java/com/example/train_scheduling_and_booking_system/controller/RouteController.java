package com.example.train_scheduling_and_booking_system.controller;

import com.example.train_scheduling_and_booking_system.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @GetMapping
    @PreAuthorize("hasAnyRole('SCHEDULE_COORDINATOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRoutes() {
        List<Map<String, Object>> routes = List.of(
                Map.of("routeId", 1, "origin", "Colombo Fort", "destination", "Badulla", "distanceKm", 292),
                Map.of("routeId", 2, "origin", "Colombo Fort", "destination", "Kandy", "distanceKm", 115),
                Map.of("routeId", 3, "origin", "Colombo Fort", "destination", "Galle", "distanceKm", 119),
                Map.of("routeId", 4, "origin", "Colombo Fort", "destination", "Jaffna", "distanceKm", 398)
        );
        return ResponseEntity.ok(ApiResponse.ok("Train routes retrieved successfully", routes));
    }
}
