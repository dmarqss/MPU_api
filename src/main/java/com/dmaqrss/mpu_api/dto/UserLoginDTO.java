package com.dmaqrss.mpu_api.dto;

import jakarta.validation.constraints.NotNull;

public record UserLoginDTO(
        @NotNull
        String email,
        @NotNull
        String password
) {
}
