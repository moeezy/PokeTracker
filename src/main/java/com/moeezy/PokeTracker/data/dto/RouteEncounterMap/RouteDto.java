package com.moeezy.PokeTracker.data.dto.RouteEncounterMap;

import java.util.ArrayList;
import java.util.List;

public class RouteDto {
    String routeId;
    String routeName;
    List<AreaDto> areas = new ArrayList<>();

    public RouteDto(String routeId, String routeName, List<AreaDto> areas) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.areas = areas;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setrouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public List<AreaDto> getAreas() {
        return areas;
    }

    public void setAreas(List<AreaDto> areas) {
        this.areas = areas;
    }
}
