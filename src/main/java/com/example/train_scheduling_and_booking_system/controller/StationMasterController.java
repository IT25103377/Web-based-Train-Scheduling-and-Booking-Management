package com.example.train_scheduling_and_booking_system.controller;

import com.example.train_scheduling_and_booking_system.dto.ApiResponse;
import com.example.train_scheduling_and_booking_system.dto.BoardingManifestResponse;
import com.example.train_scheduling_and_booking_system.dto.ScheduleResponse;
import com.example.train_scheduling_and_booking_system.service.StaffOperationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/station-master")
public class StationMasterController {

    private final StaffOperationsService staffOperationsService;

    public StationMasterController(StaffOperationsService staffOperationsService) {
        this.staffOperationsService = staffOperationsService;
    }

    @GetMapping("/schedules")
    @PreAuthorize("hasAnyRole('STATION_MASTER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> getStationSchedules(
            @RequestParam(required = false, defaultValue = "Colombo Fort") String station) {
        List<ScheduleResponse> list = staffOperationsService.getStationSchedule(station);
        return ResponseEntity.ok(ApiResponse.ok("Station arrivals/departures retrieved for " + station, list));
    }

    @PatchMapping("/schedules/{id}/platform")
    @PreAuthorize("hasAnyRole('STATION_MASTER', 'ADMIN')")
    public ResponseEntity<ApiResponse<ScheduleResponse>> updatePlatform(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String platform = body.get("platformNumber");
        ScheduleResponse updated = staffOperationsService.updateStationPlatform(id, platform);
        return ResponseEntity.ok(ApiResponse.ok("Platform updated successfully", updated));
    }

    @GetMapping("/schedules/{id}/manifest")
    @PreAuthorize("hasAnyRole('STATION_MASTER', 'ADMIN')")
    public ResponseEntity<ApiResponse<BoardingManifestResponse>> getManifest(@PathVariable Long id) {
        BoardingManifestResponse manifest = staffOperationsService.getBoardingManifest(id);
        return ResponseEntity.ok(ApiResponse.ok("Live passenger manifest retrieved", manifest));
    }
}
