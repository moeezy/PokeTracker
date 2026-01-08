package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.entity.UserPokemon;
import com.moeezy.PokeTracker.data.repository.UserPokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserPokemonService {

    private final UserPokemonRepository userPokemonRepository;

    @Autowired
    public UserPokemonService(UserPokemonRepository userPokemonRepository){
        this.userPokemonRepository = userPokemonRepository;
    }

    public Optional<UserPokemon> findUserPokemon(long userId, int pokedexNumber){
            return userPokemonRepository.findUserPokemon(userId, pokedexNumber);
    }

    public List<UserPokemon> findUserShinyPokemon(long userId){
            return userPokemonRepository.findUserShinyPokemon(userId);
    }
}
