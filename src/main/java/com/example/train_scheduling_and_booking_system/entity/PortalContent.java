package com.example.train_scheduling_and_booking_system.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "portal_content")
public class PortalContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "content_key", length = 100, nullable = false, unique = true)
    private String contentKey;

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Column(name = "content_value", columnDefinition = "TEXT", nullable = false)
    private String contentValue;

    @Column(name = "category", length = 50, nullable = false)
    private String category = "LANDING_PAGE";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false)
    private User updatedBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public PortalContent() {}

    public PortalContent(Long contentId, String contentKey, String title, String contentValue,
                         String category, User updatedBy, LocalDateTime updatedAt) {
        this.contentId = contentId;
        this.contentKey = contentKey;
        this.title = title;
        this.contentValue = contentValue;
        this.category = category != null ? category : "LANDING_PAGE";
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getContentKey() {
        return contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentValue() {
        return contentValue;
    }

    public void setContentValue(String contentValue) {
        this.contentValue = contentValue;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long contentId;
        private String contentKey;
        private String title;
        private String contentValue;
        private String category = "LANDING_PAGE";
        private User updatedBy;
        private LocalDateTime updatedAt;

        public Builder contentId(Long contentId) {
            this.contentId = contentId;
            return this;
        }

        public Builder contentKey(String contentKey) {
            this.contentKey = contentKey;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder contentValue(String contentValue) {
            this.contentValue = contentValue;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder updatedBy(User updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PortalContent build() {
            return new PortalContent(contentId, contentKey, title, contentValue, category, updatedBy, updatedAt);
        }
    }
}
