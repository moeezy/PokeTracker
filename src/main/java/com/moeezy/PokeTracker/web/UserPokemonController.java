package com.moeezy.PokeTracker.web;

import com.moeezy.PokeTracker.data.dto.UpsertUserPokemonDTO;
import com.moeezy.PokeTracker.data.dto.UserPokemonDTO;
import com.moeezy.PokeTracker.data.entity.UserPokemon;
import com.moeezy.PokeTracker.service.UserPokemonService;
import com.moeezy.PokeTracker.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/v1/userPokemon")
public class UserPokemonController {

    private final UserPokemonService userPokemonService;

    @Autowired
    public UserPokemonController(UserPokemonService userPokemonService){
        this.userPokemonService = userPokemonService;
    }

    @GetMapping("/{userId}/{pokedexNumber}")
    public UserPokemon findUserIndividualPokemon(@PathVariable long userId, @PathVariable int pokedexNumber){
        Optional<UserPokemon> userPokemon = this.userPokemonService.findUserIndividualPokemon(userId, pokedexNumber);

        if(userPokemon.isEmpty()){
            throw new NotFoundException("UserPokemon not found with id: " + userId + "and pokedex Number: " + pokedexNumber);
        }
        return userPokemon.get();
    }

    @GetMapping("/{userId}/shiny")
    public ResponseEntity<List<UserPokemon>> findUserShinyPokemon(@PathVariable long userId){
        List<UserPokemon> userPokemon = this.userPokemonService.findUserShinyPokemon(userId);

        if(userPokemon.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userPokemon);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserPokemonDTO>> findUserPokemon(@PathVariable long userId){
        List<UserPokemonDTO> userPokemon = this.userPokemonService.findUserPokemon(userId);

        if(userPokemon.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userPokemon);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Void> updateUserPokemon(
            @PathVariable long userId,
            @RequestBody @Validated UpsertUserPokemonDTO updatedPokemon){
        userPokemonService.upsertUserPokemon(userId, updatedPokemon);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
