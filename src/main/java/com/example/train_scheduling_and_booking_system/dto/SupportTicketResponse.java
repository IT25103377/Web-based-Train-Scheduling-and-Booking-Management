package com.example.train_scheduling_and_booking_system.dto;

import java.time.LocalDateTime;

public class SupportTicketResponse {

    private Long ticketId;
    private String ticketNumber;
    private String username;
    private String userFullName;
    private Long bookingId;
    private String bookingReference;
    private String category;
    private String subject;
    private String description;
    private String status;
    private String resolutionNotes;
    private String resolvedByUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SupportTicketResponse() {}

    public SupportTicketResponse(Long ticketId, String ticketNumber, String username, String userFullName,
                                 Long bookingId, String bookingReference, String category, String subject,
                                 String description, String status, String resolutionNotes,
                                 String resolvedByUsername, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.ticketId = ticketId;
        this.ticketNumber = ticketNumber;
        this.username = username;
        this.userFullName = userFullName;
        this.bookingId = bookingId;
        this.bookingReference = bookingReference;
        this.category = category;
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.resolutionNotes = resolutionNotes;
        this.resolvedByUsername = resolvedByUsername;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public String getTicketNumber() { return ticketNumber; }
    public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public String getResolvedByUsername() { return resolvedByUsername; }
    public void setResolvedByUsername(String resolvedByUsername) { this.resolvedByUsername = resolvedByUsername; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long ticketId;
        private String ticketNumber;
        private String username;
        private String userFullName;
        private Long bookingId;
        private String bookingReference;
        private String category;
        private String subject;
        private String description;
        private String status;
        private String resolutionNotes;
        private String resolvedByUsername;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder ticketId(Long ticketId) { this.ticketId = ticketId; return this; }
        public Builder ticketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder userFullName(String userFullName) { this.userFullName = userFullName; return this; }
        public Builder bookingId(Long bookingId) { this.bookingId = bookingId; return this; }
        public Builder bookingReference(String bookingReference) { this.bookingReference = bookingReference; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder subject(String subject) { this.subject = subject; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder resolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; return this; }
        public Builder resolvedByUsername(String resolvedByUsername) { this.resolvedByUsername = resolvedByUsername; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public SupportTicketResponse build() {
            return new SupportTicketResponse(ticketId, ticketNumber, username, userFullName,
                    bookingId, bookingReference, category, subject, description, status,
                    resolutionNotes, resolvedByUsername, createdAt, updatedAt);
        }
    }
}
