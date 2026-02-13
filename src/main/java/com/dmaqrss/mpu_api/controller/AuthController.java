package com.dmaqrss.mpu_api.controller;

import com.dmaqrss.mpu_api.dto.user.UserLoginDTO;
import com.dmaqrss.mpu_api.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/login")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping
    @Operation(
            summary = "user login",
            description = "Validates the user's login and returns a simple token. (permite all roles)"
    )
    public ResponseEntity<String> login(@RequestBody UserLoginDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(dto));
    }

}
