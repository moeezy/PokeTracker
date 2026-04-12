package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.dto.PokeApi.PokemonSpeciesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PokeApiService {

    @Autowired
    WebClient webClient;

    public Mono<PokemonSpeciesDto> retrieve(int id){

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
}
