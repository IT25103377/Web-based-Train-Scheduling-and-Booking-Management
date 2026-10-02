package com.train.controller;

import com.train.dto.ReviewDTO;
import com.train.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // 1. Admin Dashboard Overview
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", reviewService.getDashboardStats());
        return "dashboard";
    }

    // 2. Admin Review Moderation Table (READ)
    @GetMapping("/reviews")
    public String viewReviews(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());
        return "reviews";
    }

    // 3. Customer-Facing Review Page (CREATE & READ APPROVED)
    @GetMapping("/customer/reviews")
    public String customerReviewPage(Model model) {
        model.addAttribute("review", new ReviewDTO());
        model.addAttribute("approvedReviews", reviewService.getApprovedReviews());
        return "customer-review";
    }

    // 4. Save Submitted Feedback via DTO (CREATE)
    @PostMapping("/saveReview")
    public String saveReview(@ModelAttribute("review") ReviewDTO reviewDTO) {
        reviewService.createReviewFromDTO(reviewDTO);
        return "redirect:/customer/reviews";
    }

    // 5. Update Status Endpoint (UPDATE)
    @PostMapping("/reviews/{id}/status")
    public String updateStatus(@PathVariable Integer id, @RequestParam String status) {
        reviewService.updateStatus(id, status);
        return "redirect:/reviews";
    }

    // 6. Admin Reply Endpoint (UPDATE)
    @PostMapping("/reviews/{id}/reply")
    public String replyReview(@PathVariable Integer id, @RequestParam String reply) {
        reviewService.addAdminReply(id, reply);
        return "redirect:/reviews";
    }

    // 7. Delete Review Endpoint (DELETE)
    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
        return "redirect:/reviews";
    }
}