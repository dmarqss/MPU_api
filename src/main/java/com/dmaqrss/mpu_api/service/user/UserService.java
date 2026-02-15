package com.dmaqrss.mpu_api.service.user;

import com.dmaqrss.mpu_api.dto.user.ResetPasswordDTO;
import com.dmaqrss.mpu_api.dto.user.UserRegisterDTO;
import com.dmaqrss.mpu_api.dto.user.UserResponseDTO;
import com.dmaqrss.mpu_api.dto.user.UserRoleDTO;
import com.dmaqrss.mpu_api.exception.BusinessException;
import com.dmaqrss.mpu_api.mapper.UserMapper;
import com.dmaqrss.mpu_api.model.User;
import com.dmaqrss.mpu_api.model.roles.UserRoles;
import com.dmaqrss.mpu_api.publisher.PasswordEmailPublisher;
import com.dmaqrss.mpu_api.repository.UserRepository;
import com.dmaqrss.mpu_api.security.TokenService;
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

    @Autowired
    TokenService tokenService;

    @Autowired
    PasswordEmailPublisher publisher;

    public UserResponseDTO register(UserRegisterDTO dto){
        if(repository.existsByEmail(dto.email())){throw new BusinessException("o email já existe");}

        String encryptedPassword = passwordEncoder.encode(dto.password());
        User user = mapper.toEntity(dto);
        user.setPassword(encryptedPassword);
        user.setRole(UserRoles.USER);
        repository.save(user);
        return mapper.toResponse(user);
    }

    public UserResponseDTO updateRole(String email, UserRoleDTO role){
        User user = repository.findByEmail(email).orElseThrow(() -> new BusinessException("o email não existe"));
        user.setRole(role.role());
        repository.save(user);
        return mapper.toResponse(user);
    }

    public void sendResetPasswordEmail(String email){
        repository.findByEmail(email).ifPresent(user -> {
            String token = tokenService.generateResetToken(email);
            String link = "http://localhost:8080/user/reset-password?token=" + token;

            publisher.resetPasswordEmail(new ResetPasswordDTO(link, email));
        });
    }

    public void resetPassword(String token, String newPassword){
        String email = tokenService.validateResetToken(token);

        repository.findByEmail(email).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            repository.save(user);
        });
    }

    public void delete(String email){
        User user = repository.findByEmail(email).orElseThrow(() -> new BusinessException("email invalido"));
        repository.delete(user);
    }

    public UserResponseDTO getUser(String email){
        User user = repository.findByEmail(email).orElseThrow(() -> new BusinessException("email invalido"));
        return mapper.toResponse(user);
    }
}
