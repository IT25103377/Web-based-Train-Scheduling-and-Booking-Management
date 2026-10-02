package com.srilankarailways.trainroute.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class RouteRequest {

    @NotBlank(message = "Route code is required")
    @Size(min = 2, max = 50, message = "Route code must be between 2 and 50 characters")
    private String routeCode;

    @NotBlank(message = "Route name is required")
    @Size(min = 3, max = 150, message = "Route name must be between 3 and 150 characters")
    private String routeName;

    @NotEmpty(message = "Station list must not be empty")
    @Size(min = 2, message = "A route must contain at least two stations")
    private List<Long> stationIds;

    private Boolean active = true;

    public RouteRequest() {
    }

    public RouteRequest(String routeCode, String routeName, List<Long> stationIds, Boolean active) {
        this.routeCode = routeCode;
        this.routeName = routeName;
        this.stationIds = stationIds;
        this.active = active != null ? active : true;
    }

    // Getters and Setters
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

    public List<Long> getStationIds() {
        return stationIds;
    }

    public void setStationIds(List<Long> stationIds) {
        this.stationIds = stationIds;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
