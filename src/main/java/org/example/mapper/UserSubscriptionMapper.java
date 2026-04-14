package org.example.mapper;

import org.example.dto.subscription.UserSubscriptionResponseDto;
import org.example.entity.UserSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserSubscriptionMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "subscription.id", target = "subscriptionId")
    @Mapping(source = "subscription.name", target = "subscriptionName")
    UserSubscriptionResponseDto toDto(UserSubscription userSubscription);
}