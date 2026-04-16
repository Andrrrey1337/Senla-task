package org.example.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;
import org.example.dto.point.RentalPointCreateDto;
import org.example.dto.point.RentalPointDataDto;
import org.example.dto.point.RentalPointResponseDto;
import org.example.dto.point.RentalPointUpdateDto;
import org.example.dto.scooter.ScooterAdminResponseDto;
import org.example.entity.RentalPoint;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.mapper.RentalPointMapper;
import org.example.mapper.ScooterMapper;
import org.example.security.JwtAuthenticationFilter;
import org.example.security.JwtService;
import org.example.service.RentalPointService;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RentalPointController.class)
@AutoConfigureMockMvc(addFilters = false)
class RentalPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private RentalPointService rentalPointService;
    @MockitoBean private RentalPointMapper rentalPointMapper;
    @MockitoBean private ScooterMapper scooterMapper;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private UserDetailsService userDetailsService;
    @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private RentalPoint rentalPoint;
    private RentalPointResponseDto responseDto;

    @BeforeEach
    void setUp() {
        rentalPoint = new RentalPoint();
        rentalPoint.setId(1L);
        rentalPoint.setName("Point A");

        responseDto = new RentalPointResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Point A");

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
    }

    @Test
    @DisplayName("POST /api/points - Создать точку (Админ)")
    void createRentalPoint_ReturnsCreated() throws Exception {
        RentalPointCreateDto createDto = new RentalPointCreateDto();
        createDto.setName("Point A");
        createDto.setLatitude(new BigDecimal("53.9000"));
        createDto.setLongitude(new BigDecimal("27.5667"));

        when(rentalPointService.createRentalPoint(any(RentalPointCreateDto.class))).thenReturn(rentalPoint);
        when(rentalPointMapper.toDto(any(RentalPoint.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/points")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Point A"));
    }

    @Test
    @DisplayName("GET /api/points/{id} - По ID")
    void getRentalPointById_ReturnsOk() throws Exception {
        when(rentalPointService.findRentalPointById(1L)).thenReturn(rentalPoint);
        when(rentalPointMapper.toDto(rentalPoint)).thenReturn(responseDto);

        mockMvc.perform(get("/api/points/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("GET /api/points/name/{name} - По названию")
    void getRentalPointByName_ReturnsOk() throws Exception {
        when(rentalPointService.findRentalPointByName("Point A")).thenReturn(rentalPoint);
        when(rentalPointMapper.toDto(rentalPoint)).thenReturn(responseDto);

        mockMvc.perform(get("/api/points/name/Point A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Point A"));
    }

    @Test
    @DisplayName("GET /api/points - Все точки")
    void getAllRentalPoints_ReturnsOk() throws Exception {
        when(rentalPointService.findAllRentalPoints()).thenReturn(Collections.singletonList(rentalPoint));
        when(rentalPointMapper.toDtos(any())).thenReturn(Collections.singletonList(responseDto));

        mockMvc.perform(get("/api/points"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Point A"));
    }

    @Test
    @DisplayName("PATCH /api/points/{id} - Обновить (Админ)")
    void updateRentalPoint_ReturnsOk() throws Exception {
        RentalPointUpdateDto updateDto = new RentalPointUpdateDto();
        updateDto.setName("New Name");

        when(rentalPointService.updateRentalPoint(eq(1L), any(RentalPointUpdateDto.class))).thenReturn(rentalPoint);
        when(rentalPointMapper.toDto(rentalPoint)).thenReturn(responseDto);

        mockMvc.perform(patch("/api/points/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/points/{id} - Удалить (Админ)")
    void deleteRentalPoint_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/points/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/points/scooters/{id} - Самокаты на точке (Админ)")
    void getScootersAtPointById_ReturnsOk() throws Exception {
        ScooterAdminResponseDto scooterDto = new ScooterAdminResponseDto();
        when(rentalPointService.findAllScootersAtRentalPoint(1L)).thenReturn(Collections.emptyList());
        when(scooterMapper.toAdminDtos(any())).thenReturn(Collections.singletonList(scooterDto));

        mockMvc.perform(get("/api/points/scooters/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/points/data/{id} - Детальная статистика (Админ)")
    void getRentalPointDataById_ReturnsOk() throws Exception {
        RentalPointDataDto dataDto = RentalPointDataDto.builder()
                .rentalPointId(1L)
                .rentalPointName("Point A")
                .build();

        when(rentalPointService.getRentalPointDataById(1L)).thenReturn(dataDto);

        mockMvc.perform(get("/api/points/data/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rentalPointId").value(1L));
    }
}
