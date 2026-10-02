package com.example.train_scheduling_and_booking_system.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "companions")
public class Companion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "companion_id")
    private Long companionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "nic_or_passport", length = 30, nullable = false)
    private String nicOrPassport;

    @Enumerated(EnumType.STRING)
    @Column(name = "concession_type", length = 50, nullable = false)
    private ConcessionType concessionType = ConcessionType.NONE;

    @Column(name = "concession_ref", length = 50)
    private String concessionRef;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Companion() {}

    public Companion(Long companionId, User user, String fullName, String nicOrPassport,
                     ConcessionType concessionType, String concessionRef, LocalDateTime createdAt) {
        this.companionId = companionId;
        this.user = user;
        this.fullName = fullName;
        this.nicOrPassport = nicOrPassport;
        this.concessionType = concessionType != null ? concessionType : ConcessionType.NONE;
        this.concessionRef = concessionRef;
        this.createdAt = createdAt;
    }

    public Long getCompanionId() {
        return companionId;
    }

    public void setCompanionId(Long companionId) {
        this.companionId = companionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        private User user;
        private String fullName;
        private String nicOrPassport;
        private ConcessionType concessionType = ConcessionType.NONE;
        private String concessionRef;
        private LocalDateTime createdAt;

        public Builder companionId(Long companionId) {
            this.companionId = companionId;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
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

        public Companion build() {
            return new Companion(companionId, user, fullName, nicOrPassport, concessionType, concessionRef, createdAt);
        }
    }
}
