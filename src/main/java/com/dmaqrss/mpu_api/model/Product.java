package com.dmaqrss.mpu_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @PositiveOrZero
    private int amount;

    @Column(nullable = false)
    @PositiveOrZero
    private BigDecimal price;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, unique = true)
    @PositiveOrZero
    private Long barCode;

    public Product(String name, String description, int amount, BigDecimal price, String type, Long barCode) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.price = price;
        this.type = type;
        this.barCode = barCode;
    }


}
