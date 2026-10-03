package com.srilankarailways.trainroute.dto;

import com.srilankarailways.trainroute.entity.Route;
import com.srilankarailways.trainroute.entity.RouteStation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RouteResponse {

    private Long routeId;
    private String routeCode;
    private String routeName;
    private String origin;
    private String destination;
    private Integer totalStations;
    private Boolean active;
    private List<RouteStationResponse> stations = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RouteResponse() {
    }

    public RouteResponse(Long routeId, String routeCode, String routeName, String origin, String destination, Integer totalStations, Boolean active, List<RouteStationResponse> stations, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.routeId = routeId;
        this.routeCode = routeCode;
        this.routeName = routeName;
        this.origin = origin;
        this.destination = destination;
        this.totalStations = totalStations;
        this.active = active;
        this.stations = stations;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RouteResponse fromEntity(Route route) {
        if (route == null) return null;

        List<RouteStationResponse> stationDtos = new ArrayList<>();
        String originName = "N/A";
        String destinationName = "N/A";

        if (route.getRouteStations() != null && !route.getRouteStations().isEmpty()) {
            List<RouteStation> sorted = route.getRouteStations().stream()
                    .sorted(Comparator.comparingInt(RouteStation::getStopOrder))
                    .collect(Collectors.toList());

            stationDtos = sorted.stream()
                    .map(RouteStationResponse::fromEntity)
                    .collect(Collectors.toList());

            if (!sorted.isEmpty()) {
                originName = sorted.get(0).getStation() != null ? sorted.get(0).getStation().getName() : "N/A";
                destinationName = sorted.get(sorted.size() - 1).getStation() != null ? sorted.get(sorted.size() - 1).getStation().getName() : "N/A";
            }
        }

        return new RouteResponse(
                route.getId(),
                route.getRouteCode(),
                route.getRouteName(),
                originName,
                destinationName,
                stationDtos.size(),
                route.getActive(),
                stationDtos,
                route.getCreatedAt(),
                route.getUpdatedAt()
        );
    }

    // Getters and Setters
    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getTotalStations() {
        return totalStations;
    }

    public void setTotalStations(Integer totalStations) {
        this.totalStations = totalStations;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<RouteStationResponse> getStations() {
        return stations;
    }

    public void setStations(List<RouteStationResponse> stations) {
        this.stations = stations;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
