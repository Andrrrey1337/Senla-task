package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.subscription.SubscriptionCreateDto;
import org.example.dto.subscription.SubscriptionResponseDto;
import org.example.dto.subscription.SubscriptionUpdateDto;
import org.example.dto.subscription.UserSubscriptionResponseDto;
import org.example.entity.Subscription;
import org.example.entity.User;
import org.example.entity.UserSubscription;
import org.example.mapper.SubscriptionMapper;
import org.example.mapper.UserSubscriptionMapper;
import org.example.service.SubscriptionService;
import org.example.service.UserSubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Абонементы", description = "Просмотр и управление тарифными планами")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;
    private final UserSubscriptionMapper userSubscriptionMapper;
    private final UserSubscriptionService userSubscriptionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создать новый абонемент (Админ)")
    public ResponseEntity<SubscriptionResponseDto> create(@Valid @RequestBody SubscriptionCreateDto dto) {
        Subscription entity = subscriptionService.createSubscription(subscriptionMapper.toEntity(dto));
        return new ResponseEntity<>(subscriptionMapper.toDto(entity), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/buy")
    @Operation(summary = "Купить абонемент", description = "Списывает средства с баланса текущего авторизованного пользователя")
    public ResponseEntity<UserSubscriptionResponseDto> buySubscription(@PathVariable Long id) { // id подписки
        // id пользователя возьмем из токена
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserSubscription userSubscription = userSubscriptionService.buySubscription(user.getId(), id);

        return ResponseEntity.ok(userSubscriptionMapper.toDto(userSubscription));
    }

    @GetMapping
    @Operation(summary = "Посмотреть список всех абонементов (Все)")
    public List<SubscriptionResponseDto> getAll() {
        return subscriptionMapper.toDtos(subscriptionService.findAllSubscriptions());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить абонемент по его id (Админ)")
    public SubscriptionResponseDto getSubscriptionById(@PathVariable Long id) {
        return subscriptionMapper.toDto(subscriptionService.findSubscriptionById(id));
    }

    @GetMapping("/my/active")
    @Operation(summary = "Мой активный абонемент", description = "Возвращает информацию о текущем действующем абонементе пользователя")
    public ResponseEntity<UserSubscriptionResponseDto> getMyActiveSubscription() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserSubscription active = userSubscriptionService.findActiveSubscription(currentUser.getId());
        return ResponseEntity.ok(userSubscriptionMapper.toDto(active));
    }

    @GetMapping("/my/history")
    @Operation(summary = "История моих покупок", description = "Возвращает список всех когда-либо купленных абонементов")
    public List<UserSubscriptionResponseDto> getMySubscriptionHistory() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userSubscriptionService.findPurchaseHistory(currentUser.getId())
                .stream()
                .map(userSubscriptionMapper::toDto)
                .toList();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Изменить условия абонемента (Админ)")
    public ResponseEntity<SubscriptionResponseDto> update(@PathVariable Long id, @Valid @RequestBody SubscriptionUpdateDto dto) {
        Subscription entity = subscriptionService.updateSubscription(id, dto);
        return ResponseEntity.ok(subscriptionMapper.toDto(entity));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить абонемент (Админ)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
