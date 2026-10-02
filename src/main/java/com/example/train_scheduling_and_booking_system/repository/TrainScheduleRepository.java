package com.example.train_scheduling_and_booking_system.repository;

import com.example.train_scheduling_and_booking_system.entity.TrainSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainScheduleRepository extends JpaRepository<TrainSchedule, Long> {

    List<TrainSchedule> findByOriginStationIgnoreCaseAndDestinationStationIgnoreCase(String origin, String destination);

    List<TrainSchedule> findByOriginStationIgnoreCase(String origin);

    List<TrainSchedule> findByOriginStationIgnoreCaseOrDestinationStationIgnoreCase(String origin, String destination);

    List<TrainSchedule> findByOriginStationIgnoreCaseAndDestinationStationIgnoreCaseAndTravelDate(String origin, String destination, String travelDate);

    Optional<TrainSchedule> findByTrainNumber(String trainNumber);

    List<TrainSchedule> findAllByOrderByDepartureTimeAsc();

    long countByStatus(String status);
}
