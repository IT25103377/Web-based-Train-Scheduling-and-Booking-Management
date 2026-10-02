package com.trainbooking.trainschedulemanagment.repository;

import com.trainbooking.trainschedulemanagment.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Integer> {
}