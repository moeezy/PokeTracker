package com.moeezy.PokeTracker.data.entity;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//implement no arg constructor and equals & hashable methods
@NoArgsConstructor
@Getter
public class UserPokemonId implements Serializable {

    private long userId;
    private int pokedexNumber;

    public UserPokemonId(long userId, int pokedexNumber){
        this.userId = userId;
        this.pokedexNumber = pokedexNumber;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof UserPokemonId userPokemonId)) return false;
        return userId == userPokemonId.userId && pokedexNumber == userPokemonId.pokedexNumber;
    }

    @Override
    public int hashCode(){
        return Objects.hash(userId, pokedexNumber);
    }
}
