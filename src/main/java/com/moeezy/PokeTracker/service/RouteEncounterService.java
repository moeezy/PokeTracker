package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.entity.RouteEncounter;
import com.moeezy.PokeTracker.data.repository.RouteEncounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RouteEncounterService {
    private final RouteEncounterRepository routeEncounterRepository;

    @Autowired
    public RouteEncounterService(RouteEncounterRepository routeEncounterRepository){
        this.routeEncounterRepository = routeEncounterRepository;
    }

    public Optional<RouteEncounter> findAvailableRoutePokemon(int routeId, String time){
        try{
            return routeEncounterRepository.findAvailableRoutePokemon(routeId, time);
        } catch (Exception e) {
        }
        return Optional.empty();
    }
}
