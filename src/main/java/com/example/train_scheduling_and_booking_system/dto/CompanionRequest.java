package com.example.train_scheduling_and_booking_system.dto;

import com.example.train_scheduling_and_booking_system.entity.ConcessionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CompanionRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    private String fullName;

    @NotBlank(message = "NIC or Passport number is required")
    @Size(max = 30, message = "NIC or Passport number cannot exceed 30 characters")
    private String nicOrPassport;

    @NotNull(message = "Concession type is required")
    private ConcessionType concessionType = ConcessionType.NONE;

    @Size(max = 50, message = "Concession reference cannot exceed 50 characters")
    private String concessionRef;

    public CompanionRequest() {}

    public CompanionRequest(String fullName, String nicOrPassport, ConcessionType concessionType, String concessionRef) {
        this.fullName = fullName;
        this.nicOrPassport = nicOrPassport;
        this.concessionType = concessionType != null ? concessionType : ConcessionType.NONE;
        this.concessionRef = concessionRef;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNicOrPassport() {
        return nicOrPassport;
    }

    public void setNicOrPassport(String nicOrPassport) {
        this.nicOrPassport = nicOrPassport;
    }

    public ConcessionType getConcessionType() {
        return concessionType;
    }

    public void setConcessionType(ConcessionType concessionType) {
        this.concessionType = concessionType;
    }

    public String getConcessionRef() {
        return concessionRef;
    }

    public void setConcessionRef(String concessionRef) {
        this.concessionRef = concessionRef;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String fullName;
        private String nicOrPassport;
        private ConcessionType concessionType = ConcessionType.NONE;
        private String concessionRef;

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder nicOrPassport(String nicOrPassport) {
            this.nicOrPassport = nicOrPassport;
            return this;
        }

        public Builder concessionType(ConcessionType concessionType) {
            this.concessionType = concessionType;
            return this;
        }

        public Builder concessionRef(String concessionRef) {
            this.concessionRef = concessionRef;
            return this;
        }

        public CompanionRequest build() {
            return new CompanionRequest(fullName, nicOrPassport, concessionType, concessionRef);
        }
    }
}
