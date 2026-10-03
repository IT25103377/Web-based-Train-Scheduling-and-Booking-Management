package com.example.train_scheduling_and_booking_system.dto;

import com.example.train_scheduling_and_booking_system.entity.ConcessionType;

import java.time.LocalDateTime;

public class CompanionResponse {

    private Long companionId;
    private Long userId;
    private String fullName;
    private String nicOrPassport;
    private ConcessionType concessionType;
    private String concessionRef;
    private LocalDateTime createdAt;

    public CompanionResponse() {}

    public CompanionResponse(Long companionId, Long userId, String fullName, String nicOrPassport,
                             ConcessionType concessionType, String concessionRef, LocalDateTime createdAt) {
        this.companionId = companionId;
        this.userId = userId;
        this.fullName = fullName;
        this.nicOrPassport = nicOrPassport;
        this.concessionType = concessionType;
        this.concessionRef = concessionRef;
        this.createdAt = createdAt;
    }

    public Long getCompanionId() {
        return companionId;
    }

    public void setCompanionId(Long companionId) {
        this.companionId = companionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long companionId;
        private Long userId;
        private String fullName;
        private String nicOrPassport;
        private ConcessionType concessionType;
        private String concessionRef;
        private LocalDateTime createdAt;

        public Builder companionId(Long companionId) {
            this.companionId = companionId;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

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

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CompanionResponse build() {
            return new CompanionResponse(companionId, userId, fullName, nicOrPassport, concessionType, concessionRef, createdAt);
        }
    }
}
