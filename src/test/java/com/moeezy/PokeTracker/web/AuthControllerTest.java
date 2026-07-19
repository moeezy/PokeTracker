package com.moeezy.PokeTracker.web;

import com.moeezy.PokeTracker.data.dto.Auth.AuthResponseDTO;
import com.moeezy.PokeTracker.service.AuthService;
import com.moeezy.PokeTracker.web.exception.InvalidCredentialsException;
import com.moeezy.PokeTracker.web.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    private static final String REGISTER_URL = "/v1/users/register";
    private static final String LOGIN_URL = "/v1/users/login";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authServiceMock;

    @Test
    void registerReturnsCreatedWithTokenAndUsernameOnSuccess() throws Exception {
        String body = """
                {"username":"ash","email":"ash@pallet.town","password":"pikachu123"}
                """;
        when(authServiceMock.register(any())).thenReturn(new AuthResponseDTO("jwt-token", "ash", 1));

        mockMvc.perform(post(REGISTER_URL).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.username").value("ash"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void registerReturnsConflictWhenUserAlreadyExists() throws Exception {
        String body = """
                {"username":"ash","email":"ash@pallet.town","password":"pikachu123"}
                """;
        doThrow(new UserAlreadyExistsException("Username already taken: ash"))
                .when(authServiceMock).register(any());

        mockMvc.perform(post(REGISTER_URL).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void loginReturnsTokenAndUsernameOnSuccess() throws Exception {
        String body = """
                {"username":"ash","password":"pikachu123"}
                """;
        when(authServiceMock.login(any())).thenReturn(new AuthResponseDTO("jwt-token", "ash", 1));

        mockMvc.perform(post(LOGIN_URL).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.username").value("ash"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void loginReturnsUnauthorizedOnBadCredentials() throws Exception {
        String body = """
                {"username":"ash","password":"wrong"}
                """;
        when(authServiceMock.login(any())).thenThrow(new InvalidCredentialsException("Invalid username or password"));

        mockMvc.perform(post(LOGIN_URL).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnauthorized());
    }
}
