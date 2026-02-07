package com.dmaqrss.mpu_api.mapper;

import com.dmaqrss.mpu_api.dto.UserRequestDTO;
import com.dmaqrss.mpu_api.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDTO dto);
}
