package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.subscription.SubscriptionCreateDto;
import org.example.dto.subscription.SubscriptionResponseDto;
import org.example.dto.subscription.SubscriptionUpdateDto;
import org.example.entity.Subscription;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.SubscriptionMapper;
import org.example.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface SubscriptionService {
    SubscriptionResponseDto createSubscription(SubscriptionCreateDto dto);

    Subscription findSubscriptionById(Long id);

    List<SubscriptionResponseDto> findAllSubscriptions();

    SubscriptionResponseDto updateSubscription(Long id, SubscriptionUpdateDto subscriptionUpdateDto);

    SubscriptionResponseDto getSubscriptionDtoById(Long id);

    void deleteSubscription(Long id);

}
