package com.example.train_scheduling_and_booking_system.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BookingResponse {

    private Long bookingId;
    private String bookingReference;
    private String trainNumber;
    private String trainName;
    private String originStation;
    private String destinationStation;
    private String departureTime;
    private String travelDate;
    private Integer passengerCount;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String status;
    private LocalDateTime createdAt;
    private List<PassengerItemDetail> passengers;

    public static class PassengerItemDetail {
        private String fullName;
        private String nicOrPassport;
        private String concessionType;
        private BigDecimal fareApplied;

        public PassengerItemDetail() {}
        public PassengerItemDetail(String fullName, String nicOrPassport, String concessionType, BigDecimal fareApplied) {
            this.fullName = fullName;
            this.nicOrPassport = nicOrPassport;
            this.concessionType = concessionType;
            this.fareApplied = fareApplied;
        }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getNicOrPassport() { return nicOrPassport; }
        public void setNicOrPassport(String nicOrPassport) { this.nicOrPassport = nicOrPassport; }
        public String getConcessionType() { return concessionType; }
        public void setConcessionType(String concessionType) { this.concessionType = concessionType; }
        public BigDecimal getFareApplied() { return fareApplied; }
        public void setFareApplied(BigDecimal fareApplied) { this.fareApplied = fareApplied; }
    }

    public BookingResponse() {}

    public BookingResponse(Long bookingId, String bookingReference, String trainNumber,
                           String trainName, String originStation, String destinationStation,
                           String departureTime, String travelDate, Integer passengerCount,
                           BigDecimal totalAmount, BigDecimal discountAmount, BigDecimal finalAmount,
                           String status, LocalDateTime createdAt, List<PassengerItemDetail> passengers) {
        this.bookingId = bookingId;
        this.bookingReference = bookingReference;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.originStation = originStation;
        this.destinationStation = destinationStation;
        this.departureTime = departureTime;
        this.travelDate = travelDate;
        this.passengerCount = passengerCount;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.passengers = passengers;
    }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }

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

    public String getTravelDate() { return travelDate; }
    public void setTravelDate(String travelDate) { this.travelDate = travelDate; }

    public Integer getPassengerCount() { return passengerCount; }
    public void setPassengerCount(Integer passengerCount) { this.passengerCount = passengerCount; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<PassengerItemDetail> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerItemDetail> passengers) { this.passengers = passengers; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long bookingId;
        private String bookingReference;
        private String trainNumber;
        private String trainName;
        private String originStation;
        private String destinationStation;
        private String departureTime;
        private String travelDate;
        private Integer passengerCount;
        private BigDecimal totalAmount;
        private BigDecimal discountAmount;
        private BigDecimal finalAmount;
        private String status;
        private LocalDateTime createdAt;
        private List<PassengerItemDetail> passengers;

        public Builder bookingId(Long bookingId) { this.bookingId = bookingId; return this; }
        public Builder bookingReference(String bookingReference) { this.bookingReference = bookingReference; return this; }
        public Builder trainNumber(String trainNumber) { this.trainNumber = trainNumber; return this; }
        public Builder trainName(String trainName) { this.trainName = trainName; return this; }
        public Builder originStation(String originStation) { this.originStation = originStation; return this; }
        public Builder destinationStation(String destinationStation) { this.destinationStation = destinationStation; return this; }
        public Builder departureTime(String departureTime) { this.departureTime = departureTime; return this; }
        public Builder travelDate(String travelDate) { this.travelDate = travelDate; return this; }
        public Builder passengerCount(Integer passengerCount) { this.passengerCount = passengerCount; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder discountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; return this; }
        public Builder finalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder passengers(List<PassengerItemDetail> passengers) { this.passengers = passengers; return this; }

        public BookingResponse build() {
            return new BookingResponse(bookingId, bookingReference, trainNumber, trainName,
                    originStation, destinationStation, departureTime, travelDate,
                    passengerCount, totalAmount, discountAmount, finalAmount, status, createdAt, passengers);
        }
    }
}
