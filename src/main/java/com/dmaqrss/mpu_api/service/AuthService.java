package com.dmaqrss.mpu_api.service;

import com.dmaqrss.mpu_api.dto.UserLoginDTO;
import com.dmaqrss.mpu_api.model.User;
import com.dmaqrss.mpu_api.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService{
    @Autowired
    AuthenticationManager authManager;

    @Autowired
    TokenService tokenService;

    public String login(UserLoginDTO dto){
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var auth = this.authManager.authenticate(usernamePassword);

        return tokenService.generateToken((User) auth.getPrincipal());
    }
}
