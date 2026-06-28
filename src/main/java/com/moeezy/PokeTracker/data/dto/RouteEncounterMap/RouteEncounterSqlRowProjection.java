package com.moeezy.PokeTracker.data.dto.RouteEncounterMap;
//row projection handles mapping from sql query automatically assuming same names as cols
public interface RouteEncounterSqlRowProjection {

    String getRouteId();

    String getAreaId();

    String getRouteName();

    String getAreaName();

    String getPokedexNumber();

    String getName();

    Boolean getCaught();

    String getTimeOfDay();

    String getRadio();

    String getSwarm();

    String getEncounterMethod();
}