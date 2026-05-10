package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.dto.PokeApi.EncounterKey;
import com.moeezy.PokeTracker.data.dto.PokeApi.PokemonEncounterProcessingDto;
import com.moeezy.PokeTracker.data.entity.Route;
import com.moeezy.PokeTracker.data.entity.RouteEncounter;
import com.moeezy.PokeTracker.data.repository.RouteEncounterRepository;
import com.moeezy.PokeTracker.data.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PokeApiEncounterService {
    private static final String GAME = "heartgold";
    private static final String TIME = "time";
    private static final String RADIO = "radio";
    private static final String SWARM = "swarm";
    private static final String NOT_APPLICABLE = "NA";
    @Autowired
    WebClient webClient;

    private final RouteEncounterRepository routeEncounterRepository;
    private final RouteRepository routeRepository;

    @Autowired
    PokeApiEncounterService(RouteEncounterRepository routeEncounterRepository, RouteRepository routeRepository){
        this.routeEncounterRepository = routeEncounterRepository;
        this.routeRepository = routeRepository;
    }

    public void allRegionRoutes(int id){
        JsonNode Routes = webClient.get()
                .uri("/region/" + id + "/")
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> Mono.error(new RuntimeException("Routes not found"))
                ).bodyToMono(JsonNode.class).block().path("locations");

        for(JsonNode route: Routes){
            String routeUri = route.path("url").asString();
            String routeUriPath = "https://pokeapi.co/api/v2/location/";
            int startIdx = routeUri.indexOf(routeUriPath);
            String routeId = routeUri.substring(startIdx + routeUriPath.length(), routeUri.length() - 1);
            System.out.println(routeUri);
            routeProcessing(routeId);
        }
    }

    public void routeProcessing(String id){
        JsonNode location =  webClient.get()
                .uri("/location/" + id + "/")
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> Mono.error(new RuntimeException("Location not found"))
                ).bodyToMono(JsonNode.class).block();
        String locationName = location.path("name").asString();

        JsonNode areas = location.path("areas");

        for(JsonNode area : areas){

            String areaUri = area.path("url").asString();
            String areaUriPath = "https://pokeapi.co/api/v2/location-area/";
            int startIdx = areaUri.indexOf(areaUriPath);
            String areaId = areaUri.substring(startIdx + areaUriPath.length(), areaUri.length() - 1);
            String areaName = area.path("name").asString();
            System.out.println("******" + areaUri + "******");
            System.out.println("Attempting Route Insertion: route id:" + areaId + " area id: " + id);
            saveRoute(id, locationName, areaId, areaName);
            areaProcessing(areaId, id);
        }
    }
    //build on this to save to route repo
    public void areaProcessing(String id, String locationId){
        JsonNode area =  webClient.get()
                .uri("/location-area/" + id + "/")
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> Mono.error(new RuntimeException("Location Area not found"))
                ).bodyToMono(JsonNode.class).block();
        String areaId = area.path("id").asString();
        JsonNode encounters = area.path("pokemon_encounters");
        for(JsonNode encounter : encounters){
            JsonNode versions = encounter.path("version_details");
            for(JsonNode version : versions){
                if(version.path("version").path("name").asString().equals(GAME)){
                    String pokemonIdUri = encounter.path("pokemon").path("url").asString();
                    String pokemonIdUriPath = "https://pokeapi.co/api/v2/pokemon/";
                    int startIdx = pokemonIdUri.indexOf(pokemonIdUriPath);
                    String pokemonId = pokemonIdUri.substring(startIdx + pokemonIdUriPath.length(), pokemonIdUri.length() - 1);
                    List<PokemonEncounterProcessingDto> enc = new ArrayList<>();
                    for(JsonNode conds: version.path("encounter_details")) {
                            String time = time(conds.path("condition_values"));
                            String radio = radio(conds.path("condition_values"));
                            String swarm = swarm(conds.path("condition_values"));
                            String method = conds.path("method").path("name").asString();
                            enc.add(new PokemonEncounterProcessingDto(method, time, radio, swarm));
                    }
                    savePokemonEncounters(enc, pokemonId, areaId, locationId);

                }
            }
        }

    }

    private void savePokemonEncounters(List<PokemonEncounterProcessingDto> encounters, String pokemon, String areaId, String locationId){
        List<PokemonEncounterProcessingDto> processed = processPokemonEncounters(encounters);
        System.out.println("area " + areaId + " | location: " + locationId);
        for (PokemonEncounterProcessingDto pokemonData : processed){
            savePokemonEncounter(pokemonData, pokemon, areaId, locationId);
           // System.out.println("Pokemon: " + pokemon + " method: " + pokemonData.getMethod() + " time: " + pokemonData.getTime() + " radio: " + pokemonData.getRadio() + " swarm: " + pokemonData.getSwarm());
        }
    }

    private void savePokemonEncounter(PokemonEncounterProcessingDto encounter, String pokemon, String areaId, String locationId){
        int pokemonId = Integer.parseInt(pokemon);
        int area = Integer.parseInt(areaId);
        int location = Integer.parseInt(locationId);
        String time = encounter.getTime();
        String radio;
        if (encounter.getRadio().equals(NOT_APPLICABLE)){
            radio = null;
        }
        else{
            radio = encounter.getRadio();
        }
        String swarm;
        if (encounter.getSwarm().equals(NOT_APPLICABLE)){
            swarm = null;
        }
        else{
            swarm = encounter.getSwarm();
        }
        String method = encounter.getMethod();

        RouteEncounter routeEncounter = new RouteEncounter();

        routeEncounter.setRouteId(location);
        routeEncounter.setAreaId(area);
        routeEncounter.setPokedexNumber(pokemonId);
        routeEncounter.setTime(time);
        routeEncounter.setRadio(radio);
        routeEncounter.setSwarm(swarm);
        routeEncounter.setMethod(method);

        routeEncounterRepository.save(routeEncounter);
    }

    private void saveRoute(String routeId, String routeName, String areaId, String areaName){
        int areaNum = Integer.parseInt(areaId);
        int routeNum = Integer.parseInt(routeId);

        Route route = new Route();
        route.setRouteId(routeNum);
        route.setAreaId(areaNum);
        route.setRouteName(routeName);
        route.setAreaName(areaName);
        routeRepository.save(route);
    }

    private  List<PokemonEncounterProcessingDto> processPokemonEncounters(List<PokemonEncounterProcessingDto> encounters){
        Map<EncounterKey, PokemonEncounterProcessingDto> encounterMap = new HashMap<>();
        for(PokemonEncounterProcessingDto encounter : encounters){
            EncounterKey key = new EncounterKey(encounter.getMethod(), encounter.getTime());
            encounterMap.merge(
                    key,
                    encounter,
                    (existing, incoming) -> {
                        //good
                        if (incoming.getRadio().equals("NA") && incoming.getSwarm().equals("NA")) {
                            return incoming;
                        }
                        //swarm, no radio
                        else if (incoming.getRadio().equals("NA") && !incoming.getSwarm().equals("NA") && !existing.getRadio().equals("NA") && !existing.getSwarm().equals("NA")) {
                            return incoming;
                        }
                        //radio no swarm
                        else if (!incoming.getRadio().equals("NA") && incoming.getSwarm().equals("NA") && !existing.getRadio().equals("NA") && !existing.getSwarm().equals("NA")) {
                            return incoming;
                        }

                        return existing;
                    }
            );
        }
        return encounterMap.values().stream().toList();
    }

    private String time(JsonNode condition_values) {
        for (JsonNode condition : condition_values) {
            String cond = condition.path("name").asString();
            String con = cond.split("-", 0)[0];
            if (con.equals(TIME)) {
                return cond;
            }
        }
        return "NA";
    }

    private String radio(JsonNode condition_values){
        for(JsonNode condition : condition_values) {
            String cond = condition.path("name").asString();
            String con = cond.split("-", 0)[0];
            if (con.equals(RADIO)) {
                return cond;
            }
        }
        return "NA";
    }

    private String swarm(JsonNode condition_values){
        for(JsonNode condition : condition_values) {
            String cond = condition.path("name").asString();
            String con = cond.split("-", 0)[0];
            if (con.equals(SWARM)) {
                return cond;
            }
        }
        return "NA";
    }

}
