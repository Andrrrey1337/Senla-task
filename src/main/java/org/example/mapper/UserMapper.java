package org.example.mapper;

import org.example.dto.user.UserAdminUpdateDto;
import org.example.dto.user.UserCreateDto;
import org.example.dto.user.UserResponseDto;
import org.example.dto.user.UserUpdateDto;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    void updateEntity(UserUpdateDto userUpdateDto, @MappingTarget User user);

    void updateEntity(UserAdminUpdateDto userAdminUpdateDto, @MappingTarget User user);

    User toEntity(UserCreateDto userCreateDto);

    UserResponseDto toDto(User user);


}
