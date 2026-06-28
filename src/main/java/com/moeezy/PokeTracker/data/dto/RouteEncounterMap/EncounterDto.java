package com.moeezy.PokeTracker.data.dto.RouteEncounterMap;

public class EncounterDto {
    String pokedexNumber;
    String name;
    boolean caught;
    String encounterMethod;
    String timeOfDay;
    String radio;
    String swarm;

    public EncounterDto(String pokedexNumber, String name, boolean caught, String encounterMethod, String timeOfDay, String radio, String swarm) {
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.caught = caught;
        this.encounterMethod = encounterMethod;
        this.timeOfDay = timeOfDay;
        this.radio = radio;
        this.swarm = swarm;
    }

    public String getPokedexNumber() {
        return pokedexNumber;
    }

    public void setPokedexNumber(String pokedexNumber) {
        this.pokedexNumber = pokedexNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isCaught() {
        return caught;
    }

    public void setCaught(boolean caught) {
        this.caught = caught;
    }


    public String getEncounterMethod() {
        return encounterMethod;
    }

    public void setEncounterMethod(String encounterMethod) {
        this.encounterMethod = encounterMethod;
    }


    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }


    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }


    public String getSwarm() {
        return swarm;
    }

    public void setSwarm(String swarm) {
        this.swarm = swarm;
    }


}
