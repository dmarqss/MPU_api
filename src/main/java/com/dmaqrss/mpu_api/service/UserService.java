package com.dmaqrss.mpu_api.service;

import com.dmaqrss.mpu_api.dto.UserRequestDTO;
import com.dmaqrss.mpu_api.exception.BusinessException;
import com.dmaqrss.mpu_api.mapper.UserMapper;
import com.dmaqrss.mpu_api.model.User;
import com.dmaqrss.mpu_api.model.UserRoles;
import com.dmaqrss.mpu_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    UserMapper mapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void register(UserRequestDTO dto){
        if(repository.existsByEmail(dto.email())){throw new BusinessException("o email já existe");}

        String encryptedPassword = passwordEncoder.encode(dto.password());
        User user = mapper.toEntity(dto);
        user.setPassword(encryptedPassword);
        user.setRole(UserRoles.USER);
        repository.save(user);
    }

}
