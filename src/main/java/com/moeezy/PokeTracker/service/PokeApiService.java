package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.dto.PokeApi.PokemonSpeciesDto;
import com.moeezy.PokeTracker.data.repository.PokemonRepository;
import com.moeezy.PokeTracker.web.exception.PokemonUpsertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@Service
public class PokeApiService {

    private static final int sinnoh = 493;

    @Autowired
    WebClient webClient;

    private final PokemonRepository pokemonRepository;

    @Autowired PokeApiService(PokemonRepository pokemonRepository){
        this.pokemonRepository = pokemonRepository;
    }

    public void saveAllPokemon(){
        for(int i = 1; i <= sinnoh; i++){
            try{
                savePokemon(i);
            } catch (PokemonUpsertException e) {
                throw new PokemonUpsertException("Upsert failed for Pokemon: " + i + " " + e.getMessage());
            }
        }
    }

    public void savePokemon(int id){
        Mono<PokemonSpeciesDto> species = retrieveSpeciesData(id);
        List<String> types = retrieveTypeData(id);

        int genderRate = species.block().getGender_rate();
        boolean hasGender = hasGender(genderRate);
        String name = species.block().getName();
        String primaryType = types.get(0);
        String secondaryType;
        if(types.size() == 2){
            secondaryType = types.get(1);
        }
        else{
            secondaryType = null;
        }
        try {
            pokemonRepository.upsertPokemon(id, name, primaryType, secondaryType, hasGender);
        } catch (PokemonUpsertException e) {
            throw new PokemonUpsertException("Upsert failed for Pokemon: " + id + " " + e.getMessage());
        }
        System.out.println("Upserted " + name + " " + primaryType + " " + secondaryType + " gendered: " + hasGender);

    }

    private boolean hasGender(int genderRate){
        return genderRate > 0 && genderRate < 8;
    }

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
