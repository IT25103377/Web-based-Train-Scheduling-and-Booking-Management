package com.train.repository;

import com.train.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    long countByStatus(String status);
    List<Review> findByStatusOrderByCreatedAtDesc(String status);
    List<Review> findAllByOrderByCreatedAtDesc();
}