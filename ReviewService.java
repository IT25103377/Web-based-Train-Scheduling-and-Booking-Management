package com.train.service;

import com.train.dto.ReviewDTO;
import com.train.model.Review;
import com.train.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Fetch all reviews for admin moderation
    public List<Review> getAllReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc();
    }

    // Fetch only verified/approved reviews for passenger feed
    public List<Review> getApprovedReviews() {
        return reviewRepository.findByStatusOrderByCreatedAtDesc("Approved");
    }

    // Create a new review from DTO (Business Rule: defaults to Pending)
    public void createReviewFromDTO(ReviewDTO dto) {
        Review review = new Review();
        review.setUserName(dto.getUserName());
        review.setRating(dto.getRating());
        review.setReviewText(dto.getReviewText());
        review.setStatus("Pending");
        reviewRepository.save(review);
    }

    // Direct entity save (fallback)
    public void saveReview(Review review) {
        if (review.getStatus() == null || review.getStatus().isEmpty()) {
            review.setStatus("Pending");
        }
        reviewRepository.save(review);
    }

    // Update review moderation status (Approved / Rejected)
    public void updateStatus(Integer id, String status) {
        reviewRepository.findById(id).ifPresent(review -> {
            review.setStatus(status);
            reviewRepository.save(review);
        });
    }

    // Add admin response to passenger feedback
    public void addAdminReply(Integer id, String reply) {
        reviewRepository.findById(id).ifPresent(review -> {
            review.setAdminReply(reply);
            reviewRepository.save(review);
        });
    }

    // Delete a review record
    public void deleteReview(Integer id) {
        reviewRepository.deleteById(id);
    }

    // Dashboard metrics aggregation
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Real dynamic database metrics
        stats.put("totalReviews", reviewRepository.count());
        stats.put("pendingReviews", reviewRepository.countByStatus("Pending"));
        stats.put("approvedReviews", reviewRepository.countByStatus("Approved"));

        // System overview mock figures (to be linked with teammate entities later)
        stats.put("registeredUsers", 48);
        stats.put("activeTrains", 12);
        stats.put("totalRoutes", 8);
        stats.put("scheduledTrips", 24);

        return stats;
    }
}