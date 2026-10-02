package com.trainbooking.trainschedulemanagment.repository;

import com.trainbooking.trainschedulemanagment.model.TrainSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrainScheduleRepository
        extends JpaRepository<TrainSchedule, Integer> {

    // Search by Train Number
    List<TrainSchedule> findByTrain_TrainNumberContainingIgnoreCase(
            String trainNumber
    );

    // Filter by Operating Date
    List<TrainSchedule> findByOperatingDate(
            LocalDate operatingDate
    );
}