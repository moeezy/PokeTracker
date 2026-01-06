package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.entity.UserPokemon;
import com.moeezy.PokeTracker.data.repository.UserPokemonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPokemonServiceTest {
    private static long userId1 = 1;
    private static long userId2 = 2;
    private static int dexNum1 = 161;
    private static int dexNum2 = 163;
    private static int dexNum3 = 165;
    private static int dexNum4 = 167;
    private static boolean caughtTrue = true;
    private static boolean caughtFalse = false;
    private static boolean shinyTrue = true;
    private static boolean shinyFalse = true;

    @Mock
    private UserPokemonRepository userPokemonRepositoryMock;

    @InjectMocks
    private UserPokemonService userPokemonServiceMock;

    @Test
    void ShouldFindUserPokemonById(){
        UserPokemon userPokemon1 = new UserPokemon();
        userPokemon1.setUserId(userId1);
        userPokemon1.setPokedexNumber(dexNum1);
        userPokemon1.setCaught(caughtTrue);
        userPokemon1.setShiny(shinyFalse);

        UserPokemon userPokemon2 = new UserPokemon();
        userPokemon2.setUserId(userId2);
        userPokemon2.setPokedexNumber(dexNum2);
        userPokemon2.setCaught(caughtTrue);
        userPokemon2.setShiny(shinyTrue);

        List<UserPokemon> userPokemonList = List.of(userPokemon1, userPokemon2);

        when(userPokemonServiceMock.findUserPokemon(userId1, dexNum1)).thenReturn(Optional.of(userPokemon1));
        Optional<UserPokemon> userPokemonResult1 = userPokemonServiceMock.findUserPokemon(userId1, dexNum1);
        assertNotNull(userPokemonResult1, "userPokemonResult should not be null");
        assertFalse(userPokemonResult1.isEmpty(), "Expected 1 Record");

        when(userPokemonServiceMock.findUserPokemon(userId1, dexNum2)).thenReturn(Optional.empty());
        Optional<UserPokemon> userPokemonResult2 = userPokemonServiceMock.findUserPokemon(userId1, dexNum2);
        assertTrue(userPokemonResult2.isEmpty(), "This user should not have this pokemon");

    }

    @Test
    void shouldFindUserShinyPokemon(){
        UserPokemon userPokemon1 = new UserPokemon();
        userPokemon1.setUserId(userId1);
        userPokemon1.setPokedexNumber(dexNum1);
        userPokemon1.setCaught(caughtTrue);
        userPokemon1.setShiny(shinyTrue);
    }

}
