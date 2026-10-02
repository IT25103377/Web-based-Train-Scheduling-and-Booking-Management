package com.example.train_scheduling_and_booking_system.repository;

import com.example.train_scheduling_and_booking_system.entity.PortalContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortalContentRepository extends JpaRepository<PortalContent, Long> {

    Optional<PortalContent> findByContentKey(String contentKey);

    List<PortalContent> findAllByCategory(String category);
}
