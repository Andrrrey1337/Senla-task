package org.example.mapper;

import org.example.dto.TariffUpdateDto;
import org.example.entity.Tariff;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TariffMapper {
    void updateEntity(TariffUpdateDto tariffUpdateDto, @MappingTarget Tariff tariff);
}
