package org.example.mapper;

import org.example.dto.point.RentalPointCreateDto;
import org.example.dto.point.RentalPointResponseDto;
import org.example.dto.point.RentalPointUpdateDto;
import org.example.entity.RentalPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RentalPointMapper {
    @Mapping(target = "parent", ignore = true) // обновлю в сервисе потому что через uses будет цикл зависимость
    void updateEntity(RentalPointUpdateDto rentalPointUpdateDto, @MappingTarget RentalPoint rentalPoint);

    @Mapping(target = "parent", ignore = true)
    RentalPoint toEntity(RentalPointCreateDto rentalPointCreateDto);

    @Mapping(source = "parent.id", target = "parentId")
    RentalPointResponseDto toDto(RentalPoint rentalPoint);

    List<RentalPointResponseDto> toDtos(List<RentalPoint> rentalPoints);
}
