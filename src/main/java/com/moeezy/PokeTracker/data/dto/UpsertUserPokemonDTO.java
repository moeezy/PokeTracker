package com.moeezy.PokeTracker.data.dto;

//used for updating userPokemon data
public class UpsertUserPokemonDTO {
    private Integer pokedexNumber;
    private Boolean caught;
    private Boolean shiny;

    public UpsertUserPokemonDTO(Integer pokedexNumber, String name, Boolean caught, Boolean shiny){
        this.pokedexNumber = pokedexNumber;
        this.caught = caught;
        this.shiny = shiny;
    }

    public Integer getPokedexNumber(){
        return pokedexNumber;
    }

    public Boolean getCaught(){
        return caught;
    }

    public Boolean getShiny(){
        return shiny;
    }
}
