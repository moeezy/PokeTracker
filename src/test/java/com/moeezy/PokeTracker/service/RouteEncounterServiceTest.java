package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.entity.RouteEncounter;
import com.moeezy.PokeTracker.data.repository.RouteEncounterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteEncounterServiceTest {
    private static final int routeId = 21;
    private static final String time = "Day";
    private static final int pokedexNum1 = 1;
    private static final int pokedexNum2 = 7;
    private static final String method1 = "Cave";
    private static final String method2 = "Grass";

    @Mock
    private RouteEncounterRepository routeEncounterRepositoryMock;

    @InjectMocks
    private RouteEncounterService routeEncounterServiceMock;

    @Test
    void shouldFindAvailableRoutePokemon(){
        RouteEncounter routeEncounter1 = new RouteEncounter();
        routeEncounter1.setRouteId(routeId);
        routeEncounter1.setTime(time);
        routeEncounter1.setPokedexNumber(pokedexNum1);
        routeEncounter1.setMethod(method1);

        RouteEncounter routeEncounter2 = new RouteEncounter();
        routeEncounter2.setRouteId(routeId);
        routeEncounter2.setTime(time);
        routeEncounter2.setPokedexNumber(pokedexNum2);
        routeEncounter2.setMethod(method2);

        List<RouteEncounter> encounters = List.of(routeEncounter1, routeEncounter2);

        when(routeEncounterRepositoryMock.findAvailableRoutePokemon(routeId, time)).thenReturn(encounters);

        List<RouteEncounter> routeEncounterResult = routeEncounterServiceMock.findAvailableRoutePokemon(routeId, time);
        assertNotNull(routeEncounterResult, "Result list should not be null");
        assertFalse(routeEncounterResult.isEmpty(), "Expected at least one encounter");

        List<Integer> pokedexNums = routeEncounterResult.stream().map(RouteEncounter::getPokedexNumber).toList();
        Set<Integer> expectedNums = Set.of(pokedexNum1, pokedexNum2);
        Set<Integer> actualNums = new HashSet<>(pokedexNums);

        assertEquals(expectedNums, actualNums);

        List<String> methods = routeEncounterResult.stream().map(RouteEncounter::getMethod).toList();
        Set<String> expectedMethods = Set.of(method1, method2);
        Set<String> actualMethods = new HashSet<>(methods);

        assertEquals(expectedMethods, actualMethods);
    }
}
