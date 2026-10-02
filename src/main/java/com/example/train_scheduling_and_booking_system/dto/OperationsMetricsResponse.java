package com.example.train_scheduling_and_booking_system.dto;

import java.util.List;

public class OperationsMetricsResponse {

    private int totalTrains;
    private int activeRoutes;
    private double onTimePercentage;
    private double fleetAvailabilityPercentage;
    private int delayedTrainsCount;
    private List<String> activeRoutesSummary;

    public OperationsMetricsResponse() {}

    public OperationsMetricsResponse(int totalTrains, int activeRoutes, double onTimePercentage,
                                     double fleetAvailabilityPercentage, int delayedTrainsCount,
                                     List<String> activeRoutesSummary) {
        this.totalTrains = totalTrains;
        this.activeRoutes = activeRoutes;
        this.onTimePercentage = onTimePercentage;
        this.fleetAvailabilityPercentage = fleetAvailabilityPercentage;
        this.delayedTrainsCount = delayedTrainsCount;
        this.activeRoutesSummary = activeRoutesSummary;
    }

    public int getTotalTrains() { return totalTrains; }
    public void setTotalTrains(int totalTrains) { this.totalTrains = totalTrains; }

    public int getActiveRoutes() { return activeRoutes; }
    public void setActiveRoutes(int activeRoutes) { this.activeRoutes = activeRoutes; }

    public double getOnTimePercentage() { return onTimePercentage; }
    public void setOnTimePercentage(double onTimePercentage) { this.onTimePercentage = onTimePercentage; }

    public double getFleetAvailabilityPercentage() { return fleetAvailabilityPercentage; }
    public void setFleetAvailabilityPercentage(double fleetAvailabilityPercentage) { this.fleetAvailabilityPercentage = fleetAvailabilityPercentage; }

    public int getDelayedTrainsCount() { return delayedTrainsCount; }
    public void setDelayedTrainsCount(int delayedTrainsCount) { this.delayedTrainsCount = delayedTrainsCount; }

    public List<String> getActiveRoutesSummary() { return activeRoutesSummary; }
    public void setActiveRoutesSummary(List<String> activeRoutesSummary) { this.activeRoutesSummary = activeRoutesSummary; }
}
