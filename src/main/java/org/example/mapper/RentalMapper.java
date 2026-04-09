package org.example.mapper;

import org.example.dto.rental.RentalAdminResponseDto;
import org.example.dto.rental.RentalResponseDto;
import org.example.entity.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RentalMapper {
    @Mapping(source = "scooter.serialNumber", target = "scooterSerialNumber")
    @Mapping(source = "tariff.name", target = "tariffName")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "promoCode.code", target = "promoCode")
    RentalResponseDto toDto(Rental rental);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "scooter.id", target = "scooterId")
    @Mapping(source = "scooter.serialNumber", target = "scooterSerialNumber")
    @Mapping(source = "tariff.id", target = "tariffId")
    @Mapping(source = "tariff.name", target = "tariffName")
    @Mapping(source = "promoCode.code", target = "promoCode")
    RentalAdminResponseDto  toAdminDto(Rental rental);

    List<RentalResponseDto> toDtos(List<Rental> rentals);

    List<RentalAdminResponseDto> toAdminDtos(List<Rental> rentals);
}
