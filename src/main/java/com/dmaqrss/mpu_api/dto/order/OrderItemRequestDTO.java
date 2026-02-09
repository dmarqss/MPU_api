package com.dmaqrss.mpu_api.dto.order;

import jakarta.validation.constraints.PositiveOrZero;

public record OrderItemRequestDTO(
        @PositiveOrZero
        Long barCode,

        @PositiveOrZero
        Integer quantity
) {
}
