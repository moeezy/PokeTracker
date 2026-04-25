package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.dto.PokeApi.PokemonSpeciesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@Service
public class PokeApiService {

    @Autowired
    WebClient webClient;

    public Mono<PokemonSpeciesDto> retrieveSpeciesData(int id){

        Mono<PokemonSpeciesDto> speciesData = webClient
                .get()
                .uri("/pokemon-species/" + id)
                .retrieve()
                .onStatus(                          // handle errors gracefully
                        status -> status.is4xxClientError(),
                        response -> Mono.error(new RuntimeException("Pokemon not found"))
                )
                .bodyToMono(PokemonSpeciesDto.class);

        return speciesData;
    }

    public List<String> retrieveTypeData(int id){
        JsonNode jsonNode = webClient
                .get()
                .uri("/pokemon/" + id)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> Mono.error(new RuntimeException("Pokemon not found"))
                )
                .bodyToMono(JsonNode.class).block();

        ArrayList<String> typeData = new ArrayList<String>();

        JsonNode types = jsonNode.path("types");

        for(JsonNode type : types){
            typeData.add(type.path("type")
                    .path("name")
                    .asString());
        }
        //^how to iterate over nested fields

        return typeData;
    }
}
