package com.example.train_scheduling_and_booking_system.repository;

import com.example.train_scheduling_and_booking_system.entity.BookingPassenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingPassengerRepository extends JpaRepository<BookingPassenger, Long> {

    List<BookingPassenger> findAllByBookingBookingId(Long bookingId);

    List<BookingPassenger> findAllByBookingScheduleScheduleId(Long scheduleId);
}
