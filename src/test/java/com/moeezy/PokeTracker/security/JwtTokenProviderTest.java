package com.moeezy.PokeTracker.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    private static final String SECRET = "unit-test-secret-key-value";
    private static final String USERNAME = "ash";

    @Mock
    private UserDetailsConfig userDetailsConfig;

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(userDetailsConfig);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3_600_000L);
        jwtTokenProvider.init();
    }

    @Test
    void createTokenRoundTripsUsername() {
        String token = jwtTokenProvider.createToken(USERNAME);
        assertEquals(USERNAME, jwtTokenProvider.getUsername(token));
    }

    @Test
    void validateTokenAcceptsFreshToken() {
        String token = jwtTokenProvider.createToken(USERNAME);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateTokenRejectsExpiredToken() {
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", -1000L);
        String expiredToken = jwtTokenProvider.createToken(USERNAME);

        CustomException ex = assertThrows(CustomException.class, () -> jwtTokenProvider.validateToken(expiredToken));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getHttpStatus());
    }

    @Test
    void validateTokenRejectsGarbage() {
        CustomException ex = assertThrows(CustomException.class, () -> jwtTokenProvider.validateToken("not-a-jwt"));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getHttpStatus());
    }

    @Test
    void validateTokenRejectsTokenSignedWithDifferentKey() {
        JwtTokenProvider otherProvider = new JwtTokenProvider(userDetailsConfig);
        ReflectionTestUtils.setField(otherProvider, "secretKey", "a-completely-different-secret-key");
        ReflectionTestUtils.setField(otherProvider, "validityInMilliseconds", 3_600_000L);
        otherProvider.init();
        String tokenFromOtherKey = otherProvider.createToken(USERNAME);

        assertThrows(CustomException.class, () -> jwtTokenProvider.validateToken(tokenFromOtherKey));
    }

    @Test
    void resolveTokenExtractsBearerToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer abc123");
        assertEquals("abc123", jwtTokenProvider.resolveToken(request));
    }

    @Test
    void resolveTokenReturnsNullWhenHeaderMissing() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        assertNull(jwtTokenProvider.resolveToken(request));
    }

    @Test
    void resolveTokenReturnsNullWhenNotBearerScheme() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic abc123");
        assertNull(jwtTokenProvider.resolveToken(request));
    }

    @Test
    void getAuthenticationBuildsAuthenticatedTokenFromUserDetails() {
        UserDetails userDetails = User.withUsername(USERNAME)
                .password("hashed")
                .authorities(new ArrayList<>())
                .build();
        when(userDetailsConfig.loadUserByUsername(USERNAME)).thenReturn(userDetails);

        String token = jwtTokenProvider.createToken(USERNAME);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        assertTrue(authentication.isAuthenticated());
        assertEquals(userDetails, authentication.getPrincipal());
    }
}
