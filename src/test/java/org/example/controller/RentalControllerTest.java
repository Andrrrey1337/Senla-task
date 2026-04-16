package org.example.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;
import org.example.dto.rental.FinishRentalDto;
import org.example.dto.rental.RentalAdminResponseDto;
import org.example.dto.rental.RentalResponseDto;
import org.example.dto.rental.StartRentalDto;
import org.example.entity.Rental;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.mapper.RentalMapper;
import org.example.security.JwtAuthenticationFilter;
import org.example.security.JwtService;
import org.example.service.RentalService;
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

@WebMvcTest(RentalController.class)
@AutoConfigureMockMvc(addFilters = false)
class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private RentalService rentalService;
    @MockitoBean private RentalMapper rentalMapper;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private UserDetailsService userDetailsService;
    @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private Rental rental;
    private RentalResponseDto responseDto;
    private User user;

    @BeforeEach
    void setUp() {
        rental = new Rental();
        rental.setId(1L);

        responseDto = new RentalResponseDto();
        responseDto.setId(1L);

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
    @DisplayName("POST /api/rentals/start - Начать аренду")
    void startRental_ReturnsCreated() throws Exception {
        StartRentalDto startDto = new StartRentalDto();
        startDto.setScooterId(1L);
        startDto.setTariffId(1L);
        startDto.setUserId(1L);

        when(rentalService.startRental(any())).thenReturn(rental);
        when(rentalMapper.toDto(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/rentals/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(startDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("POST /api/rentals/{id}/finish - Завершить аренду")
    void finishRental_ReturnsOk() throws Exception {
        FinishRentalDto finishDto = new FinishRentalDto();
        finishDto.setEndLatitude(new BigDecimal("53.9100"));
        finishDto.setEndLongitude(new BigDecimal("27.5700"));
        finishDto.setDistance(new BigDecimal("5.5"));

        when(rentalService.finishRental(eq(1L), any())).thenReturn(rental);
        when(rentalMapper.toDto(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/rentals/1/finish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(finishDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/rentals/my - Моя история")
    void getMyRentals_ReturnsOk() throws Exception {
        when(rentalService.findRentalsByUserId(1L)).thenReturn(Collections.singletonList(rental));
        when(rentalMapper.toDtos(any())).thenReturn(Collections.singletonList(responseDto));


        mockMvc.perform(get("/api/rentals/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("GET /api/rentals/user/{userId} - История пользователя (Админ)")
    void getUserRentals_ReturnsOk() throws Exception {
        when(rentalService.findRentalsByUserId(1L)).thenReturn(Collections.singletonList(rental));
        when(rentalMapper.toDtos(any())).thenReturn(Collections.singletonList(responseDto));

        mockMvc.perform(get("/api/rentals/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/rentals/scooter/{scooterId} - История самоката (Админ)")
    void getScooterRentals_ReturnsOk() throws Exception {
        RentalAdminResponseDto adminResponseDto = new RentalAdminResponseDto();
        when(rentalService.findRentalsByScooterId(1L)).thenReturn(Collections.singletonList(rental));
        when(rentalMapper.toAdminDtos(any())).thenReturn(Collections.singletonList(adminResponseDto));

        mockMvc.perform(get("/api/rentals/scooter/1"))
                .andExpect(status().isOk());
    }
}
