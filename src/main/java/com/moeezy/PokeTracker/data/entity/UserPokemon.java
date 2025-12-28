package com.moeezy.PokeTracker.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@IdClass( UserPokemonId.class )
@Table(name="user_pokemon")
@Data
@ToString
public class UserPokemon {
    @Id
    @Column(name="user_id")
    private long userId;

    @Id
    @Column(name="pokedex_number")
    private int pokedexNumber;

    @Column(name="caught")
    private boolean caught;

    @Column(name="shiny")
    private boolean shiny;
}
