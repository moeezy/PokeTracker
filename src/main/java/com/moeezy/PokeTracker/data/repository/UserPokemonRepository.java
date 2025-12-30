package com.moeezy.PokeTracker.data.repository;

import com.moeezy.PokeTracker.data.entity.UserPokemon;
import com.moeezy.PokeTracker.data.entity.UserPokemonId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPokemonRepository extends JpaRepository<UserPokemon, UserPokemonId> {
    @Query("Select p from UserPokemon p WHERE p.userId = :userId AND p.pokedexNumber = :pokedexNumber")
    Optional<UserPokemon> findUserPokemon(long userId, int pokedexNumber);

    @Query("Select p from UserPokemon p WHERE p.userId = :userId AND p.caught = true AND p.shiny = true")
    Optional<UserPokemon> findUserShinyPokemon(long userId);
}
