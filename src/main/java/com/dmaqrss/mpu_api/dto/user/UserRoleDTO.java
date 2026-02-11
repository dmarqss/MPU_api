package com.dmaqrss.mpu_api.dto.user;

import com.dmaqrss.mpu_api.model.roles.UserRoles;
import jakarta.validation.constraints.NotNull;

public record UserRoleDTO(
        @NotNull
        UserRoles role
) {
}
