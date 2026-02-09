package com.dmaqrss.mpu_api.repository;

import com.dmaqrss.mpu_api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
