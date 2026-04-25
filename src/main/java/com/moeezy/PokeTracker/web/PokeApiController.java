package com.moeezy.PokeTracker.web;

import com.moeezy.PokeTracker.data.dto.PokeApi.PokemonSpeciesDto;
import com.moeezy.PokeTracker.service.PokeApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequestMapping("/v1/PokeApi")
public class PokeApiController {

    @Autowired
    private PokeApiService pokeApiService;

    @GetMapping("/{id}")
    public Mono<PokemonSpeciesDto> getSpecies(@PathVariable int id) {
        return pokeApiService.retrieveSpeciesData(id);
    }

    @GetMapping("/{id}/type")
    public List<String> getType(@PathVariable int id) {
        return pokeApiService.retrieveTypeData(id);
    }

    @GetMapping("/{id}/all")
    public String savePokemonVoid(@PathVariable int id) {
        pokeApiService.savePokemon(id);
        return "Success?";
    }
}

