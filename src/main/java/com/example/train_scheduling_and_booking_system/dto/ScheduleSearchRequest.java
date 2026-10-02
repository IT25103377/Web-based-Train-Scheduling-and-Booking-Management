package com.example.train_scheduling_and_booking_system.dto;

public class ScheduleSearchRequest {

    private String origin;
    private String destination;
    private String travelDate;
    private Integer passengerCount = 1;

    public ScheduleSearchRequest() {}

    public ScheduleSearchRequest(String origin, String destination, String travelDate, Integer passengerCount) {
        this.origin = origin;
        this.destination = destination;
        this.travelDate = travelDate;
        this.passengerCount = passengerCount != null ? passengerCount : 1;
    }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getTravelDate() { return travelDate; }
    public void setTravelDate(String travelDate) { this.travelDate = travelDate; }

    public Integer getPassengerCount() { return passengerCount; }
    public void setPassengerCount(Integer passengerCount) { this.passengerCount = passengerCount; }
}
