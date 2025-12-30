package com.moeezy.PokeTracker.web;

import com.moeezy.PokeTracker.data.entity.RouteEncounter;
import com.moeezy.PokeTracker.service.RouteEncounterService;
import com.moeezy.PokeTracker.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/RouteEncounter")
public class RouteEncounterController {
    private final RouteEncounterService routeEncounterService;

    @Autowired
    public RouteEncounterController(RouteEncounterService routeEncounterService){
        this.routeEncounterService = routeEncounterService;
    }

    @GetMapping("/{routeId}/{time}")
    public RouteEncounter findAvailableRoutePokemon(@PathVariable int routeId, @PathVariable String time){
        Optional<RouteEncounter> routeEncounter = routeEncounterService.findAvailableRoutePokemon(routeId, time);

        if(routeEncounter.isEmpty()){
            throw new NotFoundException("Route Data not found for route: " + routeId + " and time: " + time);
        }

        return routeEncounter.get();
    }
}
