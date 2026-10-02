package com.example.train_scheduling_and_booking_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContentUpdateRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title cannot exceed 150 characters")
    private String title;

    @NotBlank(message = "Content value is required")
    private String contentValue;

    @Size(max = 50, message = "Category cannot exceed 50 characters")
    private String category = "LANDING_PAGE";

    public ContentUpdateRequest() {}

    public ContentUpdateRequest(String title, String contentValue, String category) {
        this.title = title;
        this.contentValue = contentValue;
        this.category = category != null ? category : "LANDING_PAGE";
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String contentValue;
        private String category = "LANDING_PAGE";

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

        public ContentUpdateRequest build() {
            return new ContentUpdateRequest(title, contentValue, category);
        }
    }
}
