package com.moeezy.PokeTracker.data.dto.PokeApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonSpeciesDto {
    private int id;
    private int gender_rate;
    private String name;

    public int getId() {
        return id;
    }

    public int getGender_rate() {
        return gender_rate;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGender_rate(int gender_rate) {
        this.gender_rate = gender_rate;
    }

    public void setName(String name) {
        this.name = name;
    }
}
