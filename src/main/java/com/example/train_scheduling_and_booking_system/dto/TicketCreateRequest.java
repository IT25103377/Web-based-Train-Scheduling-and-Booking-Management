package com.example.train_scheduling_and_booking_system.dto;

import jakarta.validation.constraints.NotBlank;

public class TicketCreateRequest {

    private Long bookingId;

    @NotBlank(message = "Category is required (REFUND, QUERY, COMPLAINT)")
    private String category;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Description is required")
    private String description;

    public TicketCreateRequest() {}

    public TicketCreateRequest(Long bookingId, String category, String subject, String description) {
        this.bookingId = bookingId;
        this.category = category;
        this.subject = subject;
        this.description = description;
    }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
