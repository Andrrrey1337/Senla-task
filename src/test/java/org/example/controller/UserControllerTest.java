package org.example.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;
import org.example.dto.user.UserAdminUpdateDto;
import org.example.dto.user.UserResponseDto;
import org.example.dto.user.UserUpdateDto;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.security.JwtAuthenticationFilter;
import org.example.security.JwtService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private UserService userService;
    @MockitoBean private UserMapper userMapper;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private UserDetailsService userDetailsService;
    @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("admin");
        mockUser.setRole(Role.ADMIN); // Выдаем права

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ADMIN"),
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("USER"),
                new SimpleGrantedAuthority("ROLE_USER")
        );


        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                mockUser, null, authorities
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setBalance(BigDecimal.valueOf(100.00));

        userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setUsername("testuser");
        userResponseDto.setBalance(BigDecimal.valueOf(100.00));
    }

    @Test
    @DisplayName("GET /api/users/username/{username} - Поиск по имени (Админ)")
    void getUserByUsername_ReturnsOk() throws Exception {
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        mockMvc.perform(get("/api/users/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("GET /api/users/me - Мой профиль")
    void getMyProfile_ReturnsOk() throws Exception {
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("GET /api/users/{id} - Поиск по ID")
    void getUserById_ReturnsOk() throws Exception {
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("POST /api/users/{id}/balance - Пополнение баланса (Админ)")
    void addBalance_ReturnsOk() throws Exception {
        when(userService.addBalance(eq(1L), any(BigDecimal.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        mockMvc.perform(post("/api/users/1/balance")
                .param("amount", "100.00"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/users/me/balance - Пополнение моего баланса")
    void addMyBalance_ReturnsOk() throws Exception {
        when(userService.addBalance(eq(1L), any(BigDecimal.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        mockMvc.perform(post("/api/users/me/balance")
                .param("amount", "100.00"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /api/users/me - Обновление моего профиля")
    void updateMe_ReturnsOk() throws Exception {
        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setUsername("newUsername");
        
        when(userService.updateUser(eq(1L), any(UserUpdateDto.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        mockMvc.perform(patch("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /api/users/{id} - Обновление профиля (Админ)")
    void updateUser_ReturnsOk() throws Exception {
        UserUpdateDto updateDto = new UserUpdateDto();
        when(userService.updateUser(eq(1L), any(UserUpdateDto.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        mockMvc.perform(patch("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /api/users/{id}/status - Обновление админских полей")
    void updateAdminFields_ReturnsOk() throws Exception {
        UserAdminUpdateDto adminDto = new UserAdminUpdateDto();
        when(userService.updateAdminFields(eq(1L), any(UserAdminUpdateDto.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        mockMvc.perform(patch("/api/users/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminDto)))
                .andExpect(status().isOk());
    }
}
