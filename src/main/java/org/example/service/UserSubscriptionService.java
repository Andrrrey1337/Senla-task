package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Subscription;
import org.example.entity.User;
import org.example.entity.UserSubscription;
import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.UserSubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserSubscriptionService {
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public UserSubscription buySubscription(Long userId, Long subscriptionId) {
        User user =  userService.findById(userId);
        Subscription subscription = subscriptionService.findSubscriptionById(subscriptionId);

        if (userSubscriptionRepository.findActiveByUserId(userId).isPresent()) {
            throw new BusinessException("У вас уже есть активный абонемент. Дождитесь его окончания.");
        }

        if (user.getBalance().compareTo(subscription.getPrice()) < 0) {
            throw new BusinessException("Недостаточно средств для покупки абонемента. Пополните баланс.");
        }

        user.setBalance(user.getBalance().subtract(subscription.getPrice()));

        UserSubscription userSubscription = UserSubscription.builder()
                .user(user)
                .subscription(subscription)
                .endDate(LocalDateTime.now().plusDays(subscription.getDurationDays()))
                .remainingMinutes(subscription.getIncludeMinutes())
                .isActive(true)
                .build();

        userSubscription = userSubscriptionRepository.create(userSubscription);

        log.info("Пользователь {} успешно купил абонемент '{}'. Списано: {} руб.",
                user.getUsername(), subscription.getName(), subscription.getPrice());

        return userSubscription;
    }

    @Transactional(readOnly = true)
    public UserSubscription findActiveSubscription(Long userId) { // активный абонемент
        return userSubscriptionRepository.findActiveByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("У вас нет активного абонемента"));
    }

    @Transactional(readOnly = true)
    public List<UserSubscription> findPurchaseHistory(Long userId) { // История покупок абонементов
        List<UserSubscription> history = userSubscriptionRepository.findAllByUserId(userId);
        log.info("Получена история подписок для пользователя ID={}. Записей: {}", userId, history.size());
        return history;
    }
}
