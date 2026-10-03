package com.payment.management.service;

import com.payment.management.model.Payment;
import com.payment.management.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // ================= CREATE PAYMENT =================

    public Payment savePayment(Payment payment) {

        // 1. Validate Booking ID
        if (payment.getBookingId() == null ||
                payment.getBookingId().isBlank()) {

            throw new IllegalArgumentException(
                    "Booking ID is required."
            );
        }

        // 2. Validate Amount
        if (payment.getAmount() <= 0) {

            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero."
            );
        }

        // 3. Validate Payment Method
        if (payment.getPaymentMethod() == null ||
                payment.getPaymentMethod().isBlank()) {

            throw new IllegalArgumentException(
                    "Payment method is required."
            );
        }

        // 4. Validate Payment Method Type
        if (!payment.getPaymentMethod().equals("Credit / Debit Card")
                && !payment.getPaymentMethod().equals("Online Banking")) {

            throw new IllegalArgumentException(
                    "Invalid payment method."
            );
        }

        // 5. ADVANCED VALIDATION
        // Check whether this booking already has
        // a successful payment.

        List<Payment> existingPayments =
                paymentRepository.findByBookingIdAndStatus(
                        payment.getBookingId(),
                        "SUCCESS"
                );

        if (!existingPayments.isEmpty()) {

            throw new IllegalArgumentException(
                    "A successful payment already exists for this booking."
            );
        }

        // 6. Save Payment
        return paymentRepository.save(payment);
    }


    // ================= FIND PAYMENT =================

    public Optional<Payment> findByPaymentId(
            String paymentId) {

        if (paymentId == null ||
                paymentId.isBlank()) {

            return Optional.empty();
        }

        return paymentRepository.findByPaymentId(
                paymentId
        );
    }


    // ================= GET ALL PAYMENTS =================

    public List<Payment> getAllPayments() {

        return paymentRepository.findAll();
    }


    // ================= SEARCH BY BOOKING ID =================

    public List<Payment> searchByBookingId(
            String bookingId) {

        if (bookingId == null ||
                bookingId.isBlank()) {

            return paymentRepository.findAll();
        }

        return paymentRepository
                .findByBookingIdContainingIgnoreCase(
                        bookingId
                );
    }


    // ================= FILTER BY STATUS =================

    public List<Payment> filterByStatus(
            String status) {

        if (status == null ||
                status.isBlank() ||
                status.equalsIgnoreCase("ALL")) {

            return paymentRepository.findAll();
        }

        return paymentRepository.findByStatus(
                status
        );
    }


    // ================= FILTER BY PAYMENT METHOD =================

    public List<Payment> filterByPaymentMethod(
            String paymentMethod) {

        if (paymentMethod == null ||
                paymentMethod.isBlank() ||
                paymentMethod.equalsIgnoreCase("ALL")) {

            return paymentRepository.findAll();
        }

        return paymentRepository
                .findByPaymentMethod(
                        paymentMethod
                );
    }


    // ================= PROCESS REFUND =================

    public Payment processRefund(
            Payment payment,
            String reason) {

        if (payment == null) {

            throw new IllegalArgumentException(
                    "Payment not found."
            );
        }

        if (!"SUCCESS".equals(payment.getStatus())) {

            throw new IllegalArgumentException(
                    "Only successful payments can be refunded."
            );
        }

        if (reason == null ||
                reason.isBlank()) {

            throw new IllegalArgumentException(
                    "Refund reason is required."
            );
        }

        payment.setStatus("REFUNDED");

        payment.setRefundReason(reason);

        return paymentRepository.save(payment);
    }


    // ================= DELETE PAYMENT =================

    public void deletePayment(
            String paymentId) {

        Payment payment =
                paymentRepository
                        .findByPaymentId(paymentId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment ID not found."
                                )
                        );

        paymentRepository.delete(payment);
    }

}