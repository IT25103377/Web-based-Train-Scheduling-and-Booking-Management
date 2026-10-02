package com.srilankarailways.trainroute.repository;

import com.srilankarailways.trainroute.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    Optional<Route> findByRouteCode(String routeCode);

    boolean existsByRouteCode(String routeCode);

    boolean existsByRouteCodeAndIdNot(String routeCode, Long id);

    List<Route> findByActiveTrue();

    List<Route> findByActive(Boolean active);
}
