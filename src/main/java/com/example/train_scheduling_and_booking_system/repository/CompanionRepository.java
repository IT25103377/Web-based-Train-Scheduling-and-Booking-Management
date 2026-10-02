package com.example.train_scheduling_and_booking_system.repository;

import com.example.train_scheduling_and_booking_system.entity.Companion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanionRepository extends JpaRepository<Companion, Long> {

    List<Companion> findAllByUserUserId(Long userId);

    Optional<Companion> findByCompanionIdAndUserUserId(Long companionId, Long userId);
}
