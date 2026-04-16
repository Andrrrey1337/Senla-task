package org.example.service;

import org.example.dto.auth.JwtRequest;
import org.example.dto.auth.JwtResponse;
import org.example.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserDetailsService userDetailsService;
    @Mock private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private JwtRequest jwtRequest;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtRequest = new JwtRequest();
        jwtRequest.setUsername("testuser");
        jwtRequest.setPassword("password");

        userDetails = mock(UserDetails.class);
    }

    @Test
    @DisplayName("login - Успешный вход")
    void login_Success() {
        when(userDetailsService.loadUserByUsername(jwtRequest.getUsername())).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("test-token");

        JwtResponse response = authService.login(jwtRequest);

        assertNotNull(response);
        assertEquals("test-token", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("login - Неверные учетные данные")
    void login_Failure_BadCredentials() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(jwtRequest));
    }
}
