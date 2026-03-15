package com.moeezy.PokeTracker.data.repository;

import com.moeezy.PokeTracker.data.entity.UserPokemon;
import com.moeezy.PokeTracker.data.entity.UserPokemonId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPokemonRepository extends JpaRepository<UserPokemon, UserPokemonId> {
    @Query("SELECT p FROM UserPokemon p WHERE p.userId = :userId AND p.pokedexNumber = :pokedexNumber AND p.caught = true")
    Optional<UserPokemon> findUserIndividualPokemon(long userId, int pokedexNumber);

    @Query("SELECT p FROM UserPokemon p WHERE p.userId = :userId AND p.caught = true AND p.shiny = true")
    List<Object[]> findUserShinyPokemon(long userId);

    @Query(value = """
    SELECT
    p.pokedex_number AS pokedexNumber,
    p.name,
    CASE
        when up.caught = true then true
        else false
        end AS caught,
    CASE
        when up.shiny = true then true
        else false
        end AS shiny
    FROM pokemon p
    LEFT JOIN user_pokemon up
    ON up.pokedex_number = p.pokedex_number
    AND up.user_id = :userId
    ORDER BY p.pokedex_number ASC
    """, nativeQuery = true)
    List<Object[]> findUserPokemon(long userId);
    //value -> string of query, nativeQuery -> uses sql instead of jpql
    //use dto to map result of sql query directly to what we have outlined there.
    //convert to jdbc later

    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO user_pokemon (user_id, pokedex_number, caught, shiny)
    VALUES (:userId, :pokedex_number, :caught, :shiny)
    ON CONFLICT (user_id, pokedex_number) DO UPDATE SET
      caught = EXCLUDED.caught,
      shiny = EXCLUDED.shiny;
    """, nativeQuery = true)
    void upsertUserPokemon(@Param("userId") long userId,
                           @Param("pokedexNumber") int pokedexNumber,
                           @Param("caught") boolean caught,
                           @Param("shiny") boolean shiny);
}
