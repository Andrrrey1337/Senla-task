package org.example.mapper;

import org.example.dto.tariff.TariffCreateDto;
import org.example.dto.tariff.TariffResponseDto;
import org.example.dto.tariff.TariffUpdateDto;
import org.example.entity.Tariff;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TariffMapper {
    void updateEntity(TariffUpdateDto tariffUpdateDto, @MappingTarget Tariff tariff);

    TariffResponseDto toDto(Tariff tariff);

    List<TariffResponseDto> toDtos(List<Tariff> tariffs);

    Tariff toEntity(TariffCreateDto tariffCreateDto);
}
