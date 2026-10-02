package com.example.train_scheduling_and_booking_system.repository;

import com.example.train_scheduling_and_booking_system.entity.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {

    List<SupportTicket> findAllByStatusOrderByCreatedAtDesc(String status);

    List<SupportTicket> findAllByUserUserIdOrderByCreatedAtDesc(Long userId);

    Optional<SupportTicket> findByTicketNumber(String ticketNumber);

    List<SupportTicket> findAllByOrderByCreatedAtDesc();

    long countByStatus(String status);
}
