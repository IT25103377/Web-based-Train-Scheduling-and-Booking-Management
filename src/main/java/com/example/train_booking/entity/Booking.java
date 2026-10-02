package com.example.train_booking.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String bookingNumber;

    @Column(nullable = false)
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate bookingDate;

    @Column(nullable = false)
    private String bookingStatus;

    @Column(nullable = false)
    private Double bookingAmount;

    @Column(nullable = false)
    private String passengerName;

    @Column(nullable = false)
    private String trainSchedule;

    public Booking() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBookingNumber() { return bookingNumber; }
    public void setBookingNumber(String bookingNumber) { this.bookingNumber = bookingNumber; }
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public Double getBookingAmount() { return bookingAmount; }
    public void setBookingAmount(Double bookingAmount) { this.bookingAmount = bookingAmount; }
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public String getTrainSchedule() { return trainSchedule; }
    public void setTrainSchedule(String trainSchedule) { this.trainSchedule = trainSchedule; }
}
