package com.moeezy.PokeTracker.web;

import com.moeezy.PokeTracker.data.dto.RouteEncounterMap.RouteDto;
import com.moeezy.PokeTracker.data.entity.RouteEncounter;
import com.moeezy.PokeTracker.security.CurrentUserService;
import com.moeezy.PokeTracker.service.RouteEncounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/v1/RouteEncounter")
public class RouteEncounterController {
    private final RouteEncounterService routeEncounterService;
    private final CurrentUserService currentUserService;

    @Autowired
    public RouteEncounterController(RouteEncounterService routeEncounterService, CurrentUserService currentUserService){
        this.routeEncounterService = routeEncounterService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/{routeId}/{time}")
    public ResponseEntity<List<RouteEncounter>> findAvailableRoutePokemon(@PathVariable int routeId, @PathVariable String time){
        List<RouteEncounter> routeEncounter = routeEncounterService.findAvailableRoutePokemon(routeId, time);

        if(routeEncounter.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(routeEncounter);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<RouteDto>> findAllMapPokemon(@PathVariable int userId){
        currentUserService.verifyIsSelf(userId);
        List<RouteDto> routeEncounter = routeEncounterService.findAvailablePokemonMap(userId);
        return ResponseEntity.ok(routeEncounter);
    }
}
