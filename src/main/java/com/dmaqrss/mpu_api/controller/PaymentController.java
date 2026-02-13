package com.dmaqrss.mpu_api.controller;

import com.dmaqrss.mpu_api.dto.payment.PaymentResponseDTO;
import com.dmaqrss.mpu_api.model.User;
import com.dmaqrss.mpu_api.model.roles.PaymentMethod;
import com.dmaqrss.mpu_api.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentController {

    @Autowired
    PaymentService service;

    @PostMapping(path = "order/{id}/payment")
    @Operation(
            summary = "Create a new payment",
            description = "Create and return the payment information. (Need user role and The user must be the same person who created the order.)",
            security = @SecurityRequirement(name = "bearer token")
    )
    public ResponseEntity<PaymentResponseDTO> createPayment(@PathVariable Long id, @RequestParam PaymentMethod method, @AuthenticationPrincipal User user){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createPayment(id, method, user));
    }

    @PostMapping(path = "payment/{id}/confirm")
    @Operation(
            summary = "confirm payment",
            description = "simulates payment confirmation after external validation. (Need admin role.)",
            security = @SecurityRequirement(name = "bearer token")
    )
    public ResponseEntity<PaymentResponseDTO> confirmPayment(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.confirmPayment(id));
    }

    @PostMapping(path = "payment/{id}/fail")
    @Operation(
            summary = "fail payment",
            description = "simulates payment fail after external validation. (Need admin role.)",
            security = @SecurityRequirement(name = "bearer token")
    )
    public ResponseEntity<PaymentResponseDTO> failPayment(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.failPayment(id));
    }

}
