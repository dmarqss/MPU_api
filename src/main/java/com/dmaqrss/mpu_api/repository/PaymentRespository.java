package com.dmaqrss.mpu_api.repository;

import com.dmaqrss.mpu_api.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRespository extends JpaRepository<Payment, Long> {
    Optional<Payment> findById(Long id);
}
