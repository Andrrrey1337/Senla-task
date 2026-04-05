package org.example.mapper;

import org.example.dto.ScooterUpdateDto;
import org.example.entity.Scooter;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

// если в dto поле будет null, тогда маппер не будет менять поле в entity
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ScooterMapper {
    // @MappingTarget говорит что не надо создавать новый Scooter, надо просто обновить существующий
    void updateEntity(ScooterUpdateDto scooterUpdateDto, @MappingTarget Scooter scooter);
}
