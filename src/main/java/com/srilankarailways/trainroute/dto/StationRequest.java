package com.srilankarailways.trainroute.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StationRequest {

    @NotBlank(message = "Station name is required")
    @Size(min = 2, max = 100, message = "Station name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Station code is required")
    @Size(min = 2, max = 20, message = "Station code must be between 2 and 20 characters")
    private String code;

    @NotBlank(message = "Station location is required")
    @Size(min = 2, max = 150, message = "Location must be between 2 and 150 characters")
    private String location;

    private Boolean active = true;

    public StationRequest() {
    }

    public StationRequest(String name, String code, String location, Boolean active) {
        this.name = name;
        this.code = code;
        this.location = location;
        this.active = active != null ? active : true;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
