package com.srilankarailways.trainroute.config;

import com.srilankarailways.trainroute.dto.RouteRequest;
import com.srilankarailways.trainroute.dto.StationRequest;
import com.srilankarailways.trainroute.dto.StationResponse;
import com.srilankarailways.trainroute.service.RouteService;
import com.srilankarailways.trainroute.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StationService stationService;
    private final RouteService routeService;

    @Autowired
    public DataInitializer(StationService stationService, RouteService routeService) {
        this.stationService = stationService;
        this.routeService = routeService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!stationService.getAllStations(null).isEmpty()) {
            return;
        }

        System.out.println("[SriLanka Railways] Initializing sample station and route data...");

        StationResponse fort = stationService.createStation(new StationRequest("Colombo Fort", "FOT", "Colombo District", true));
        StationResponse ragama = stationService.createStation(new StationRequest("Ragama", "RGM", "Gampaha District", true));
        StationResponse gampaha = stationService.createStation(new StationRequest("Gampaha", "GMP", "Gampaha District", true));
        StationResponse veyangoda = stationService.createStation(new StationRequest("Veyangoda", "VGD", "Gampaha District", true));
        StationResponse polgahawela = stationService.createStation(new StationRequest("Polgahawela", "PLW", "Kurunegala District", true));
        StationResponse kandy = stationService.createStation(new StationRequest("Kandy", "KDT", "Kandy District", true));
        StationResponse galle = stationService.createStation(new StationRequest("Galle", "GLE", "Galle District", true));
        StationResponse matara = stationService.createStation(new StationRequest("Matara", "MTR", "Matara District", true));
        StationResponse jaffna = stationService.createStation(new StationRequest("Jaffna", "JFN", "Jaffna District", true));

        // Create Main Line Route: Colombo Fort -> Ragama -> Gampaha -> Veyangoda -> Polgahawela -> Kandy
        List<Long> mainLineStationIds = Arrays.asList(
                fort.getId(),
                ragama.getId(),
                gampaha.getId(),
                veyangoda.getId(),
                polgahawela.getId(),
                kandy.getId()
        );
        routeService.createRoute(new RouteRequest("R001", "Colombo Fort - Kandy Main Line", mainLineStationIds, true));

        // Create Coastal Line Route: Colombo Fort -> Galle -> Matara
        List<Long> coastalStationIds = Arrays.asList(
                fort.getId(),
                galle.getId(),
                matara.getId()
        );
        routeService.createRoute(new RouteRequest("R002", "Colombo Fort - Matara Coastal Express", coastalStationIds, true));

        // Create Northern Line Route: Colombo Fort -> Polgahawela -> Jaffna
        List<Long> northernStationIds = Arrays.asList(
                fort.getId(),
                polgahawela.getId(),
                jaffna.getId()
        );
        routeService.createRoute(new RouteRequest("R003", "Yal Devi Northern Line (Colombo - Jaffna)", northernStationIds, true));

        System.out.println("[SriLanka Railways] Sample data initialized successfully!");
    }
}
