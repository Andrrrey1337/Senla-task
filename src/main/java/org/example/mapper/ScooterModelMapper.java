package org.example.mapper;

import org.example.dto.scooterModel.ScooterModelCreateDto;
import org.example.dto.scooterModel.ScooterModelResponseDto;
import org.example.dto.scooterModel.ScooterModelUpdateDto;
import org.example.entity.ScooterModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ScooterModelMapper {
    void updateEntity(ScooterModelUpdateDto scooterModelUpdateDto, @MappingTarget ScooterModel scooterModel);

    ScooterModel toEntity(ScooterModelCreateDto scooterModelCreateDto);

    ScooterModelResponseDto toDto(ScooterModel scooterModel);

    List<ScooterModelResponseDto> toDtos(List<ScooterModel> scooterModels);
}
