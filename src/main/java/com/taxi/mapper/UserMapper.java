package com.taxi.mapper;

import com.taxi.dto.RegisterUserRequest;
import com.taxi.dto.UserResponseDTO;
import com.taxi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target ="id", ignore= true)
    User toEntity(RegisterUserRequest request);

    UserResponseDTO toResponseDTO(User user);
}