package com.moeezy.PokeTracker.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
public class RouteId implements Serializable {
    private int routeId;
    private int areaId;

    public RouteId(int routeId, int areaId) {
        this.routeId = routeId;
        this.areaId = areaId;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof RouteId routeId)) return false;
        return this.routeId == routeId.routeId &&  areaId == routeId.areaId;
    }

    @Override
    public int hashCode(){ return Objects.hash(routeId, areaId);}
}
