package com.example.train_scheduling_and_booking_system.dto;

import jakarta.validation.constraints.NotBlank;

public class TicketResolutionRequest {

    @NotBlank(message = "Status is required (APPROVED, REJECTED, RESOLVED)")
    private String status;

    private String resolutionNotes;

    public TicketResolutionRequest() {}

    public TicketResolutionRequest(String status, String resolutionNotes) {
        this.status = status;
        this.resolutionNotes = resolutionNotes;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }
}
