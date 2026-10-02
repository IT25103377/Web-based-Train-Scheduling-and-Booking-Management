package com.example.train_scheduling_and_booking_system.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class BookingCreateRequest {

    @NotNull(message = "Schedule ID is required")
    private Long scheduleId;

    private String travelDate;

    @NotEmpty(message = "At least one passenger is required")
    @Valid
    private List<PassengerItemRequest> passengers;

    public BookingCreateRequest() {}

    public BookingCreateRequest(Long scheduleId, String travelDate, List<PassengerItemRequest> passengers) {
        this.scheduleId = scheduleId;
        this.travelDate = travelDate;
        this.passengers = passengers;
    }

    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }

    public String getTravelDate() { return travelDate; }
    public void setTravelDate(String travelDate) { this.travelDate = travelDate; }

    public List<PassengerItemRequest> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerItemRequest> passengers) { this.passengers = passengers; }
}
