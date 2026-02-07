package com.dmaqrss.mpu_api.controller;

import com.dmaqrss.mpu_api.dto.UserLoginDTO;
import com.dmaqrss.mpu_api.service.AuthService;
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
    public ResponseEntity<?> login(@RequestBody UserLoginDTO dto){
        authService.login(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
