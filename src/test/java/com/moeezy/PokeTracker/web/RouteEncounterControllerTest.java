package com.moeezy.PokeTracker.web;

import com.moeezy.PokeTracker.data.entity.RouteEncounter;
import com.moeezy.PokeTracker.service.RouteEncounterService;
import org.junit.jupiter.api.Test;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.Mockito.when;

//random port used in case other apis need to be tested -> no conflicts
@WebMvcTest(RouteEncounterController.class)
public class RouteEncounterControllerTest {
    private static final int routeId = 21;
    private static final String time = "Day";
    private static final int pokedexNum1 = 1;
    private static final int pokedexNum2 = 7;
    private static final String method1 = "Cave";
    private static final String method2 = "Grass";
    private static final String AVAILABLE_ROUTE_POKEMON_URL = "/v1/RouteEncounter/" + routeId + "/" + time;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RouteEncounterService routeEncounterServiceMock;


    //methods to test
    //findAvailableRoutePokemon
    @Test
    void findAvailableRoutePokemonTest() throws Exception {
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
        when(routeEncounterServiceMock.findAvailableRoutePokemon(routeId, time)).thenReturn(encounters);

//        ResponseEntity<RouteEncounter> response = testRestTemplate.getForEntity(AVAILABLE_ROUTE_POKEMON_URL, RouteEncounter.class);
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        mockMvc.perform(get(AVAILABLE_ROUTE_POKEMON_URL)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
    }
}
