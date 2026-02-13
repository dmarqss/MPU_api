package com.dmaqrss.mpu_api.dto.user;

public record ResetPasswordDTO(
        String link,
        String email
) {
}
