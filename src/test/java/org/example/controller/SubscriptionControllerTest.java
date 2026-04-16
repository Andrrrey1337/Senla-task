package org.example.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;
import org.example.dto.subscription.SubscriptionCreateDto;
import org.example.dto.subscription.SubscriptionResponseDto;
import org.example.dto.subscription.SubscriptionUpdateDto;
import org.example.dto.subscription.UserSubscriptionResponseDto;
import org.example.entity.Role;
import org.example.entity.Subscription;
import org.example.entity.User;
import org.example.entity.UserSubscription;
import org.example.mapper.SubscriptionMapper;
import org.example.mapper.UserSubscriptionMapper;
import org.example.security.JwtAuthenticationFilter;
import org.example.security.JwtService;
import org.example.service.SubscriptionService;
import org.example.service.UserSubscriptionService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
@AutoConfigureMockMvc(addFilters = false)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean private SubscriptionService subscriptionService;
    @MockitoBean private SubscriptionMapper subscriptionMapper;
    @MockitoBean private UserSubscriptionMapper userSubscriptionMapper;
    @MockitoBean private UserSubscriptionService userSubscriptionService;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private UserDetailsService userDetailsService;
    @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

    private Subscription testSubscription;
    private SubscriptionResponseDto subscriptionResponseDto;
    private UserSubscriptionResponseDto userSubscriptionResponseDto;

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

        testSubscription = Subscription.builder()
                .id(1L)
                .name("Monthly")
                .price(BigDecimal.valueOf(499.0))
                .build();

        subscriptionResponseDto = SubscriptionResponseDto.builder()
                .id(1L)
                .name("Monthly")
                .price(BigDecimal.valueOf(499.0))
                .build();

        userSubscriptionResponseDto = UserSubscriptionResponseDto.builder()
                .id(1L)
                .userId(1L)
                .subscriptionId(1L)
                .subscriptionName("Monthly")
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("POST /api/subscriptions - Создание абонемента (Админ)")
    void create_ReturnsCreated() throws Exception {
        SubscriptionCreateDto createDto = new SubscriptionCreateDto();
        createDto.setName("Monthly");
        createDto.setPrice(BigDecimal.valueOf(499.0));
        createDto.setDurationDays(30);
        createDto.setMinutesIncluded(100);
        createDto.setIsFreeStart(true);

        when(subscriptionMapper.toEntity(any())).thenReturn(testSubscription);
        when(subscriptionService.createSubscription(any())).thenReturn(testSubscription);
        when(subscriptionMapper.toDto(any())).thenReturn(subscriptionResponseDto);

        mockMvc.perform(post("/api/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Monthly"));
    }

    @Test
    @DisplayName("POST /api/subscriptions/{id}/buy - Купить абонемент")
    void buySubscription_ReturnsOk() throws Exception {
        UserSubscription userSub = new UserSubscription();
        when(userSubscriptionService.buySubscription(eq(1L), eq(1L))).thenReturn(userSub);
        when(userSubscriptionMapper.toDto(any())).thenReturn(userSubscriptionResponseDto);

        mockMvc.perform(post("/api/subscriptions/1/buy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscriptionName").value("Monthly"));
    }

    @Test
    @DisplayName("GET /api/subscriptions - Получить все")
    void getAll_ReturnsOk() throws Exception {
        when(subscriptionService.findAllSubscriptions()).thenReturn(Collections.singletonList(testSubscription));
        when(subscriptionMapper.toDtos(any())).thenReturn(Collections.singletonList(subscriptionResponseDto));

        mockMvc.perform(get("/api/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Monthly"));
    }

    @Test
    @DisplayName("GET /api/subscriptions/{id} - Получение по ID (Админ)")
    void getSubscriptionById_Success() throws Exception {
        when(subscriptionService.findSubscriptionById(1L)).thenReturn(testSubscription);
        when(subscriptionMapper.toDto(testSubscription)).thenReturn(subscriptionResponseDto);

        mockMvc.perform(get("/api/subscriptions/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Monthly"));
    }

    @Test
    @DisplayName("GET /api/subscriptions/my/active - Мой активный абонемент")
    void getMyActiveSubscription_ReturnsOk() throws Exception {
        UserSubscription userSub = new UserSubscription();
        when(userSubscriptionService.findActiveSubscription(1L)).thenReturn(userSub);
        when(userSubscriptionMapper.toDto(any())).thenReturn(userSubscriptionResponseDto);

        mockMvc.perform(get("/api/subscriptions/my/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscriptionName").value("Monthly"));
    }

    @Test
    @DisplayName("GET /api/subscriptions/my/history - История моих покупок")
    void getMySubscriptionHistory_Success() throws Exception {
        UserSubscription userSubscription = new UserSubscription();
        when(userSubscriptionService.findPurchaseHistory(1L)).thenReturn(List.of(userSubscription));
        when(userSubscriptionMapper.toDto(userSubscription)).thenReturn(userSubscriptionResponseDto);

        mockMvc.perform(get("/api/subscriptions/my/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].subscriptionName").value("Monthly"));
    }

    @Test
    @DisplayName("PATCH /api/subscriptions/{id} - Обновление абонемента (Админ)")
    void update_Success() throws Exception {
        SubscriptionUpdateDto updateDto = new SubscriptionUpdateDto();
        updateDto.setName("Premium");

        when(subscriptionService.updateSubscription(eq(1L), any(SubscriptionUpdateDto.class))).thenReturn(testSubscription);
        when(subscriptionMapper.toDto(testSubscription)).thenReturn(subscriptionResponseDto);

        mockMvc.perform(patch("/api/subscriptions/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/subscriptions/{id} - Удаление абонемента (Админ)")
    void delete_Success() throws Exception {
        doNothing().when(subscriptionService).deleteSubscription(1L);

        mockMvc.perform(delete("/api/subscriptions/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(subscriptionService, times(1)).deleteSubscription(1L);
    }
}
