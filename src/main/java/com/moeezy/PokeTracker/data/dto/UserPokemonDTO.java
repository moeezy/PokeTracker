package com.moeezy.PokeTracker.data.dto;

public class UserPokemonDTO {
    private Integer pokedexNumber;
    private String name;
    private Boolean caught;
    private Boolean shiny;

    public UserPokemonDTO(Integer pokedexNumber, String name, Boolean caught, Boolean shiny){
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.caught = caught;
        this.shiny = shiny;
    }

    public Integer getPokedexNumber(){
        return pokedexNumber;
    }

    public String getName(){
        return name;
    }

    public Boolean getCaught(){
        return caught;
    }

    public Boolean getShiny(){
        return shiny;
    }
}


