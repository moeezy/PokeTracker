package com.moeezy.PokeTracker.data.repository;

import com.moeezy.PokeTracker.data.entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {

    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO pokemon (pokedex_number, name, primary_type, secondary_type, has_gender)
    VALUES (:pokedex_number, :name, :primary_type, :secondary_type, :has_gender);
    """, nativeQuery = true)
    void upsertPokemon(@Param("pokedex_number") int pokedexNumber,
                       @Param("name") String name,
                       @Param("primary_type") String primaryType,
                       @Param("secondary_type") String secondaryType,
                       @Param("has_gender") boolean hasGender);
}
