package com.srilankarailways.trainroute.repository;

import com.srilankarailways.trainroute.entity.RouteStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStationRepository extends JpaRepository<RouteStation, Long> {

    List<RouteStation> findByRouteIdOrderByStopOrderAsc(Long routeId);

    void deleteByRouteId(Long routeId);
}
