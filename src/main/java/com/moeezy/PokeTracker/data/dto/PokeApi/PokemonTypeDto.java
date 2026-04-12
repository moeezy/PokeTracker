package com.moeezy.PokeTracker.data.dto.PokeApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonTypeDto {
    private String type1;
    private String type2;

    public String getType1() {
        return type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

}
