package org.example.controller;

import org.example.entity.Role;
import org.example.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;
import org.example.dto.scooterModel.ScooterModelCreateDto;
import org.example.dto.scooterModel.ScooterModelResponseDto;
import org.example.dto.scooterModel.ScooterModelUpdateDto;
import org.example.entity.ScooterModel;
import org.example.mapper.ScooterModelMapper;
import org.example.security.JwtAuthenticationFilter;
import org.example.security.JwtService;
import org.example.service.ScooterModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScooterModelController.class)
@AutoConfigureMockMvc(addFilters = false)
class ScooterModelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private ScooterModelService scooterModelService;
    @MockitoBean private ScooterModelMapper scooterModelMapper;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private UserDetailsService userDetailsService;
    @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private ScooterModel scooterModel;
    private ScooterModelResponseDto responseDto;

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

        scooterModel = new ScooterModel();
        scooterModel.setId(1L);
        scooterModel.setName("Model X");

        responseDto = new ScooterModelResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Model X");
    }

    @Test
    @DisplayName("POST /api/scooter-models - Создать модель (Админ)")
    void createScooterModel_ReturnsCreated() throws Exception {
        ScooterModelCreateDto createDto = new ScooterModelCreateDto();
        createDto.setName("Model X");
        createDto.setPricePerMinute(new BigDecimal("5.00"));

        when(scooterModelMapper.toEntity(any(ScooterModelCreateDto.class))).thenReturn(scooterModel);
        when(scooterModelService.createScooterModel(any(ScooterModel.class))).thenReturn(scooterModel);
        when(scooterModelMapper.toDto(any(ScooterModel.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/scooter-models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Model X"));
    }

    @Test
    @DisplayName("GET /api/scooter-models/{id} - По ID")
    void getScooterModelById_ReturnsOk() throws Exception {
        when(scooterModelService.findScooterModelById(1L)).thenReturn(scooterModel);
        when(scooterModelMapper.toDto(scooterModel)).thenReturn(responseDto);

        mockMvc.perform(get("/api/scooter-models/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("GET /api/scooter-models - Все модели")
    void getAllScooterModels_ReturnsOk() throws Exception {
        when(scooterModelService.findAllScooterModel()).thenReturn(Collections.singletonList(scooterModel));
        when(scooterModelMapper.toDtos(any())).thenReturn(Collections.singletonList(responseDto));

        mockMvc.perform(get("/api/scooter-models"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Model X"));
    }

    @Test
    @DisplayName("PATCH /api/scooter-models/{id} - Обновить (Админ)")
    void updateScooterModel_ReturnsOk() throws Exception {
        ScooterModelUpdateDto updateDto = new ScooterModelUpdateDto();
        updateDto.setName("New Name");

        when(scooterModelService.updateScooterModel(eq(1L), any(ScooterModelUpdateDto.class))).thenReturn(scooterModel);
        when(scooterModelMapper.toDto(scooterModel)).thenReturn(responseDto);

        mockMvc.perform(patch("/api/scooter-models/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/scooter-models/{id} - Удалить (Админ)")
    void deleteScooterModel_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/scooter-models/1"))
                .andExpect(status().isNoContent());
    }
}
