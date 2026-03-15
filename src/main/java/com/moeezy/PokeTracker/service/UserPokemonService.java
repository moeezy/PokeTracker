package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.dto.UpsertUserPokemonDTO;
import com.moeezy.PokeTracker.data.dto.UserPokemonDTO;
import com.moeezy.PokeTracker.data.entity.UserPokemon;
import com.moeezy.PokeTracker.data.repository.UserPokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserPokemonService {

    private final UserPokemonRepository userPokemonRepository;

    @Autowired
    public UserPokemonService(UserPokemonRepository userPokemonRepository){
        this.userPokemonRepository = userPokemonRepository;
    }

    public Optional<UserPokemon> findUserIndividualPokemon(long userId, int pokedexNumber){
        return userPokemonRepository.findUserIndividualPokemon(userId, pokedexNumber);
    }

    public List<UserPokemon> findUserShinyPokemon(long userId){
        return userPokemonRepository.findUserShinyPokemon(userId);
    }

    public List<UserPokemonDTO> findUserPokemon(long userId){
        List <Object[]> rows =  userPokemonRepository.findUserPokemon(userId);
        return rows.stream().map(r -> new UserPokemonDTO(
                (Integer) r[0],
                (String) r[1],
                (Boolean) r[2],
                (Boolean) r[3]
        )).toList(); //needed to convert from object to UserPokemonStatusDTO
       // return userPokemonRepository.findUserPokemon(userId);
    }
    @Transactional
    public void upsertUserPokemon(long userId, UpsertUserPokemonDTO pokemon){
        userPokemonRepository.upsertUserPokemon(userId, pokemon.getPokedexNumber(), pokemon.getCaught(), pokemon.getShiny());
    }
}
