package com.srilankarailways.trainroute.repository;

import com.srilankarailways.trainroute.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    Optional<Station> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    List<Station> findByActiveTrue();

    List<Station> findByActive(Boolean active);
}
