package com.srilankarailways.trainroute.dto;

import com.srilankarailways.trainroute.entity.RouteStation;

public class RouteStationResponse {

    private Long stationId;
    private String stationName;
    private String stationCode;
    private Integer stopOrder;
    private Boolean origin;
    private Boolean destination;

    public RouteStationResponse() {
    }

    public RouteStationResponse(Long stationId, String stationName, String stationCode, Integer stopOrder, Boolean origin, Boolean destination) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.stationCode = stationCode;
        this.stopOrder = stopOrder;
        this.origin = origin;
        this.destination = destination;
    }

    public static RouteStationResponse fromEntity(RouteStation rs) {
        if (rs == null || rs.getStation() == null) return null;
        return new RouteStationResponse(
                rs.getStation().getId(),
                rs.getStation().getName(),
                rs.getStation().getCode(),
                rs.getStopOrder(),
                rs.getOrigin(),
                rs.getDestination()
        );
    }

    // Getters and Setters
    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public Integer getStopOrder() {
        return stopOrder;
    }

    public void setStopOrder(Integer stopOrder) {
        this.stopOrder = stopOrder;
    }

    public Boolean getOrigin() {
        return origin;
    }

    public void setOrigin(Boolean origin) {
        this.origin = origin;
    }

    public Boolean getDestination() {
        return destination;
    }

    public void setDestination(Boolean destination) {
        this.destination = destination;
    }
}
