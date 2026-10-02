package com.example.train_scheduling_and_booking_system.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_transactions")
public class FinancialTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "transaction_ref", length = 40, nullable = false, unique = true)
    private String transactionRef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_type", length = 30, nullable = false)
    private String transactionType = "PAYMENT"; // 'PAYMENT', 'REFUND'

    @Column(name = "payment_method", length = 50, nullable = false)
    private String paymentMethod = "CREDIT_CARD";

    @Column(name = "status", length = 30, nullable = false)
    private String status = "SUCCESS";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public FinancialTransaction() {}

    public FinancialTransaction(Long transactionId, String transactionRef, Booking booking,
                                BigDecimal amount, String transactionType, String paymentMethod,
                                String status, LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.transactionRef = transactionRef;
        this.booking = booking;
        this.amount = amount;
        this.transactionType = transactionType != null ? transactionType : "PAYMENT";
        this.paymentMethod = paymentMethod != null ? paymentMethod : "CREDIT_CARD";
        this.status = status != null ? status : "SUCCESS";
        this.createdAt = createdAt;
    }

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long transactionId;
        private String transactionRef;
        private Booking booking;
        private BigDecimal amount;
        private String transactionType = "PAYMENT";
        private String paymentMethod = "CREDIT_CARD";
        private String status = "SUCCESS";
        private LocalDateTime createdAt;

        public Builder transactionId(Long transactionId) { this.transactionId = transactionId; return this; }
        public Builder transactionRef(String transactionRef) { this.transactionRef = transactionRef; return this; }
        public Builder booking(Booking booking) { this.booking = booking; return this; }
        public Builder amount(BigDecimal amount) { this.amount = amount; return this; }
        public Builder transactionType(String transactionType) { this.transactionType = transactionType; return this; }
        public Builder paymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public FinancialTransaction build() {
            return new FinancialTransaction(transactionId, transactionRef, booking, amount,
                    transactionType, paymentMethod, status, createdAt);
        }
    }
}
