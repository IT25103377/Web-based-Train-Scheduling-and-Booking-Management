package com.srilankarailways.trainroute.service;

import com.srilankarailways.trainroute.dto.StationRequest;
import com.srilankarailways.trainroute.dto.StationResponse;
import com.srilankarailways.trainroute.entity.Station;
import com.srilankarailways.trainroute.exception.DuplicateResourceException;
import com.srilankarailways.trainroute.exception.ResourceNotFoundException;
import com.srilankarailways.trainroute.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationRepository stationRepository;

    @Autowired
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse createStation(StationRequest request) {
        String formattedCode = request.getCode() != null ? request.getCode().toUpperCase().trim() : "";
        if (stationRepository.existsByCode(formattedCode)) {
            throw new DuplicateResourceException("Station code '" + formattedCode + "' already exists");
        }

        Station station = new Station(
                request.getName().trim(),
                formattedCode,
                request.getLocation().trim(),
                request.getActive() != null ? request.getActive() : true
        );

        Station saved = stationRepository.save(station);
        return StationResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> getAllStations(Boolean active) {
        List<Station> stations;
        if (active != null) {
            stations = stationRepository.findByActive(active);
        } else {
            stations = stationRepository.findAll();
        }
        return stations.stream()
                .map(StationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StationResponse getStationById(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station with ID " + id + " was not found"));
        return StationResponse.fromEntity(station);
    }

    @Transactional
    public StationResponse updateStation(Long id, StationRequest request) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station with ID " + id + " was not found"));

        String formattedCode = request.getCode() != null ? request.getCode().toUpperCase().trim() : "";
        if (stationRepository.existsByCodeAndIdNot(formattedCode, id)) {
            throw new DuplicateResourceException("Station code '" + formattedCode + "' already exists");
        }

        station.setName(request.getName().trim());
        station.setCode(formattedCode);
        station.setLocation(request.getLocation().trim());
        if (request.getActive() != null) {
            station.setActive(request.getActive());
        }

        Station updated = stationRepository.save(station);
        return StationResponse.fromEntity(updated);
    }

    @Transactional
    public StationResponse deactivateStation(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station with ID " + id + " was not found"));

        station.setActive(false);
        Station saved = stationRepository.save(station);
        return StationResponse.fromEntity(saved);
    }
}
