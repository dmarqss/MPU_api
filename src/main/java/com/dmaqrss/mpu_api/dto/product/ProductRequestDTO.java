package com.dmaqrss.mpu_api.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotNull(message = "o nome é obrigatorio")
        String name,

        @NotNull(message = "a descrição é obrigatoria")
        String description,

        @NotNull(message = "o valor é obrigatorio")
        @PositiveOrZero(message = "o valor invalido")
        int amount,

        @NotNull(message = "o preço é obrigatorio")
        @PositiveOrZero(message = "preço invalido")
        BigDecimal price,

        @NotNull(message = "o tipo é obrigatorio")
        String type,

        @NotNull(message = "o codigo de barras é obrigaorio")
        @PositiveOrZero(message = "codigo de barras invalido")
        Long barCode

) {
}
