package com.moeezy.PokeTracker.data.dto.PokeApi;

public class PokemonEncounterProcessingDto {
    private String method;
    private String time;
    private String radio;
    private String swarm;

    public PokemonEncounterProcessingDto(String method, String time, String radio, String swarm) {
        this.method = method;
        this.time = time;
        this.radio = radio;
        this.swarm = swarm;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
