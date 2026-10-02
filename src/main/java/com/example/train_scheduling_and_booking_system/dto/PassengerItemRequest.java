package com.example.train_scheduling_and_booking_system.dto;

import jakarta.validation.constraints.NotBlank;

public class PassengerItemRequest {

    @NotBlank(message = "Passenger full name is required")
    private String fullName;

    @NotBlank(message = "NIC or Passport is required")
    private String nicOrPassport;

    private String concessionType = "NONE";

    public PassengerItemRequest() {}

    public PassengerItemRequest(String fullName, String nicOrPassport, String concessionType) {
        this.fullName = fullName;
        this.nicOrPassport = nicOrPassport;
        this.concessionType = concessionType != null ? concessionType : "NONE";
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getNicOrPassport() { return nicOrPassport; }
    public void setNicOrPassport(String nicOrPassport) { this.nicOrPassport = nicOrPassport; }

    public String getConcessionType() { return concessionType; }
    public void setConcessionType(String concessionType) { this.concessionType = concessionType; }
}
