package com.example.train_scheduling_and_booking_system.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "booking_reference", length = 30, nullable = false, unique = true)
    private String bookingReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id", nullable = false)
    private TrainSchedule schedule;

    @Column(name = "travel_date", length = 20, nullable = false)
    private String travelDate;

    @Column(name = "passenger_count", nullable = false)
    private Integer passengerCount = 1;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "final_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalAmount;

    @Column(name = "status", length = 30, nullable = false)
    private String status = "CONFIRMED";

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingPassenger> passengers = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Booking() {}

    public Booking(Long bookingId, String bookingReference, User user, TrainSchedule schedule,
                   String travelDate, Integer passengerCount, BigDecimal totalAmount,
                   BigDecimal discountAmount, BigDecimal finalAmount, String status,
                   List<BookingPassenger> passengers, LocalDateTime createdAt) {
        this.bookingId = bookingId;
        this.bookingReference = bookingReference;
        this.user = user;
        this.schedule = schedule;
        this.travelDate = travelDate;
        this.passengerCount = passengerCount != null ? passengerCount : 1;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount != null ? discountAmount : BigDecimal.ZERO;
        this.finalAmount = finalAmount;
        this.status = status != null ? status : "CONFIRMED";
        this.passengers = passengers != null ? passengers : new ArrayList<>();
        this.createdAt = createdAt;
    }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public TrainSchedule getSchedule() { return schedule; }
    public void setSchedule(TrainSchedule schedule) { this.schedule = schedule; }

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

    public List<BookingPassenger> getPassengers() { return passengers; }
    public void setPassengers(List<BookingPassenger> passengers) { this.passengers = passengers; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long bookingId;
        private String bookingReference;
        private User user;
        private TrainSchedule schedule;
        private String travelDate;
        private Integer passengerCount = 1;
        private BigDecimal totalAmount;
        private BigDecimal discountAmount = BigDecimal.ZERO;
        private BigDecimal finalAmount;
        private String status = "CONFIRMED";
        private List<BookingPassenger> passengers = new ArrayList<>();
        private LocalDateTime createdAt;

        public Builder bookingId(Long bookingId) { this.bookingId = bookingId; return this; }
        public Builder bookingReference(String bookingReference) { this.bookingReference = bookingReference; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder schedule(TrainSchedule schedule) { this.schedule = schedule; return this; }
        public Builder travelDate(String travelDate) { this.travelDate = travelDate; return this; }
        public Builder passengerCount(Integer passengerCount) { this.passengerCount = passengerCount; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder discountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; return this; }
        public Builder finalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder passengers(List<BookingPassenger> passengers) { this.passengers = passengers; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Booking build() {
            return new Booking(bookingId, bookingReference, user, schedule, travelDate,
                    passengerCount, totalAmount, discountAmount, finalAmount, status, passengers, createdAt);
        }
    }
}
