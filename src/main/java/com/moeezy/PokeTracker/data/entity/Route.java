package com.moeezy.PokeTracker.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="route")
@Data
@ToString
public class Route {

    @Id
    @Column(name="route_id")
    private int routeId;

    @Column(name="route_name")
    private String routeName;
}
