package com.srilankarailways.trainroute.controller;

import com.srilankarailways.trainroute.dto.RouteRequest;
import com.srilankarailways.trainroute.dto.RouteResponse;
import com.srilankarailways.trainroute.dto.RouteStationResponse;
import com.srilankarailways.trainroute.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
public class RouteController {

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    public ResponseEntity<RouteResponse> createRoute(@Valid @RequestBody RouteRequest request) {
        RouteResponse created = routeService.createRoute(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAllRoutes(@RequestParam(required = false) Boolean active) {
        List<RouteResponse> routes = routeService.getAllRoutes(active);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/active")
    public ResponseEntity<List<RouteResponse>> getActiveRoutes() {
        List<RouteResponse> routes = routeService.getActiveRoutes();
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getRouteById(@PathVariable Long id) {
        RouteResponse route = routeService.getRouteById(id);
        return ResponseEntity.ok(route);
    }

    @GetMapping("/{id}/stations")
    public ResponseEntity<List<RouteStationResponse>> getRouteStations(@PathVariable Long id) {
        List<RouteStationResponse> stations = routeService.getRouteStations(id);
        return ResponseEntity.ok(stations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteResponse> updateRoute(@PathVariable Long id, @Valid @RequestBody RouteRequest request) {
        RouteResponse updated = routeService.updateRoute(id, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RouteResponse> updateRouteStatus(@PathVariable Long id, @RequestParam Boolean active) {
        RouteResponse updated = routeService.updateRouteStatus(id, active);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RouteResponse> deactivateRoute(@PathVariable Long id) {
        RouteResponse deactivated = routeService.deactivateRoute(id);
        return ResponseEntity.ok(deactivated);
    }
}
