package com.dmaqrss.mpu_api.dto;
import java.math.BigDecimal;

public record ProductResponseDTO(

        String name,

        String description,

        int amount,

        BigDecimal price,

        String type,

        Long barCode

) {
}
