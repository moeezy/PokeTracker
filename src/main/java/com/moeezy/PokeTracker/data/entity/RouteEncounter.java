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
    @Column(name="pokedex_number")
    private int pokedexNumber;

    @Id
    @Column(name="time_of_day")
    private String time;

    @Id
    @Column(name="encounter_method")
    private String method;
}
