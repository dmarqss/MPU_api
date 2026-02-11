package com.dmaqrss.mpu_api.dto.order;

import java.math.BigDecimal;

public record OrderItemResponseDTO(
        Long productBarCode,

        String productName,

        Integer quantity,

        BigDecimal price,

        BigDecimal subTotal
) {
}
