package com.example.train_scheduling_and_booking_system.dto;

import java.util.List;

public class BoardingManifestResponse {
    private Long scheduleId;
    private String trainNumber;
    private String trainName;
    private String originStation;
    private String destinationStation;
    private String departureTime;
    private String arrivalTime;
    private Integer platformNumber;
    private Integer totalCapacity;
    private Integer totalBookedSeats;
    private List<ManifestPassenger> passengers;

    public static class ManifestPassenger {
        private String bookingReference;
        private String passengerName;
        private String nicOrPassport;
        private String concessionType;
        private String seatIdentifier;
        private String status;

        public ManifestPassenger() {}

        public ManifestPassenger(String bookingReference, String passengerName, String nicOrPassport,
                                 String concessionType, String seatIdentifier, String status) {
            this.bookingReference = bookingReference;
            this.passengerName = passengerName;
            this.nicOrPassport = nicOrPassport;
            this.concessionType = concessionType;
            this.seatIdentifier = seatIdentifier;
            this.status = status;
        }

        public String getBookingReference() { return bookingReference; }
        public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }

        public String getPassengerName() { return passengerName; }
        public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

        public String getNicOrPassport() { return nicOrPassport; }
        public void setNicOrPassport(String nicOrPassport) { this.nicOrPassport = nicOrPassport; }

        public String getConcessionType() { return concessionType; }
        public void setConcessionType(String concessionType) { this.concessionType = concessionType; }

        public String getSeatIdentifier() { return seatIdentifier; }
        public void setSeatIdentifier(String seatIdentifier) { this.seatIdentifier = seatIdentifier; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public BoardingManifestResponse() {}

    public BoardingManifestResponse(Long scheduleId, String trainNumber, String trainName,
                                  String originStation, String destinationStation,
                                  String departureTime, String arrivalTime, Integer platformNumber,
                                  Integer totalCapacity, Integer totalBookedSeats,
                                  List<ManifestPassenger> passengers) {
        this.scheduleId = scheduleId;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.originStation = originStation;
        this.destinationStation = destinationStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.platformNumber = platformNumber;
        this.totalCapacity = totalCapacity;
        this.totalBookedSeats = totalBookedSeats;
        this.passengers = passengers;
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

    public Integer getPlatformNumber() { return platformNumber; }
    public void setPlatformNumber(Integer platformNumber) { this.platformNumber = platformNumber; }

    public Integer getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(Integer totalCapacity) { this.totalCapacity = totalCapacity; }

    public Integer getTotalBookedSeats() { return totalBookedSeats; }
    public void setTotalBookedSeats(Integer totalBookedSeats) { this.totalBookedSeats = totalBookedSeats; }

    public List<ManifestPassenger> getPassengers() { return passengers; }
    public void setPassengers(List<ManifestPassenger> passengers) { this.passengers = passengers; }
}
