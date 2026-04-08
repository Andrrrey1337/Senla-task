package org.example.mapper;

import org.example.dto.scooterModel.ScooterModelUpdateDto;
import org.example.entity.ScooterModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ScooterModelMapper {
    void updateEntity(ScooterModelUpdateDto scooterModelUpdateDto, @MappingTarget ScooterModel scooterModel);
}
