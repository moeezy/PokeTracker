package com.moeezy.PokeTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;

@Service
public class PokeApiEncounterService {
    private static final String GAME = "heartgold";
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
        JsonNode encounters =  webClient.get()
                .uri("/location-area/" + id + "/")
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> Mono.error(new RuntimeException("Location Area not found"))
                ).bodyToMono(JsonNode.class).block().path("pokemon_encounters");
        for(JsonNode encounter : encounters){
            JsonNode versions = encounter.path("version_details");
            for(JsonNode version : versions){
                if(version.path("version").path("name").asString().equals(GAME)){
                    System.out.println(encounter.path("pokemon").path("name").asString());
                    System.out.println(version.path("encounter_details").toString());
                }
            }
        }

    }

}
