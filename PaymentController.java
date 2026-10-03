package com.payment.management;

import com.payment.management.model.Payment;
import com.payment.management.model.Refund;
import com.payment.management.service.PaymentService;
import com.payment.management.service.RefundService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class PaymentController {

    private final PaymentService paymentService;
    private final RefundService refundService;

    public PaymentController(
            PaymentService paymentService,
            RefundService refundService) {

        this.paymentService = paymentService;
        this.refundService = refundService;
    }


    // =========================================================
    // PAYMENT PAGE
    // =========================================================

    @GetMapping("/payment")
    public String paymentPage() {
        return "payment";
    }


    // =========================================================
    // PROCESS PAYMENT
    // =========================================================

    @PostMapping("/process-payment")
    public String processPayment(
            @RequestParam String bookingId,
            @RequestParam double amount,
            @RequestParam String paymentMethod,
            Model model) {

        try {

            Payment payment = new Payment();

            payment.setPaymentId(
                    "PAY" + System.currentTimeMillis()
            );

            payment.setBookingId(bookingId);
            payment.setAmount(amount);
            payment.setPaymentMethod(paymentMethod);
            payment.setStatus("SUCCESS");
            payment.setPaymentDate(LocalDateTime.now());

            Payment savedPayment =
                    paymentService.savePayment(payment);

            model.addAttribute(
                    "payment",
                    savedPayment
            );

            return "payment-success";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "error",
                    e.getMessage()
            );

            return "payment";
        }
    }


    // =========================================================
    // PAYMENT HISTORY
    // =========================================================

    @GetMapping("/payment-history")
    public String paymentHistory(
            @RequestParam(required = false) String bookingId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String paymentMethod,
            Model model) {

        List<Payment> payments;

        boolean hasBookingSearch =
                bookingId != null && !bookingId.isBlank();

        boolean hasStatusFilter =
                status != null &&
                        !status.isBlank() &&
                        !status.equalsIgnoreCase("ALL");

        boolean hasMethodFilter =
                paymentMethod != null &&
                        !paymentMethod.isBlank() &&
                        !paymentMethod.equalsIgnoreCase("ALL");


        if (!hasBookingSearch &&
                !hasStatusFilter &&
                !hasMethodFilter) {

            payments = paymentService.getAllPayments();

        } else if (hasBookingSearch &&
                !hasStatusFilter &&
                !hasMethodFilter) {

            payments =
                    paymentService.searchByBookingId(
                            bookingId
                    );

        } else if (!hasBookingSearch &&
                hasStatusFilter &&
                !hasMethodFilter) {

            payments =
                    paymentService.filterByStatus(
                            status
                    );

        } else if (!hasBookingSearch &&
                !hasStatusFilter &&
                hasMethodFilter) {

            payments =
                    paymentService.filterByPaymentMethod(
                            paymentMethod
                    );

        } else {

            payments = paymentService.getAllPayments();

            if (hasBookingSearch) {
                payments = payments.stream()
                        .filter(payment ->
                                payment.getBookingId()
                                        .toLowerCase()
                                        .contains(
                                                bookingId.toLowerCase()
                                        )
                        )
                        .toList();
            }

            if (hasStatusFilter) {
                payments = payments.stream()
                        .filter(payment ->
                                payment.getStatus()
                                        .equalsIgnoreCase(status)
                        )
                        .toList();
            }

            if (hasMethodFilter) {
                payments = payments.stream()
                        .filter(payment ->
                                payment.getPaymentMethod()
                                        .equalsIgnoreCase(paymentMethod)
                        )
                        .toList();
            }
        }


        model.addAttribute("payments", payments);
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("status", status);
        model.addAttribute("paymentMethod", paymentMethod);

        return "payment-history";
    }


    // =========================================================
    // PAYMENT VERIFICATION PAGE
    // =========================================================

    @GetMapping("/payment-verification")
    public String paymentVerificationPage(Model model) {
        return "payment-verification";
    }


    // =========================================================
    // VERIFY PAYMENT
    // =========================================================

    @PostMapping("/verify-payment")
    public String verifyPayment(
            @RequestParam String paymentId,
            Model model) {

        Payment payment =
                paymentService
                        .findByPaymentId(paymentId)
                        .orElse(null);


        if (payment == null) {

            model.addAttribute(
                    "paymentId",
                    paymentId
            );

            model.addAttribute(
                    "verificationMessage",
                    "Payment ID not found in database."
            );

            model.addAttribute(
                    "paymentFound",
                    false
            );

        } else {

            model.addAttribute(
                    "payment",
                    payment
            );

            model.addAttribute(
                    "paymentFound",
                    true
            );

            model.addAttribute(
                    "verificationMessage",
                    "Payment successfully verified."
            );
        }

        return "payment-verification";
    }


    // =========================================================
    // REFUND PAGE
    // =========================================================

    @GetMapping("/payment-refund")
    public String refundPage(Model model) {

        model.addAttribute("paymentFound", false);
        model.addAttribute("refundEligible", false);
        model.addAttribute("refundConfirmed", false);
        model.addAttribute("refundProcessed", false);
        model.addAttribute("alreadyRefunded", false);

        return "payment-refund";
    }


    // =========================================================
    // SEARCH PAYMENT FOR REFUND
    // =========================================================

    @PostMapping("/refund-search")
    public String searchRefundPayment(
            @RequestParam String paymentId,
            Model model) {

        model.addAttribute("paymentFound", false);
        model.addAttribute("refundEligible", false);
        model.addAttribute("refundConfirmed", false);
        model.addAttribute("refundProcessed", false);
        model.addAttribute("alreadyRefunded", false);

        model.addAttribute(
                "paymentId",
                paymentId
        );


        // -----------------------------------------------------
        // Validate Payment ID
        // -----------------------------------------------------

        if (paymentId == null ||
                paymentId.isBlank()) {

            model.addAttribute(
                    "refundMessage",
                    "Please enter a Payment ID."
            );

            return "payment-refund";
        }


        // -----------------------------------------------------
        // Find Payment
        // -----------------------------------------------------

        Payment payment =
                paymentService
                        .findByPaymentId(paymentId)
                        .orElse(null);


        if (payment == null) {

            model.addAttribute(
                    "refundMessage",
                    "Payment ID not found in database."
            );

            return "payment-refund";
        }


        model.addAttribute(
                "paymentFound",
                true
        );

        model.addAttribute(
                "payment",
                payment
        );


        // -----------------------------------------------------
        // NEW:
        // Check Refund table
        // -----------------------------------------------------

        Optional<Refund> existingRefund =
                refundService.findByPaymentId(
                        paymentId
                );


        if (existingRefund.isPresent()) {

            Refund refund =
                    existingRefund.get();

            // Send actual Refund object to HTML
            model.addAttribute(
                    "refund",
                    refund
            );

            model.addAttribute(
                    "alreadyRefunded",
                    true
            );

            model.addAttribute(
                    "refundMessage",
                    "A refund already exists for this payment."
            );

            return "payment-refund";
        }


        // -----------------------------------------------------
        // Check Payment Status
        // -----------------------------------------------------

        if (!"SUCCESS".equals(payment.getStatus())) {

            model.addAttribute(
                    "refundMessage",
                    "This payment is not eligible for a refund because its status is not SUCCESS."
            );

            return "payment-refund";
        }


        // -----------------------------------------------------
        // Payment is eligible
        // -----------------------------------------------------

        model.addAttribute(
                "refundEligible",
                true
        );

        model.addAttribute(
                "refundMessage",
                "Payment found successfully. This payment is eligible for a refund."
        );

        return "payment-refund";
    }


    // =========================================================
    // CONFIRM REFUND
    // =========================================================

    @PostMapping("/refund-confirm")
    public String confirmRefund(
            @RequestParam String paymentId,
            @RequestParam String refundReason,
            Model model) {

        model.addAttribute("paymentFound", false);
        model.addAttribute("refundEligible", false);
        model.addAttribute("refundConfirmed", false);
        model.addAttribute("refundProcessed", false);
        model.addAttribute("alreadyRefunded", false);

        model.addAttribute(
                "paymentId",
                paymentId
        );

        model.addAttribute(
                "refundReason",
                refundReason
        );


        if (paymentId == null ||
                paymentId.isBlank()) {

            model.addAttribute(
                    "refundMessage",
                    "Payment ID is required."
            );

            return "payment-refund";
        }


        Payment payment =
                paymentService
                        .findByPaymentId(paymentId)
                        .orElse(null);


        if (payment == null) {

            model.addAttribute(
                    "refundMessage",
                    "Payment ID not found in database."
            );

            return "payment-refund";
        }


        model.addAttribute(
                "payment",
                payment
        );

        model.addAttribute(
                "paymentFound",
                true
        );


        // Check Refund table
        Optional<Refund> existingRefund =
                refundService.findByPaymentId(
                        paymentId
                );


        if (existingRefund.isPresent()) {

            model.addAttribute(
                    "refund",
                    existingRefund.get()
            );

            model.addAttribute(
                    "alreadyRefunded",
                    true
            );

            model.addAttribute(
                    "refundMessage",
                    "A refund already exists for this payment."
            );

            return "payment-refund";
        }


        if (!"SUCCESS".equals(payment.getStatus())) {

            model.addAttribute(
                    "refundMessage",
                    "Only successful payments can be refunded."
            );

            return "payment-refund";
        }


        if (refundReason == null ||
                refundReason.isBlank()) {

            model.addAttribute(
                    "refundEligible",
                    true
            );

            model.addAttribute(
                    "refundMessage",
                    "Please select a refund reason."
            );

            return "payment-refund";
        }


        model.addAttribute(
                "refundEligible",
                true
        );

        model.addAttribute(
                "refundConfirmed",
                true
        );

        model.addAttribute(
                "refundMessage",
                "Please review the refund details and confirm the refund."
        );

        return "payment-refund";
    }


    // =========================================================
    // PROCESS REFUND
    // =========================================================

    @PostMapping("/process-refund")
    public String processRefund(
            @RequestParam String paymentId,
            @RequestParam String refundReason,
            Model model) {

        model.addAttribute(
                "paymentId",
                paymentId
        );

        model.addAttribute(
                "refundProcessed",
                false
        );

        model.addAttribute(
                "alreadyRefunded",
                false
        );


        if (paymentId == null ||
                paymentId.isBlank()) {

            model.addAttribute(
                    "refundMessage",
                    "Payment ID is required."
            );

            return "payment-refund";
        }


        Payment payment =
                paymentService
                        .findByPaymentId(paymentId)
                        .orElse(null);


        if (payment == null) {

            model.addAttribute(
                    "refundMessage",
                    "Payment ID not found in database."
            );

            return "payment-refund";
        }


        model.addAttribute(
                "payment",
                payment
        );


        // Check Refund table first
        Optional<Refund> existingRefund =
                refundService.findByPaymentId(
                        paymentId
                );


        if (existingRefund.isPresent()) {

            model.addAttribute(
                    "refund",
                    existingRefund.get()
            );

            model.addAttribute(
                    "alreadyRefunded",
                    true
            );

            model.addAttribute(
                    "refundMessage",
                    "A refund already exists for this payment."
            );

            return "payment-refund";
        }


        if (!"SUCCESS".equals(payment.getStatus())) {

            model.addAttribute(
                    "refundMessage",
                    "Only successful payments can be refunded."
            );

            return "payment-refund";
        }


        if (refundReason == null ||
                refundReason.isBlank()) {

            model.addAttribute(
                    "refundMessage",
                    "Refund reason is required."
            );

            return "payment-refund";
        }


        try {

            // Create Refund database record
            Refund refund =
                    refundService.createRefund(
                            payment,
                            refundReason
                    );


            // Update Payment
            payment.setStatus(
                    "REFUNDED"
            );

            payment.setRefundReason(
                    refundReason
            );


            Payment refundedPayment =
                    paymentService.savePayment(
                            payment
                    );


            model.addAttribute(
                    "payment",
                    refundedPayment
            );

            model.addAttribute(
                    "refund",
                    refund
            );

            model.addAttribute(
                    "refundProcessed",
                    true
            );

            model.addAttribute(
                    "refundMessage",
                    "Refund successfully processed and refund record created."
            );


        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "refundMessage",
                    e.getMessage()
            );

        } catch (Exception e) {

            model.addAttribute(
                    "refundMessage",
                    "An unexpected error occurred while processing the refund."
            );
        }


        return "payment-refund";
    }


    // =========================================================
    // REFUND HISTORY
    // =========================================================

    @GetMapping("/refund-history")
    public String refundHistory(Model model) {

        List<Refund> refunds =
                refundService.getAllRefunds();


        long processedRefundCount =
                refunds.stream()
                        .filter(refund ->
                                "PROCESSED".equals(
                                        refund.getRefundStatus()
                                )
                        )
                        .count();


        double totalRefundAmount =
                refunds.stream()
                        .mapToDouble(
                                Refund::getRefundAmount
                        )
                        .sum();


        model.addAttribute(
                "refunds",
                refunds
        );

        model.addAttribute(
                "processedRefundCount",
                processedRefundCount
        );

        model.addAttribute(
                "totalRefundAmount",
                totalRefundAmount
        );

        return "refund-history";
    }


    // =========================================================
    // E-TICKET PAGE
    // =========================================================

    @GetMapping("/e-ticket")
    public String eTicket(Model model) {

        List<Payment> payments =
                paymentService.getAllPayments();


        if (!payments.isEmpty()) {

            Payment payment =
                    payments.get(
                            payments.size() - 1
                    );

            model.addAttribute(
                    "payment",
                    payment
            );
        }

        return "e-ticket";
    }


    // =========================================================
    // DOWNLOAD E-TICKET
    // =========================================================

    @GetMapping("/download-ticket")
    public ResponseEntity<byte[]> downloadTicket() {

        List<Payment> payments =
                paymentService.getAllPayments();


        if (payments.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }


        Payment payment =
                payments.get(
                        payments.size() - 1
                );


        String ticketHtml =

                "<!DOCTYPE html>" +
                        "<html lang='en'>" +

                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "<title>SriLanka Railways E-Ticket</title>" +

                        "<style>" +

                        "body{" +
                        "font-family:Arial,sans-serif;" +
                        "background:#eef4ff;" +
                        "padding:40px;" +
                        "margin:0;" +
                        "}" +

                        ".ticket{" +
                        "max-width:700px;" +
                        "margin:auto;" +
                        "background:white;" +
                        "padding:40px;" +
                        "border-radius:20px;" +
                        "box-shadow:0 10px 35px rgba(0,0,0,0.12);" +
                        "}" +

                        "h1{" +
                        "text-align:center;" +
                        "color:#173b8f;" +
                        "}" +

                        ".success{" +
                        "text-align:center;" +
                        "color:#16803c;" +
                        "font-size:20px;" +
                        "font-weight:bold;" +
                        "margin:25px;" +
                        "}" +

                        ".row{" +
                        "display:flex;" +
                        "justify-content:space-between;" +
                        "padding:15px 0;" +
                        "border-bottom:1px solid #eee;" +
                        "}" +

                        ".label{" +
                        "color:#777;" +
                        "}" +

                        ".value{" +
                        "font-weight:bold;" +
                        "color:#25345f;" +
                        "}" +

                        ".footer{" +
                        "text-align:center;" +
                        "margin-top:30px;" +
                        "color:#888;" +
                        "font-size:13px;" +
                        "}" +

                        "</style>" +
                        "</head>" +

                        "<body>" +

                        "<div class='ticket'>" +

                        "<h1>🚆 SriLanka Railways</h1>" +

                        "<div class='success'>" +
                        "✓ PAYMENT SUCCESSFUL" +
                        "</div>" +

                        "<div class='row'>" +
                        "<span class='label'>Payment ID</span>" +
                        "<span class='value'>" +
                        payment.getPaymentId() +
                        "</span>" +
                        "</div>" +

                        "<div class='row'>" +
                        "<span class='label'>Booking ID</span>" +
                        "<span class='value'>" +
                        payment.getBookingId() +
                        "</span>" +
                        "</div>" +

                        "<div class='row'>" +
                        "<span class='label'>Amount</span>" +
                        "<span class='value'>Rs. " +
                        String.format(
                                "%.2f",
                                payment.getAmount()
                        ) +
                        "</span>" +
                        "</div>" +

                        "<div class='row'>" +
                        "<span class='label'>Payment Method</span>" +
                        "<span class='value'>" +
                        payment.getPaymentMethod() +
                        "</span>" +
                        "</div>" +

                        "<div class='row'>" +
                        "<span class='label'>Payment Status</span>" +
                        "<span class='value'>" +
                        payment.getStatus() +
                        "</span>" +
                        "</div>" +

                        "<div class='row'>" +
                        "<span class='label'>Payment Date</span>" +
                        "<span class='value'>" +
                        payment.getPaymentDate() +
                        "</span>" +
                        "</div>" +

                        "<div class='footer'>" +
                        "Thank you for using SriLanka Railways." +
                        "<br>" +
                        "Please keep this e-ticket for your records." +
                        "</div>" +

                        "</div>" +

                        "</body>" +
                        "</html>";


        byte[] fileBytes =
                ticketHtml.getBytes(
                        StandardCharsets.UTF_8
                );


        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"SriLanka-Railways-E-Ticket.html\""
                )
                .contentType(
                        MediaType.TEXT_HTML
                )
                .body(fileBytes);
    }


    // =========================================================
    // DELETE PAYMENT PAGE
    // =========================================================

    @GetMapping("/delete-payment")
    public String deletePaymentPage(Model model) {

        model.addAttribute(
                "paymentFound",
                false
        );

        model.addAttribute(
                "deleteConfirmed",
                false
        );

        return "delete-payment";
    }


    // =========================================================
    // SEARCH PAYMENT FOR DELETE
    // =========================================================

    @PostMapping("/delete-search")
    public String searchPaymentForDelete(
            @RequestParam String paymentId,
            Model model) {

        model.addAttribute(
                "paymentFound",
                false
        );

        model.addAttribute(
                "deleteConfirmed",
                false
        );

        model.addAttribute(
                "paymentId",
                paymentId
        );


        if (paymentId == null ||
                paymentId.isBlank()) {

            model.addAttribute(
                    "message",
                    "Please enter a Payment ID."
            );

            return "delete-payment";
        }


        Payment payment =
                paymentService
                        .findByPaymentId(paymentId)
                        .orElse(null);


        if (payment == null) {

            model.addAttribute(
                    "message",
                    "Payment ID not found."
            );

            return "delete-payment";
        }


        model.addAttribute(
                "payment",
                payment
        );

        model.addAttribute(
                "paymentFound",
                true
        );

        return "delete-payment";
    }


    // =========================================================
    // CONFIRM DELETE
    // =========================================================

    @PostMapping("/delete-confirm")
    public String confirmDeletePayment(
            @RequestParam String paymentId,
            Model model) {

        model.addAttribute(
                "paymentFound",
                false
        );

        model.addAttribute(
                "deleteConfirmed",
                false
        );

        model.addAttribute(
                "paymentId",
                paymentId
        );


        Payment payment =
                paymentService
                        .findByPaymentId(paymentId)
                        .orElse(null);


        if (payment == null) {

            model.addAttribute(
                    "message",
                    "Payment ID not found."
            );

            return "delete-payment";
        }


        model.addAttribute(
                "payment",
                payment
        );

        model.addAttribute(
                "paymentFound",
                true
        );

        model.addAttribute(
                "deleteConfirmed",
                true
        );

        model.addAttribute(
                "message",
                "Please confirm that you want to permanently delete this payment record."
        );

        return "delete-payment";
    }


    // =========================================================
    // DELETE PAYMENT
    // =========================================================

    @PostMapping("/delete-payment")
    public String deletePayment(
            @RequestParam String paymentId,
            Model model) {

        try {

            paymentService.deletePayment(
                    paymentId
            );

            model.addAttribute(
                    "paymentFound",
                    false
            );

            model.addAttribute(
                    "deleteConfirmed",
                    false
            );

            model.addAttribute(
                    "message",
                    "Payment " +
                            paymentId +
                            " was successfully deleted."
            );

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "paymentFound",
                    false
            );

            model.addAttribute(
                    "deleteConfirmed",
                    false
            );

            model.addAttribute(
                    "message",
                    e.getMessage()
            );
        }

        return "delete-payment";
    }

}