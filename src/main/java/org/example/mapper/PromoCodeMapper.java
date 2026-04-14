package org.example.mapper;

import org.example.dto.promocode.PromoCodeCreateDto;
import org.example.dto.promocode.PromoCodeResponseDto;
import org.example.dto.promocode.PromoCodeUpdateDto;
import org.example.entity.PromoCode;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PromoCodeMapper {
    void updatePromoCode(PromoCodeUpdateDto promoCodeUpdateDto, @MappingTarget PromoCode promoCode);

    PromoCode toEntity(PromoCodeCreateDto promoCodeCreateDto);

    PromoCodeResponseDto toDto(PromoCode promoCode);

    List<PromoCodeResponseDto> toDtos(List<PromoCode> promoCodes);
}
