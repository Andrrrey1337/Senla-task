package org.example.mapper;

import org.example.dto.point.RentalPointUpdateDto;
import org.example.entity.RentalPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RentalPointMapper {
    @Mapping(target = "parent", ignore = true) // обновлю в сервисе потому что через uses будет цикл зависимость
    void updateEntity(RentalPointUpdateDto rentalPointUpdateDto, @MappingTarget RentalPoint rentalPoint);
}
