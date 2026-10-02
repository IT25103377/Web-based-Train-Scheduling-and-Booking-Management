package com.payment.management.repository;

import com.payment.management.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    // Find a refund using Payment ID
    Optional<Refund> findByPaymentId(String paymentId);

    // Get all refunds for Refund History
    List<Refund> findAll();
}