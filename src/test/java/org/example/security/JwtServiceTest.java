package org.example.security;

import org.example.entity.Role;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String secret = Base64.getEncoder().encodeToString("testSecretKeyWithEnoughLengthForHS256Algorithm".getBytes());
    private final long expiration = 3600000;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", secret);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", expiration);
    }

    @Test
    @DisplayName("generateToken and extractUsername - Успех")
    void generateAndExtract_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setRole(Role.USER);

        String token = jwtService.generateToken(user);
        assertNotNull(token);

        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    @DisplayName("isTokenValid - Успех")
    void isTokenValid_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setRole(Role.USER);

        String token = jwtService.generateToken(user);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    @DisplayName("isTokenValid - Невалидный токен")
    void isTokenValid_InvalidToken_ReturnsFalse() {
        assertFalse(jwtService.isTokenValid("invalidtoken"));
    }
}
