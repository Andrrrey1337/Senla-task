package org.example.controller;

import org.example.entity.Role;
import org.example.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;
import org.example.dto.scooter.ScooterAdminResponseDto;
import org.example.dto.scooter.ScooterCreateDto;
import org.example.dto.scooter.ScooterResponseDto;
import org.example.dto.scooter.ScooterUpdateDto;
import org.example.entity.Scooter;
import org.example.mapper.ScooterMapper;
import org.example.security.JwtAuthenticationFilter;
import org.example.security.JwtService;
import org.example.service.ScooterService;
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

@WebMvcTest(ScooterController.class)
@AutoConfigureMockMvc(addFilters = false)
class ScooterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private ScooterService scooterService;
    @MockitoBean private ScooterMapper scooterMapper;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private UserDetailsService userDetailsService;
    @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private Scooter scooter;
    private ScooterResponseDto scooterResponseDto;
    private ScooterAdminResponseDto scooterAdminResponseDto;

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

        scooter = new Scooter();
        scooter.setId(1L);
        scooter.setSerialNumber("SN123");

        scooterResponseDto = ScooterResponseDto.builder()
                .id(1L)
                .serialNumber("SN123")
                .build();

        scooterAdminResponseDto = ScooterAdminResponseDto.builder()
                .id(1L)
                .serialNumber("SN123")
                .build();
    }

    @Test
    @DisplayName("POST /api/scooters - Создать самокат (Админ)")
    void createScooter_ReturnsCreated() throws Exception {
        ScooterCreateDto createDto = new ScooterCreateDto();
        createDto.setSerialNumber("SN123");
        createDto.setModelId(1L);
        createDto.setLatitude(new BigDecimal("53.9"));
        createDto.setLongitude(new BigDecimal("27.5"));

        when(scooterService.createScooter(any(ScooterCreateDto.class))).thenReturn(scooter);
        when(scooterMapper.toAdminDto(any(Scooter.class))).thenReturn(scooterAdminResponseDto);

        mockMvc.perform(post("/api/scooters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serialNumber").value("SN123"));
    }

    @Test
    @DisplayName("GET /api/scooters/{id} - По ID (Админ)")
    void getScooterById_ReturnsOk() throws Exception {
        when(scooterService.findScooterById(1L)).thenReturn(scooter);
        when(scooterMapper.toAdminDto(scooter)).thenReturn(scooterAdminResponseDto);

        mockMvc.perform(get("/api/scooters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("GET /api/scooters/number/{number} - По номеру")
    void getScooterByNumber_ReturnsOk() throws Exception {
        when(scooterService.findScooterBySerialNumber("SN123")).thenReturn(scooter);
        when(scooterMapper.toDto(scooter)).thenReturn(scooterResponseDto);

        mockMvc.perform(get("/api/scooters/number/SN123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("SN123"));
    }

    @Test
    @DisplayName("GET /api/scooters/available - Доступные на точке")
    void getAvailableScooters_ReturnsOk() throws Exception {
        when(scooterService.findAvailableScooters(eq(1L), any())).thenReturn(Collections.singletonList(scooter));
        when(scooterMapper.toDtos(any())).thenReturn(Collections.singletonList(scooterResponseDto));

        mockMvc.perform(get("/api/scooters/available")
                .param("pointId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].serialNumber").value("SN123"));
    }

    @Test
    @DisplayName("DELETE /api/scooters/{id} - Удалить (Админ)")
    void deleteScooter_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/scooters/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PATCH /api/scooters/{id} - Обновить (Админ)")
    void updateScooter_ReturnsOk() throws Exception {
        ScooterUpdateDto updateDto = new ScooterUpdateDto();
        updateDto.setLatitude(new BigDecimal("53.9100"));
        updateDto.setLongitude(new BigDecimal("27.5700"));
        when(scooterService.updateScooter(eq(1L), any(ScooterUpdateDto.class))).thenReturn(scooter);
        when(scooterMapper.toAdminDto(scooter)).thenReturn(scooterAdminResponseDto);

        mockMvc.perform(patch("/api/scooters/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("SN123"));
    }
}
