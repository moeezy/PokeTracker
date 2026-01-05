package com.moeezy.PokeTracker.web;

import com.moeezy.PokeTracker.data.entity.RouteEncounter;
import com.moeezy.PokeTracker.service.RouteEncounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/RouteEncounter")
public class RouteEncounterController {
    private final RouteEncounterService routeEncounterService;

    @Autowired
    public RouteEncounterController(RouteEncounterService routeEncounterService){
        this.routeEncounterService = routeEncounterService;
    }

    @GetMapping("/{routeId}/{time}")
    public ResponseEntity<List<RouteEncounter>> findAvailableRoutePokemon(@PathVariable int routeId, @PathVariable String time){
        List<RouteEncounter> routeEncounter = routeEncounterService.findAvailableRoutePokemon(routeId, time);

        if(routeEncounter.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(routeEncounter);
    }
}
