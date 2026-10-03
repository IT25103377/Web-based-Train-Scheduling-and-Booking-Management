package com.srilankarailways.trainroute.controller;

import com.srilankarailways.trainroute.dto.StationRequest;
import com.srilankarailways.trainroute.dto.StationResponse;
import com.srilankarailways.trainroute.service.StationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@CrossOrigin(origins = "*")
public class StationController {

    private final StationService stationService;

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@Valid @RequestBody StationRequest request) {
        StationResponse created = stationService.createStation(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> getAllStations(@RequestParam(required = false) Boolean active) {
        List<StationResponse> stations = stationService.getAllStations(active);
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> getStationById(@PathVariable Long id) {
        StationResponse station = stationService.getStationById(id);
        return ResponseEntity.ok(station);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StationResponse> updateStation(@PathVariable Long id, @Valid @RequestBody StationRequest request) {
        StationResponse updated = stationService.updateStation(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StationResponse> deactivateStation(@PathVariable Long id) {
        StationResponse deactivated = stationService.deactivateStation(id);
        return ResponseEntity.ok(deactivated);
    }
}
