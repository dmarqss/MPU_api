package com.dmaqrss.mpu_api.dto.order;

import java.util.List;

public record CreateOrderRequestDTO(
        List<OrderItemRequestDTO> items
) {
}
