package com.dmaqrss.mpu_api.mapper;

import com.dmaqrss.mpu_api.dto.order.OrderResponseDTO;
import com.dmaqrss.mpu_api.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(source = "id", target = "orderId")
    OrderResponseDTO toResponse(Order order);
}