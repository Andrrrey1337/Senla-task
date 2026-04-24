package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.subscription.SubscriptionUpdateDto;
import org.example.entity.Subscription;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.SubscriptionMapper;
import org.example.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    public Subscription createSubscription(Subscription subscription) {
        subscription = subscriptionRepository.create(subscription);
        log.info("Создан новый абонемент: {}", subscription.getName());
        return subscription;
    }

    public Subscription findSubscriptionById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Абонемент с ID " + id + " не найден"));

        log.info("Успешно найден абонемент с ID: {}", id);
        return subscription;
    }

    public List<Subscription> findAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        log.info("Получен список всех абонементов. Количество: {}", subscriptions.size());
        return subscriptions;
    }

    public Subscription updateSubscription(Long id, SubscriptionUpdateDto subscriptionUpdateDto) {
        Subscription subscription = findSubscriptionById(id);

        subscriptionMapper.updateSubscription(subscriptionUpdateDto, subscription);
        log.info("Данные абонемента с ID {} успешно обновлены", id);

        return subscription;
    }

    public void deleteSubscription(Long id){
        findSubscriptionById(id);
        subscriptionRepository.deleteById(id);
        log.info("Абонемент с ID {} успешно удален", id);
    }
}
