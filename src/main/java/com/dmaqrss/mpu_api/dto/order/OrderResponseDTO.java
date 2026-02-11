package com.dmaqrss.mpu_api.dto.order;

import com.dmaqrss.mpu_api.model.roles.OrderStatusRole;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long orderId,

        BigDecimal total,

        OrderStatusRole status,

        LocalDateTime createdAt,

        List<OrderItemResponseDTO> items

) {
}
