package com.moeezy.PokeTracker.web;

import com.moeezy.PokeTracker.data.entity.UserPokemon;
import com.moeezy.PokeTracker.service.UserPokemonService;
import com.moeezy.PokeTracker.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/userPokemon")
public class UserPokemonController {

    private final UserPokemonService userPokemonService;

    @Autowired
    public UserPokemonController(UserPokemonService userPokemonService){
        this.userPokemonService = userPokemonService;
    }

    @GetMapping("/{userId}/{pokedexNumber}")
    public UserPokemon findUserPokemon(@PathVariable long userId, @PathVariable int pokedexNumber){
        Optional<UserPokemon> userPokemon = this.userPokemonService.findUserPokemon(userId, pokedexNumber);

        if(userPokemon.isEmpty()){
            throw new NotFoundException("UserPokemon not found with id: " + userId + "and pokedex Number: " + pokedexNumber);
        }
        return userPokemon.get();
    }
}
