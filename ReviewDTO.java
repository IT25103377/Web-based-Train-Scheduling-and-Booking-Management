package com.train.dto;

public class ReviewDTO {

    private String userName;
    private Integer rating;
    private String reviewText;

    public ReviewDTO() {}

    public ReviewDTO(String userName, Integer rating, String reviewText) {
        this.userName = userName;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
