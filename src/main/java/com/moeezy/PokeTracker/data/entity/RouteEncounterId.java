package com.moeezy.PokeTracker.data.entity;

//implement no arg constructor and equals & hashable methods
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
public class RouteEncounterId implements Serializable {
    private int routeId;
    private int areaId;
    private int pokedexNumber;
    private String time;
    private String method;

    public RouteEncounterId(int routeId, int areaId, int pokedexNumber, String time, String method){
        this.routeId = routeId;
        this.areaId = areaId;
        this.pokedexNumber = pokedexNumber;
        this.time = time;
        this.method = method;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof RouteEncounterId routeEncounterId)) return false;
        return routeId == routeEncounterId.routeId && areaId == routeEncounterId.areaId && pokedexNumber == routeEncounterId.pokedexNumber && Objects.equals(time, routeEncounterId.time) && Objects.equals(method, routeEncounterId.method);
    }

    @Override
    public int hashCode(){
        return Objects.hash(routeId, areaId, pokedexNumber, time, method);
    }
}
