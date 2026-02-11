package com.dmaqrss.mpu_api.mapper;

import com.dmaqrss.mpu_api.dto.order.OrderItemResponseDTO;
import com.dmaqrss.mpu_api.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "product.barCode", target = "productBarCode")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(
            target = "subTotal",
            expression = "java(item.getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())))"
    )
    OrderItemResponseDTO toResponse(OrderItem item);
}
