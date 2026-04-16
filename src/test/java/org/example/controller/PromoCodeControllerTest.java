package org.example.controller;

import org.example.entity.Role;
import org.example.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import tools.jackson.databind.ObjectMapper;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.example.dto.promocode.PromoCodeCreateDto;
import org.example.dto.promocode.PromoCodeResponseDto;
import org.example.dto.promocode.PromoCodeUpdateDto;
import org.example.entity.PromoCode;
import org.example.mapper.PromoCodeMapper;
import org.example.security.JwtAuthenticationFilter;
import org.example.security.JwtService;
import org.example.service.PromoCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PromoCodeController.class)
@AutoConfigureMockMvc(addFilters = false)
class PromoCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private PromoCodeService promoCodeService;
    @MockitoBean private PromoCodeMapper promoCodeMapper;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private UserDetailsService userDetailsService;
    @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private PromoCode promoCode;
    private PromoCodeResponseDto responseDto;

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

        promoCode = new PromoCode();
        promoCode.setId(1L);
        promoCode.setCode("SALE50");

        responseDto = new PromoCodeResponseDto();
        responseDto.setId(1L);
        responseDto.setCode("SALE50");
    }

    @Test
    @DisplayName("POST /api/promocodes - Создать промокод (Админ)")
    void create_ReturnsCreated() throws Exception {
        PromoCodeCreateDto createDto = new PromoCodeCreateDto();
        createDto.setCode("SALE50");
        createDto.setDiscount(50);

        when(promoCodeMapper.toEntity(any(PromoCodeCreateDto.class))).thenReturn(promoCode);
        when(promoCodeService.createPromoCode(any(PromoCode.class))).thenReturn(promoCode);
        when(promoCodeMapper.toDto(any(PromoCode.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/promocodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("SALE50"));
    }

    @Test
    @DisplayName("GET /api/promocodes - Все промокоды (Админ)")
    void getAll_ReturnsOk() throws Exception {
        when(promoCodeService.findAllPromoCodes()).thenReturn(Collections.singletonList(promoCode));
        when(promoCodeMapper.toDtos(any())).thenReturn(Collections.singletonList(responseDto));

        mockMvc.perform(get("/api/promocodes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("SALE50"));
    }

    @Test
    @DisplayName("GET /api/promocodes/{id} - По ID (Админ)")
    void getPromoCodeById_ReturnsOk() throws Exception {
        when(promoCodeService.findPromoCodeById(1L)).thenReturn(promoCode);
        when(promoCodeMapper.toDto(promoCode)).thenReturn(responseDto);

        mockMvc.perform(get("/api/promocodes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PATCH /api/promocodes/{id} - Обновить (Админ)")
    void update_ReturnsOk() throws Exception {
        PromoCodeUpdateDto updateDto = new PromoCodeUpdateDto();
        updateDto.setCode("NEWCODE");

        when(promoCodeService.updatePromoCode(eq(1L), any(PromoCodeUpdateDto.class))).thenReturn(promoCode);
        when(promoCodeMapper.toDto(promoCode)).thenReturn(responseDto);

        mockMvc.perform(patch("/api/promocodes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/promocodes/{id} - Удалить (Админ)")
    void delete_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/promocodes/1"))
                .andExpect(status().isNoContent());
    }
}
