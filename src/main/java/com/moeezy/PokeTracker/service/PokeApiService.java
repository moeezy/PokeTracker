package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.dto.PokeApi.PokemonSpeciesDto;
import com.moeezy.PokeTracker.data.repository.PokemonRepository;
import com.moeezy.PokeTracker.web.exception.PokemonInsertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@Service
public class PokeApiService {

    private static final int SINNOH = 493;
    private static final String GAME = "heartgold";
    private static final int GENDERLESS = 0;
    private static final int ONLY_ONE_GENDER = 8;

    @Autowired
    WebClient webClient;

    private final PokemonRepository pokemonRepository;

    @Autowired PokeApiService(PokemonRepository pokemonRepository){
        this.pokemonRepository = pokemonRepository;
    }

    public void saveAllPokemon(){
        for(int i = 1; i <= SINNOH; i++){
            try{
                savePokemon(i);
            } catch (PokemonInsertException e) {
                throw new PokemonInsertException("Upsert failed for Pokemon: " + i + " " + e.getMessage());
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
            pokemonRepository.insertPokemon(id, name, primaryType, secondaryType, hasGender);
        } catch (DataIntegrityViolationException e) {
            throw new PokemonInsertException("Insert failed for Pokemon: " + id + " Exception: " + e.getMessage());
        }
        System.out.println("Upserted " + name + " " + primaryType + " " + secondaryType + " gendered: " + hasGender);

    }

    private boolean hasGender(int genderRate){
        return genderRate > GENDERLESS && genderRate < ONLY_ONE_GENDER;
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

    //to be deprecated
    public List<JsonNode> routeData(int id){
        List<JsonNode> encounterData= new ArrayList<JsonNode>();
        JsonNode jsonNode = webClient
                .get()
                .uri("/pokemon/" + id + "/encounters")
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> Mono.error(new RuntimeException("Pokemon Encounters not found"))
                )
                .bodyToMono(JsonNode.class).block();

        for(JsonNode encounters : jsonNode){
            JsonNode encountersForLocation = encounters.path("version_details");
            for(JsonNode versions : encountersForLocation){
                if(versions.path("version").path("name").asString().equals(GAME)){
                    System.out.println(versions.path("location_area"));
                    System.out.println(versions.path("encounter_details"));
                    encounterData.add(encounters.path("location_area"));
                    encounterData.add(versions.path("encounter_details"));
                }
            }
        }
        return encounterData;
    }


}
