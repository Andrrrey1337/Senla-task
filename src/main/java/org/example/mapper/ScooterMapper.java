package org.example.mapper;

import org.example.dto.scooter.ScooterUpdateDto;
import org.example.entity.Scooter;
import org.example.service.RentalPointService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

// если в dto поле будет null, тогда маппер не будет менять поле в entity
@Mapper(componentModel = "spring", uses = RentalPointService.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ScooterMapper {


    // @MappingTarget говорит что не надо создавать новый Scooter, надо просто обновить существующий
    // rentalPointId из dto попадет в RentalPointService и результат положится в поле rentalPoint нашего самоката
    @Mapping(source = "rentalPointId", target = "rentalPoint")
    void updateEntity(ScooterUpdateDto scooterUpdateDto, @MappingTarget Scooter scooter);
}
