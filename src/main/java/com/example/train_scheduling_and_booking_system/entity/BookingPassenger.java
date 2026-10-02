package com.example.train_scheduling_and_booking_system.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "booking_passengers")
public class BookingPassenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "nic_or_passport", length = 30, nullable = false)
    private String nicOrPassport;

    @Column(name = "concession_type", length = 50, nullable = false)
    private String concessionType = "NONE";

    @Column(name = "fare_applied", nullable = false, precision = 10, scale = 2)
    private BigDecimal fareApplied;

    public BookingPassenger() {}

    public BookingPassenger(Long id, Booking booking, String fullName, String nicOrPassport,
                            String concessionType, BigDecimal fareApplied) {
        this.id = id;
        this.booking = booking;
        this.fullName = fullName;
        this.nicOrPassport = nicOrPassport;
        this.concessionType = concessionType != null ? concessionType : "NONE";
        this.fareApplied = fareApplied;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getNicOrPassport() { return nicOrPassport; }
    public void setNicOrPassport(String nicOrPassport) { this.nicOrPassport = nicOrPassport; }

    public String getConcessionType() { return concessionType; }
    public void setConcessionType(String concessionType) { this.concessionType = concessionType; }

    public BigDecimal getFareApplied() { return fareApplied; }
    public void setFareApplied(BigDecimal fareApplied) { this.fareApplied = fareApplied; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Booking booking;
        private String fullName;
        private String nicOrPassport;
        private String concessionType = "NONE";
        private BigDecimal fareApplied;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder booking(Booking booking) { this.booking = booking; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder nicOrPassport(String nicOrPassport) { this.nicOrPassport = nicOrPassport; return this; }
        public Builder concessionType(String concessionType) { this.concessionType = concessionType; return this; }
        public Builder fareApplied(BigDecimal fareApplied) { this.fareApplied = fareApplied; return this; }

        public BookingPassenger build() {
            return new BookingPassenger(id, booking, fullName, nicOrPassport, concessionType, fareApplied);
        }
    }
}
