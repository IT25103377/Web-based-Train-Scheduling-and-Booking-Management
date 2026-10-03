package com.example.train_scheduling_and_booking_system.repository;

import com.example.train_scheduling_and_booking_system.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByUserUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Booking> findByBookingReference(String bookingReference);

    List<Booking> findAllByScheduleScheduleId(Long scheduleId);

    List<Booking> findAllByOrderByCreatedAtDesc();

    long countByStatus(String status);
}
