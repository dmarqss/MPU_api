package com.dmaqrss.mpu_api.dto.payment;

import com.dmaqrss.mpu_api.model.Payment;
import com.dmaqrss.mpu_api.model.roles.PaymentMethod;
import com.dmaqrss.mpu_api.model.roles.PaymentStatus;
import java.math.BigDecimal;

public record PaymentResponseDTO(
        Long id,
        Long orderId,
        BigDecimal amount,
        PaymentStatus status,
        PaymentMethod method
) {
    public PaymentResponseDTO(Payment payment) {
        this(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getMethod()
        );
    }
}
