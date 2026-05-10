package com.moeezy.PokeTracker.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@IdClass( RouteEncounterId.class )
@Table(name="route_encounters")
@Data
@ToString
public class RouteEncounter {
    @Id
    @Column(name="route_id")
    private int routeId;

    @Id
    @Column(name="area_id")
    private int areaId;

    @Id
    @Column(name="pokedex_number")
    private int pokedexNumber;

    @Id
    @Column(name="time_of_day")
    private String time;

    @Column(name="radio")
    private String radio;

    @Column(name="swarm")
    private String swarm;

    @Id
    @Column(name="encounter_method")
    private String method;
}
