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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
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
        log.info("[USER] Registrando novo usuario: {}", dto.email());
        if(repository.existsByEmail(dto.email())){
            log.warn("[USER] Tentativa de registro com email ja existente: {}", dto.email());
            throw new BusinessException("O email já existe");
        }

        String encryptedPassword = passwordEncoder.encode(dto.password());
        User user = mapper.toEntity(dto);
        user.setPassword(encryptedPassword);
        user.setRole(UserRoles.USER);

        repository.save(user);
        log.info("[USER] Registrado com sucesso: {}", user.getEmail());
        return mapper.toResponse(user);
    }

    public UserResponseDTO updateRole(String email, UserRoleDTO role){
        log.info("[USER] Update de role para o user: {}", email);
        User user = repository.findByEmail(email).orElseThrow(() ->{
            log.warn("[USER] Tentativa de update no email inexistente: {}", email);
            return new BusinessException("O email não existe");
        });
        user.setRole(role.role());
        repository.save(user);
        log.info("[USER] Role atualizada para {} no usuário: {}", role.role(), email);
        return mapper.toResponse(user);
    }

    public void sendResetPasswordEmail(String email){
        log.info("[USER] Solicitação para reset de senha no email: {}", email);
        Optional<User> user = repository.findByEmail(email);

        if (user.isEmpty()) {
            log.warn("[USER] Reset solicitado para email não cadastrado: {}", email);
            return;
        }

        String token = tokenService.generateResetToken(email);
        String link = "http://localhost:8080/user/reset-password?token=" + token;
        publisher.resetPasswordEmail(new ResetPasswordDTO(link, email));
        log.info("[USER] Email de reset enviado para a queue");
    }

    public void resetPassword(String token, String newPassword){
        String email = tokenService.validateResetToken(token);
        log.info("[USER] Reset de senha iniciado: {}", email);

        Optional<User> user = repository.findByEmail(email);

        if(user.isEmpty()){
            log.warn("[USER] Token válido mas usuário não encontrado: {}", email);
            return;
        }

        User data = user.get();
        data.setPassword(passwordEncoder.encode(newPassword));
        repository.save(data);
        log.info("[USER] Senha redefinida com sucesso: {}", email);
    }

    public void delete(String email){
        log.info("[USER] Iniciando delete do usuario: {}", email);
        User user = repository.findByEmail(email).orElseThrow(() -> {
            log.warn("[USER] Usuario não existente para o delete: {}", email);
            return new BusinessException("email invalido");
        });

        repository.delete(user);
        log.info("[USER] Usuario deletado com sucesso: {}", email);
    }

    public UserResponseDTO getUser(String email){
        log.info("[USER] Get User solicitado: {}", email);
        User user = repository.findByEmail(email).orElseThrow(() -> {
            log.warn("[USER] Usuario não existente para o retorno: {}", email);
            return new BusinessException("email invalido");
        });
        log.info("[USER] Retornando usuario com sucesso: {}", user.getEmail());
        return mapper.toResponse(user);
    }
}
