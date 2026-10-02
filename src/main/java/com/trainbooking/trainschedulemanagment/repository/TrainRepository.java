package com.trainbooking.trainschedulemanagment.repository;

import com.trainbooking.trainschedulemanagment.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainRepository extends JpaRepository<Train, Integer> {
}