package com.dmaqrss.mpu_api.mapper;

import com.dmaqrss.mpu_api.dto.UserRegisterDTO;
import com.dmaqrss.mpu_api.dto.UserResponseDTO;
import com.dmaqrss.mpu_api.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRegisterDTO dto);
    UserResponseDTO toResponse(User user);
}
