package com.dmaqrss.mpu_api.controller;

import com.dmaqrss.mpu_api.dto.UserRequestDTO;
import com.dmaqrss.mpu_api.service.UserService;
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
    public ResponseEntity<?> register(@RequestBody @Valid UserRequestDTO dto){
        service.register(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
