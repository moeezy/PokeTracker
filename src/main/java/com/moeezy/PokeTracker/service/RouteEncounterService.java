package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.dto.RouteEncounterMap.AreaDto;
import com.moeezy.PokeTracker.data.dto.RouteEncounterMap.EncounterDto;
import com.moeezy.PokeTracker.data.dto.RouteEncounterMap.RouteDto;
import com.moeezy.PokeTracker.data.dto.RouteEncounterMap.RouteEncounterSqlRowProjection;
import com.moeezy.PokeTracker.data.entity.RouteEncounter;
import com.moeezy.PokeTracker.data.repository.RouteEncounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RouteEncounterService {
    private final RouteEncounterRepository routeEncounterRepository;

    @Autowired
    public RouteEncounterService(RouteEncounterRepository routeEncounterRepository){
        this.routeEncounterRepository = routeEncounterRepository;
    }
    //not used tbh
    public List<RouteEncounter> findAvailableRoutePokemon(int routeId, String time){
        return routeEncounterRepository.findAvailableRoutePokemon(routeId, time);
    }
    //change later to filter by time
    public List<RouteDto> findAvailablePokemonMap(int id){
        //hashmap of type routeDto? then iterate through each row, get area? then  get encounter info?
        Map<String, RouteDto> routeMap = new LinkedHashMap<>();
        Map<String, AreaDto> areaMap = new LinkedHashMap<>();

        List <RouteEncounterSqlRowProjection> rows =  routeEncounterRepository.findAllMapPokemon(id);
        for(RouteEncounterSqlRowProjection row : rows){
            if(!routeMap.containsKey(row.getRouteId())){
                RouteDto routeDto = new RouteDto(row.getRouteId(), row.getRouteName(), new ArrayList<>());
                routeMap.put(row.getRouteId(), routeDto);
            }
            RouteDto currentRoute = routeMap.get(row.getRouteId());
            if(!areaMap.containsKey(row.getAreaId())){
                AreaDto areaDto = new AreaDto(row.getAreaId(), row.getAreaName(), new ArrayList<>());
                areaMap.put(row.getAreaId(), areaDto);
                currentRoute.getAreas().add(areaDto);
            }
            AreaDto currentArea = areaMap.get(row.getAreaId());
            EncounterDto encounter = new EncounterDto(row.getPokedexNumber(), row.getName(), row.getCaught(), row.getEncounterMethod(), row.getTimeOfDay(), row.getRadio(), row.getSwarm());
            currentArea.getEncounters().add(encounter);

        }
        return new ArrayList<>(routeMap.values());

    }
}
