package com.example.train_scheduling_and_booking_system.dto;

import java.math.BigDecimal;

public class ScheduleResponse {

    private Long scheduleId;
    private String trainNumber;
    private String trainName;
    private String originStation;
    private String destinationStation;
    private String departureTime;
    private String arrivalTime;
    private String travelDate;
    private Integer totalSeats;
    private Integer availableSeats;
    private BigDecimal baseFare;
    private String platformNumber;
    private String status;
    private Integer delayMinutes;

    public ScheduleResponse() {}

    public ScheduleResponse(Long scheduleId, String trainNumber, String trainName, String originStation,
                            String destinationStation, String departureTime, String arrivalTime,
                            String travelDate, Integer totalSeats, Integer availableSeats,
                            BigDecimal baseFare, String platformNumber, String status, Integer delayMinutes) {
        this.scheduleId = scheduleId;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.originStation = originStation;
        this.destinationStation = destinationStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.travelDate = travelDate;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.baseFare = baseFare;
        this.platformNumber = platformNumber;
        this.status = status;
        this.delayMinutes = delayMinutes;
    }

    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }

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

    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public BigDecimal getBaseFare() { return baseFare; }
    public void setBaseFare(BigDecimal baseFare) { this.baseFare = baseFare; }

    public String getPlatformNumber() { return platformNumber; }
    public void setPlatformNumber(String platformNumber) { this.platformNumber = platformNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getDelayMinutes() { return delayMinutes; }
    public void setDelayMinutes(Integer delayMinutes) { this.delayMinutes = delayMinutes; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long scheduleId;
        private String trainNumber;
        private String trainName;
        private String originStation;
        private String destinationStation;
        private String departureTime;
        private String arrivalTime;
        private String travelDate;
        private Integer totalSeats;
        private Integer availableSeats;
        private BigDecimal baseFare;
        private String platformNumber;
        private String status;
        private Integer delayMinutes;

        public Builder scheduleId(Long scheduleId) { this.scheduleId = scheduleId; return this; }
        public Builder trainNumber(String trainNumber) { this.trainNumber = trainNumber; return this; }
        public Builder trainName(String trainName) { this.trainName = trainName; return this; }
        public Builder originStation(String originStation) { this.originStation = originStation; return this; }
        public Builder destinationStation(String destinationStation) { this.destinationStation = destinationStation; return this; }
        public Builder departureTime(String departureTime) { this.departureTime = departureTime; return this; }
        public Builder arrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; return this; }
        public Builder travelDate(String travelDate) { this.travelDate = travelDate; return this; }
        public Builder totalSeats(Integer totalSeats) { this.totalSeats = totalSeats; return this; }
        public Builder availableSeats(Integer availableSeats) { this.availableSeats = availableSeats; return this; }
        public Builder baseFare(BigDecimal baseFare) { this.baseFare = baseFare; return this; }
        public Builder platformNumber(String platformNumber) { this.platformNumber = platformNumber; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder delayMinutes(Integer delayMinutes) { this.delayMinutes = delayMinutes; return this; }

        public ScheduleResponse build() {
            return new ScheduleResponse(scheduleId, trainNumber, trainName, originStation,
                    destinationStation, departureTime, arrivalTime, travelDate,
                    totalSeats, availableSeats, baseFare, platformNumber, status, delayMinutes);
        }
    }
}
