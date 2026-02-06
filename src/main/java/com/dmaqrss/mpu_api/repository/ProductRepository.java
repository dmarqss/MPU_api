package com.dmaqrss.mpu_api.repository;

import com.dmaqrss.mpu_api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public Boolean existsByBarCode(Long barCode);
    public Optional<Product> findByBarCode(Long barCode);
}
