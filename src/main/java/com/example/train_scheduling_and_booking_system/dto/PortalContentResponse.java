package com.example.train_scheduling_and_booking_system.dto;

import java.time.LocalDateTime;

public class PortalContentResponse {

    private Long contentId;
    private String contentKey;
    private String title;
    private String contentValue;
    private String category;
    private String updatedByUsername;
    private LocalDateTime updatedAt;

    public PortalContentResponse() {}

    public PortalContentResponse(Long contentId, String contentKey, String title, String contentValue,
                                 String category, String updatedByUsername, LocalDateTime updatedAt) {
        this.contentId = contentId;
        this.contentKey = contentKey;
        this.title = title;
        this.contentValue = contentValue;
        this.category = category;
        this.updatedByUsername = updatedByUsername;
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

    public String getUpdatedByUsername() {
        return updatedByUsername;
    }

    public void setUpdatedByUsername(String updatedByUsername) {
        this.updatedByUsername = updatedByUsername;
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
        private String category;
        private String updatedByUsername;
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

        public Builder updatedByUsername(String updatedByUsername) {
            this.updatedByUsername = updatedByUsername;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PortalContentResponse build() {
            return new PortalContentResponse(contentId, contentKey, title, contentValue, category, updatedByUsername, updatedAt);
        }
    }
}
