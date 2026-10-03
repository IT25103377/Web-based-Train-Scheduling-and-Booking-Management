package com.payment.management.repository;

import com.payment.management.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentId(String paymentId);

    List<Payment> findByBookingIdContainingIgnoreCase(String bookingId);

    List<Payment> findByStatus(String status);

    List<Payment> findByPaymentMethod(String paymentMethod);

    List<Payment> findByBookingIdAndStatus(
            String bookingId,
            String status
    );
}