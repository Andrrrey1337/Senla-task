package org.example.controller;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;
import org.example.dto.auth.JwtRequest;
import org.example.dto.auth.JwtResponse;
import org.example.dto.user.UserCreateDto;
import org.example.dto.user.UserResponseDto;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.security.JwtAuthenticationFilter;
import org.example.security.JwtService;
import org.example.service.AuthService;
import org.example.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private AuthService authService;
    @MockitoBean private UserService userService;
    @MockitoBean private UserMapper userMapper;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private UserDetailsService userDetailsService;
    @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/auth/register - Регистрация пользователя")
    void registerUser_ReturnsCreated() throws Exception {
        UserCreateDto createDto = new UserCreateDto();
        createDto.setUsername("testuser");
        createDto.setPassword("password");

        User user = new User();
        user.setUsername("testuser");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setUsername("testuser");

        when(userMapper.toEntity(any(UserCreateDto.class))).thenReturn(user);
        when(userService.registerUser(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("POST /api/auth/login - Логин")
    void login_ReturnsOk() throws Exception {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername("testuser");
        jwtRequest.setPassword("password");

        JwtResponse jwtResponse = new JwtResponse("test-token");

        when(authService.login(any(JwtRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"));
    }
}
