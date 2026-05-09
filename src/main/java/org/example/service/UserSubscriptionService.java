package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.subscription.UserSubscriptionResponseDto;
import org.example.entity.Subscription;
import org.example.entity.User;
import org.example.entity.UserSubscription;
import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.UserSubscriptionMapper;
import org.example.repository.UserSubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserSubscriptionService {
    UserSubscriptionResponseDto buySubscription(Long userId, Long subscriptionId);

    Optional<UserSubscription> findValidActiveSubscription(Long userId);

    void updateSubscription(UserSubscription userSubscription);

    UserSubscription findActiveSubscription(Long userId);

    List<UserSubscription> findPurchaseHistory(Long userId);

    UserSubscriptionResponseDto findActiveSubscriptionDto(Long userId);

    List<UserSubscriptionResponseDto> findPurchaseHistoryDto(Long userId);

}
