package com.dmaqrss.mpu_api.controller;

import com.dmaqrss.mpu_api.dto.user.UserRegisterDTO;
import com.dmaqrss.mpu_api.dto.user.UserResponseDTO;
import com.dmaqrss.mpu_api.dto.user.UserRoleDTO;
import com.dmaqrss.mpu_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "user")
public class UserController {
    @Autowired
    UserService service;

    @PostMapping(path = "/register")
    @Operation(
            summary = "Create a new User",
            description = "Create a new user if one does not already exist. (permite all roles)"
    )
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRegisterDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(service.register(dto));
    }

    @PutMapping(path = "role/{email}")
    @Operation(
            summary = "Update user role",
            description = "Update user role. (Need admin role)",
            security = @SecurityRequirement(name = "bearer token")
    )
    public ResponseEntity<UserResponseDTO> updateRole(@PathVariable String email, @RequestBody @Valid UserRoleDTO role){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateRole(email,role));
    }

    @PostMapping(path = "/forgot-password")
    @Operation(
            summary = "forgot password",
            description = "Send a password recovery email. (permite all roles)"
    )
    public ResponseEntity<?> forgotPassword(@RequestParam String email){
        service.sendResetPasswordEmail(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(path = "/reset-password")
    @Operation(
            summary = "reset password",
            description = "reset password. (Need forgotPassword token)"
    )
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword){
        service.resetPassword(token, newPassword);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(path = "/{email}")
    @Operation(
            summary = "Delete user",
            description = "delete user from database. (Need admin role)",
            security = @SecurityRequirement(name = "bearer token")
    )
    public ResponseEntity<?> delete(@PathVariable String email){
        service.delete(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/{email}")
    @Operation(
            summary = "Get user",
            description = "Get data of user by email. (Need admin role)",
            security = @SecurityRequirement(name = "bearer token")
    )
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String email){
        return ResponseEntity.status(HttpStatus.OK).body(service.getUser(email));
    }



}
