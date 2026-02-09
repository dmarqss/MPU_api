package com.dmaqrss.mpu_api.controller;

import com.dmaqrss.mpu_api.dto.order.CreateOrderRequestDTO;
import com.dmaqrss.mpu_api.model.User;
import com.dmaqrss.mpu_api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "order")
public class OrderController {

    @Autowired
    OrderService service;

    @PostMapping
    @Operation(
            summary = "Create a new Order",
            description = "Create and return the order information. (Need seller role)",
            security = @SecurityRequirement(name = "bearer token")
    )
    public ResponseEntity<?> createOrder(@RequestBody @Valid CreateOrderRequestDTO dto, @AuthenticationPrincipal User user){
        return ResponseEntity.status(HttpStatus.OK).body(service.createOrder(dto,user));
    }

}
