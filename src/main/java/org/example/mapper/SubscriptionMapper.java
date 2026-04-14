package org.example.mapper;

import org.example.dto.subscription.SubscriptionCreateDto;
import org.example.dto.subscription.SubscriptionResponseDto;
import org.example.dto.subscription.SubscriptionUpdateDto;
import org.example.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SubscriptionMapper {
    void updateSubscription(SubscriptionUpdateDto subscriptionUpdateDto, @MappingTarget Subscription subscription);

    Subscription toEntity(SubscriptionCreateDto subscriptionCreateDto);

    SubscriptionResponseDto toDto(Subscription subscription);

    List<SubscriptionResponseDto> toDtos(List<Subscription> subscriptions);
}
