package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.entity.UserPokemon;
import com.moeezy.PokeTracker.data.repository.UserPokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPokemonService {

    private final UserPokemonRepository userPokemonRepository;

    @Autowired
    public UserPokemonService(UserPokemonRepository userPokemonRepository){
        this.userPokemonRepository = userPokemonRepository;
    }

    public Optional<UserPokemon> findUserPokemon(long userId, int pokedexNumber){
        try{
            return userPokemonRepository.findUserPokemon(userId, pokedexNumber);
        }catch (Exception ignored){

        }
        return Optional.empty();
    }

    public Optional<UserPokemon> findUserShinyPokemon(long userId){
        try{
            return userPokemonRepository.findUserShinyPokemon(userId);
        } catch (Exception e) {
        }
        return Optional.empty();
    }
}
