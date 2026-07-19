package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.dto.Auth.AuthResponseDTO;
import com.moeezy.PokeTracker.data.dto.Auth.LoginUserDTO;
import com.moeezy.PokeTracker.data.dto.Auth.RegisterUserDTO;
import com.moeezy.PokeTracker.data.entity.User;
import com.moeezy.PokeTracker.data.repository.UserRepository;
import com.moeezy.PokeTracker.security.JwtTokenProvider;
import com.moeezy.PokeTracker.web.exception.InvalidCredentialsException;
import com.moeezy.PokeTracker.web.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String USERNAME = "ash";
    private static final String EMAIL = "ash@pallet.town";
    private static final String RAW_PASSWORD = "pikachu123";
    private static final String ENCODED_PASSWORD = "bcrypt-hash";
    private static final int USER_ID = 42;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerSavesUserWithEncodedPasswordAndReturnsTokenWithUsernameAndId() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(jwtTokenProvider.createToken(USERNAME)).thenReturn("jwt-token");
        User persisted = new User();
        persisted.setUserId(USER_ID);
        persisted.setUsername(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(persisted);

        AuthResponseDTO response = authService.register(new RegisterUserDTO(USERNAME, EMAIL, RAW_PASSWORD));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals(USERNAME, saved.getUsername());
        assertEquals(EMAIL, saved.getEmail());
        assertEquals(ENCODED_PASSWORD, saved.getPassword());
        assertNotEquals(RAW_PASSWORD, saved.getPassword());

        assertEquals("jwt-token", response.getToken());
        assertEquals(USERNAME, response.getUsername());
        assertEquals(USER_ID, response.getId());
    }

    @Test
    void registerRejectsDuplicateUsername() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> authService.register(new RegisterUserDTO(USERNAME, EMAIL, RAW_PASSWORD)));

        verify(userRepository, never()).save(any());
    }

    @Test
    void registerRejectsDuplicateEmail() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> authService.register(new RegisterUserDTO(USERNAME, EMAIL, RAW_PASSWORD)));

        verify(userRepository, never()).save(any());
    }

    @Test
    void loginReturnsTokenUsernameAndIdOnValidCredentials() {
        when(jwtTokenProvider.createToken(USERNAME)).thenReturn("jwt-token");
        User persisted = new User();
        persisted.setUserId(USER_ID);
        persisted.setUsername(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(persisted);

        AuthResponseDTO response = authService.login(new LoginUserDTO(USERNAME, RAW_PASSWORD));

        assertEquals("jwt-token", response.getToken());
        assertEquals(USERNAME, response.getUsername());
        assertEquals(USER_ID, response.getId());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void loginRejectsBadCredentials() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad creds"));

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(new LoginUserDTO(USERNAME, "wrong-password")));

        verify(jwtTokenProvider, never()).createToken(any());
    }
}
