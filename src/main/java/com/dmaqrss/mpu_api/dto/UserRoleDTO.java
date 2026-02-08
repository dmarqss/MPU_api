package com.dmaqrss.mpu_api.dto;

import com.dmaqrss.mpu_api.model.UserRoles;
import jakarta.validation.constraints.NotNull;

public record UserRoleDTO(
        @NotNull
        UserRoles role
) {
}
