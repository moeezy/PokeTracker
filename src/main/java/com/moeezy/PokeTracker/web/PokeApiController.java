package com.moeezy.PokeTracker.web;

import com.moeezy.PokeTracker.data.dto.PokeApi.PokemonSpeciesDto;
import com.moeezy.PokeTracker.service.PokeApiEncounterService;
import com.moeezy.PokeTracker.service.PokeApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;

import java.util.List;


@RestController
@RequestMapping("/v1/PokeApi")
public class PokeApiController {

    @Autowired
    private PokeApiService pokeApiService;

    @Autowired
    private PokeApiEncounterService pokeApiEncounterService;

    @GetMapping("/{id}")
    public Mono<PokemonSpeciesDto> getSpecies(@PathVariable int id) {
        return pokeApiService.retrieveSpeciesData(id);
    }

    @GetMapping("/{id}/type")
    public List<String> getType(@PathVariable int id) {
        return pokeApiService.retrieveTypeData(id);
    }

    @GetMapping("/{id}/save")
    public String savePokemonVoid(@PathVariable int id) {
        pokeApiService.savePokemon(id);
        return "Success?";
    }

    @GetMapping("/gen4")
    public String savePokemonGen4() {
        pokeApiService.saveAllPokemon();
        return "Success??";
    }

    @GetMapping("/{id}/saveEncounter")
    public List<JsonNode> encounterPokemon(@PathVariable int id) {
        return pokeApiService.routeData(id);
    }

    @GetMapping("/{id}/Routes")
    public String routes(@PathVariable int id) {
        pokeApiEncounterService.allRegionRoutes(id);
        return "Success?????";
    }
}

