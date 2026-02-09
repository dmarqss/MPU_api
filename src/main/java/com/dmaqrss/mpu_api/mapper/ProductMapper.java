package com.dmaqrss.mpu_api.mapper;

import com.dmaqrss.mpu_api.dto.product.ProductRequestDTO;
import com.dmaqrss.mpu_api.dto.product.ProductResponseDTO;
import com.dmaqrss.mpu_api.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequestDTO dto);
    ProductResponseDTO toResponse(Product product);
}
