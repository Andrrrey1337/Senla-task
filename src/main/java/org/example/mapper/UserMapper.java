package org.example.mapper;

import org.example.dto.UserUpdateDto;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    void updateEntity(UserUpdateDto userUpdateDto, @MappingTarget User user);
}
