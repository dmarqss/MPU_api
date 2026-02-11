package com.dmaqrss.mpu_api.repository;

import com.dmaqrss.mpu_api.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
