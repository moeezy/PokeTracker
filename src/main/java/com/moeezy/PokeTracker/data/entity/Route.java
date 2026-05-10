package com.moeezy.PokeTracker.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@IdClass( RouteId.class )
@Table(name="routes")
@Data
@ToString
public class Route {

    @Id
    @Column(name="route_id")
    private int routeId;

    @Column(name="route_name")
    private String routeName;

    @Id
    @Column(name="area_id")
    private int areaId;

    @Column(name="area_name")
    private String areaName;
}
