package com.example.train_scheduling_and_booking_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ScheduleCreateRequest {

    @NotBlank(message = "Train number is required")
    private String trainNumber;

    @NotBlank(message = "Train name is required")
    private String trainName;

    @NotBlank(message = "Origin station is required")
    private String originStation;

    @NotBlank(message = "Destination station is required")
    private String destinationStation;

    @NotBlank(message = "Departure time is required")
    private String departureTime;

    @NotBlank(message = "Arrival time is required")
    private String arrivalTime;

    @NotBlank(message = "Travel date is required")
    private String travelDate;

    @NotNull(message = "Total seats is required")
    private Integer totalSeats = 120;

    @NotNull(message = "Base fare is required")
    private BigDecimal baseFare = BigDecimal.valueOf(500.00);

    private String platformNumber = "1";

    public ScheduleCreateRequest() {}

    public ScheduleCreateRequest(String trainNumber, String trainName, String originStation,
                                 String destinationStation, String departureTime, String arrivalTime,
                                 String travelDate, Integer totalSeats, BigDecimal baseFare, String platformNumber) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.originStation = originStation;
        this.destinationStation = destinationStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.travelDate = travelDate;
        this.totalSeats = totalSeats;
        this.baseFare = baseFare;
        this.platformNumber = platformNumber;
    }

    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }

    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }

    public String getOriginStation() { return originStation; }
    public void setOriginStation(String originStation) { this.originStation = originStation; }

    public String getDestinationStation() { return destinationStation; }
    public void setDestinationStation(String destinationStation) { this.destinationStation = destinationStation; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getTravelDate() { return travelDate; }
    public void setTravelDate(String travelDate) { this.travelDate = travelDate; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }

    public BigDecimal getBaseFare() { return baseFare; }
    public void setBaseFare(BigDecimal baseFare) { this.baseFare = baseFare; }

    public String getPlatformNumber() { return platformNumber; }
    public void setPlatformNumber(String platformNumber) { this.platformNumber = platformNumber; }
}
