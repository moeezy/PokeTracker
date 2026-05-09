package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.dto.PokeApi.EncounterKey;
import com.moeezy.PokeTracker.data.dto.PokeApi.PokemonEncounterProcessingDto;
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
    @Autowired
    WebClient webClient;

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
        JsonNode areas =  webClient.get()
                .uri("/location/" + id + "/")
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> Mono.error(new RuntimeException("Location not found"))
                ).bodyToMono(JsonNode.class).block().path("areas");

        for(JsonNode area : areas){
            String areaUri = area.path("url").asString();
            String areaUriPath = "https://pokeapi.co/api/v2/location-area/";
            int startIdx = areaUri.indexOf(areaUriPath);
            String routeId = areaUri.substring(startIdx + areaUriPath.length(), areaUri.length() - 1);
            System.out.println(areaUri);
            areaProcessing(routeId);
        }
    }
    //build on this to save to route repo
    public void areaProcessing(String id){
        JsonNode area =  webClient.get()
                .uri("/location-area/" + id + "/")
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> Mono.error(new RuntimeException("Location Area not found"))
                ).bodyToMono(JsonNode.class).block();
        String areaName = area.path("name").asString();
        int areaId = area.path("id").asInt();
        JsonNode encounters = area.path("pokemon_encounters");
        for(JsonNode encounter : encounters){
            JsonNode versions = encounter.path("version_details");
            for(JsonNode version : versions){
                if(version.path("version").path("name").asString().equals(GAME)){
                    String pokemonName = encounter.path("pokemon").path("name").asString();
                    List<PokemonEncounterProcessingDto> enc = new ArrayList<>();
                    for(JsonNode conds: version.path("encounter_details")) {
                            String time = time(conds.path("condition_values"));
                            String radio = radio(conds.path("condition_values"));
                            String swarm = swarm(conds.path("condition_values"));
                            String method = conds.path("method").path("name").asString();
                            enc.add(new PokemonEncounterProcessingDto(method, time, radio, swarm));
                    }
                    savePokemonEncounters(enc, pokemonName, areaName, areaId);

                }
            }
        }

    }

    private void savePokemonEncounters(List<PokemonEncounterProcessingDto> encounters, String pokemon, String areaName, int areaId){
        List<PokemonEncounterProcessingDto> processed = processPokemonEncounters(encounters);
        System.out.println("Area: " + areaName + " ID: " + areaId);
        for (PokemonEncounterProcessingDto pokemonData : processed){
            System.out.println("Pokemon: " + pokemon + " method: " + pokemonData.getMethod() + " time: " + pokemonData.getTime() + " radio: " + pokemonData.getRadio() + " swarm: " + pokemonData.getSwarm());
        }
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
