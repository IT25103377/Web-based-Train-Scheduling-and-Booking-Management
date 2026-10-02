package com.payment.management.service;

import com.payment.management.model.Payment;
import com.payment.management.model.Refund;
import com.payment.management.repository.RefundRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RefundService {

    private final RefundRepository refundRepository;

    public RefundService(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }


    // =========================================================
    // CREATE REFUND
    // =========================================================

    public Refund createRefund(
            Payment payment,
            String refundReason) {

        if (payment == null) {
            throw new IllegalArgumentException(
                    "Payment not found."
            );
        }

        if (refundReason == null ||
                refundReason.isBlank()) {

            throw new IllegalArgumentException(
                    "Refund reason is required."
            );
        }


        // Check whether a refund already exists
        Optional<Refund> existingRefund =
                refundRepository.findByPaymentId(
                        payment.getPaymentId()
                );


        if (existingRefund.isPresent()) {

            throw new IllegalArgumentException(
                    "A refund already exists for this payment."
            );
        }


        // Create new Refund record
        Refund refund = new Refund();

        refund.setBookingId(
                payment.getBookingId()
        );

        refund.setPaymentId(
                payment.getPaymentId()
        );

        refund.setRefundAmount(
                payment.getAmount()
        );

        refund.setRefundDate(
                LocalDateTime.now()
        );

        refund.setRefundId(
                "REF" + System.currentTimeMillis()
        );

        refund.setRefundReason(
                refundReason
        );

        refund.setRefundStatus(
                "PROCESSED"
        );


        return refundRepository.save(refund);
    }


    // =========================================================
    // FIND REFUND BY PAYMENT ID
    // =========================================================

    public Optional<Refund> findByPaymentId(
            String paymentId) {

        if (paymentId == null ||
                paymentId.isBlank()) {

            return Optional.empty();
        }

        return refundRepository.findByPaymentId(
                paymentId
        );
    }


    // =========================================================
    // GET ALL REFUNDS
    // =========================================================

    public List<Refund> getAllRefunds() {

        return refundRepository.findAll();
    }
}