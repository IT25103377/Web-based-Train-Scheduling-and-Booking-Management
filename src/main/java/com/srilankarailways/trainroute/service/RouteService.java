package com.srilankarailways.trainroute.service;

import com.srilankarailways.trainroute.dto.RouteRequest;
import com.srilankarailways.trainroute.dto.RouteResponse;
import com.srilankarailways.trainroute.dto.RouteStationResponse;
import com.srilankarailways.trainroute.entity.Route;
import com.srilankarailways.trainroute.entity.RouteStation;
import com.srilankarailways.trainroute.entity.Station;
import com.srilankarailways.trainroute.exception.DuplicateResourceException;
import com.srilankarailways.trainroute.exception.InvalidRouteException;
import com.srilankarailways.trainroute.exception.ResourceNotFoundException;
import com.srilankarailways.trainroute.repository.RouteRepository;
import com.srilankarailways.trainroute.repository.RouteStationRepository;
import com.srilankarailways.trainroute.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final StationRepository stationRepository;
    private final RouteStationRepository routeStationRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository,
                        StationRepository stationRepository,
                        RouteStationRepository routeStationRepository) {
        this.routeRepository = routeRepository;
        this.stationRepository = stationRepository;
        this.routeStationRepository = routeStationRepository;
    }

    @Transactional
    public RouteResponse createRoute(RouteRequest request) {
        validateRouteRequest(request, null);

        String formattedCode = request.getRouteCode().toUpperCase().trim();
        Route route = new Route(
                formattedCode,
                request.getRouteName().trim(),
                request.getActive() != null ? request.getActive() : true
        );

        Map<Long, Station> stationMap = fetchAndValidateStations(request.getStationIds());

        List<Long> stationIds = request.getStationIds();
        for (int i = 0; i < stationIds.size(); i++) {
            Long stationId = stationIds.get(i);
            Station station = stationMap.get(stationId);
            int stopOrder = i + 1;
            boolean isOrigin = (i == 0);
            boolean isDestination = (i == stationIds.size() - 1);

            RouteStation rs = new RouteStation(route, station, stopOrder, isOrigin, isDestination);
            route.addRouteStation(rs);
        }

        Route savedRoute = routeRepository.save(route);
        return RouteResponse.fromEntity(savedRoute);
    }

    @Transactional
    public RouteResponse updateRoute(Long id, RouteRequest request) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route with ID " + id + " was not found"));

        validateRouteRequest(request, id);

        Map<Long, Station> stationMap = fetchAndValidateStations(request.getStationIds());

        String formattedCode = request.getRouteCode().toUpperCase().trim();
        route.setRouteCode(formattedCode);
        route.setRouteName(request.getRouteName().trim());
        if (request.getActive() != null) {
            route.setActive(request.getActive());
        }

        // Clear existing route-station associations safely
        route.clearRouteStations();
        routeRepository.saveAndFlush(route);

        List<Long> stationIds = request.getStationIds();
        for (int i = 0; i < stationIds.size(); i++) {
            Long stationId = stationIds.get(i);
            Station station = stationMap.get(stationId);
            int stopOrder = i + 1;
            boolean isOrigin = (i == 0);
            boolean isDestination = (i == stationIds.size() - 1);

            RouteStation rs = new RouteStation(route, station, stopOrder, isOrigin, isDestination);
            route.addRouteStation(rs);
        }

        Route updatedRoute = routeRepository.save(route);
        return RouteResponse.fromEntity(updatedRoute);
    }

    @Transactional(readOnly = true)
    public RouteResponse getRouteById(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route with ID " + id + " was not found"));
        return RouteResponse.fromEntity(route);
    }

    @Transactional(readOnly = true)
    public List<RouteResponse> getAllRoutes(Boolean active) {
        List<Route> routes;
        if (active != null) {
            routes = routeRepository.findByActive(active);
        } else {
            routes = routeRepository.findAll();
        }
        return routes.stream()
                .map(RouteResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RouteResponse> getActiveRoutes() {
        return routeRepository.findByActiveTrue().stream()
                .map(RouteResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RouteStationResponse> getRouteStations(Long id) {
        if (!routeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Route with ID " + id + " was not found");
        }

        return routeStationRepository.findByRouteIdOrderByStopOrderAsc(id).stream()
                .map(RouteStationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public RouteResponse updateRouteStatus(Long id, Boolean active) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route with ID " + id + " was not found"));

        route.setActive(active);
        Route saved = routeRepository.save(route);
        return RouteResponse.fromEntity(saved);
    }

    @Transactional
    public RouteResponse deactivateRoute(Long id) {
        return updateRouteStatus(id, false);
    }

    private void validateRouteRequest(RouteRequest request, Long routeId) {
        if (request.getStationIds() == null || request.getStationIds().size() < 2) {
            throw new InvalidRouteException("A route must contain at least two stations (origin and destination)");
        }

        Set<Long> uniqueStationIds = new HashSet<>(request.getStationIds());
        if (uniqueStationIds.size() < request.getStationIds().size()) {
            throw new InvalidRouteException("Route contains duplicate station IDs");
        }

        String formattedCode = request.getRouteCode() != null ? request.getRouteCode().toUpperCase().trim() : "";
        if (routeId == null) {
            if (routeRepository.existsByRouteCode(formattedCode)) {
                throw new DuplicateResourceException("Route code '" + formattedCode + "' already exists");
            }
        } else {
            if (routeRepository.existsByRouteCodeAndIdNot(formattedCode, routeId)) {
                throw new DuplicateResourceException("Route code '" + formattedCode + "' already exists");
            }
        }
    }

    private Map<Long, Station> fetchAndValidateStations(List<Long> stationIds) {
        List<Station> stations = stationRepository.findAllById(stationIds);
        Map<Long, Station> stationMap = stations.stream()
                .collect(Collectors.toMap(Station::getId, s -> s));

        for (Long id : stationIds) {
            if (!stationMap.containsKey(id)) {
                throw new ResourceNotFoundException("Station with ID " + id + " was not found");
            }
        }

        return stationMap;
    }
}
