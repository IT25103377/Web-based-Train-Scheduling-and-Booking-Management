package com.srilankarailways.trainroute.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "route_stations", uniqueConstraints = {
    @UniqueConstraint(name = "uk_route_station", columnNames = {"route_id", "station_id"}),
    @UniqueConstraint(name = "uk_route_stop_order", columnNames = {"route_id", "stop_order"})
})
public class RouteStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(name = "stop_order", nullable = false)
    private Integer stopOrder;

    @Column(nullable = false)
    private Boolean origin = false;

    @Column(nullable = false)
    private Boolean destination = false;

    public RouteStation() {
    }

    public RouteStation(Route route, Station station, Integer stopOrder, Boolean origin, Boolean destination) {
        this.route = route;
        this.station = station;
        this.stopOrder = stopOrder;
        this.origin = origin != null ? origin : false;
        this.destination = destination != null ? destination : false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
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
