package com.dmaqrss.mpu_api.dto.user;

import com.dmaqrss.mpu_api.model.roles.UserRoles;

public record UserResponseDTO(
        String name,
        String email,
        UserRoles role
) {
}
