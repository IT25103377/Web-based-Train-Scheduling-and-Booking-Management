package com.example.train_scheduling_and_booking_system.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "train_schedules")
public class TrainSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "train_number", length = 20, nullable = false)
    private String trainNumber;

    @Column(name = "train_name", length = 100, nullable = false)
    private String trainName;

    @Column(name = "origin_station", length = 100, nullable = false)
    private String originStation;

    @Column(name = "destination_station", length = 100, nullable = false)
    private String destinationStation;

    @Column(name = "departure_time", length = 20, nullable = false)
    private String departureTime;

    @Column(name = "arrival_time", length = 20, nullable = false)
    private String arrivalTime;

    @Column(name = "travel_date", length = 20, nullable = false)
    private String travelDate;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats = 120;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats = 120;

    @Column(name = "base_fare", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseFare = BigDecimal.valueOf(500.00);

    @Column(name = "platform_number", length = 10, nullable = false)
    private String platformNumber = "1";

    @Column(name = "status", length = 30, nullable = false)
    private String status = "ON_TIME";

    @Column(name = "delay_minutes", nullable = false)
    private Integer delayMinutes = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public TrainSchedule() {}

    public TrainSchedule(Long scheduleId, String trainNumber, String trainName, String originStation,
                         String destinationStation, String departureTime, String arrivalTime,
                         String travelDate, Integer totalSeats, Integer availableSeats,
                         BigDecimal baseFare, String platformNumber, String status,
                         Integer delayMinutes, LocalDateTime createdAt) {
        this.scheduleId = scheduleId;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.originStation = originStation;
        this.destinationStation = destinationStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.travelDate = travelDate;
        this.totalSeats = totalSeats != null ? totalSeats : 120;
        this.availableSeats = availableSeats != null ? availableSeats : 120;
        this.baseFare = baseFare != null ? baseFare : BigDecimal.valueOf(500.00);
        this.platformNumber = platformNumber != null ? platformNumber : "1";
        this.status = status != null ? status : "ON_TIME";
        this.delayMinutes = delayMinutes != null ? delayMinutes : 0;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

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
        private Integer totalSeats = 120;
        private Integer availableSeats = 120;
        private BigDecimal baseFare = BigDecimal.valueOf(500.00);
        private String platformNumber = "1";
        private String status = "ON_TIME";
        private Integer delayMinutes = 0;
        private LocalDateTime createdAt;

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
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public TrainSchedule build() {
            return new TrainSchedule(scheduleId, trainNumber, trainName, originStation,
                    destinationStation, departureTime, arrivalTime, travelDate,
                    totalSeats, availableSeats, baseFare, platformNumber, status, delayMinutes, createdAt);
        }
    }
}
