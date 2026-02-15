package com.dmaqrss.mpu_api.service.user;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper mapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    TokenService tokenService;

    @Mock
    PasswordEmailPublisher publisher;

    @InjectMocks
    UserService userService;

    @Test
    void shouldRegisterUserSuccessfully(){
        UserRegisterDTO dto = new UserRegisterDTO("test", "test@test.com", "test123");
        User user = new User("test", "test@test.com", "test1234", UserRoles.USER);

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode(dto.password())).thenReturn("test1234");
        when(mapper.toEntity(dto)).thenReturn(user);
        when(mapper.toResponse(user)).thenReturn(new UserResponseDTO(user.getName(), user.getEmail(), user.getRole()));

        UserResponseDTO response = userService.register(dto);

        verify(passwordEncoder).encode(dto.password());
        assertEquals("test", response.name());
        assertEquals("test@test.com", response.email());
        verify(userRepository).save(any(User.class));

    }

    @Test
    void shouldThrowExceptionOnRegisterWhenEmailAlreadyExist() {
        UserRegisterDTO dto = new UserRegisterDTO("test", "test@test.com", "test123");

        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(BusinessException.class, () -> userService.register(dto));
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldUpdateUserRoleSucessfully(){
        User user = new User("test", "test@test.com", "test1234", UserRoles.USER);

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(mapper.toResponse(any(User.class)))
                .thenAnswer(invocation -> {
                    User u = invocation.getArgument(0);
                    return new UserResponseDTO(u.getName(), u.getEmail(), u.getRole());
                });

        UserResponseDTO response = userService.updateRole("test@test.com", new UserRoleDTO(UserRoles.ADMIN));

        assertEquals(UserRoles.ADMIN, user.getRole());
        assertEquals(UserRoles.ADMIN, response.role());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionOnUpdateRoleWhenEmailNotExist(){
        String email = "test@test.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> userService.updateRole("test@test.com", new UserRoleDTO(UserRoles.ADMIN)));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldSendResetPasswordEmailSucessfully(){
        String email = "teste@test.com";
        User user = new User();
        String token = "123456";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(tokenService.generateResetToken(email)).thenReturn(token);

        userService.sendResetPasswordEmail(email);

        verify(tokenService).generateResetToken(email);
        verify(publisher).resetPasswordEmail(
                argThat(dto ->
                        dto.email().equals(email) && dto.link().contains(token)
                ));
    }

    @Test
    void shouldNotSendResetPasswordEmailWhenEmailNotExist(){
        String email = "teste@test.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        userService.sendResetPasswordEmail(email);

        verify(tokenService, never()).generateResetToken(any());
        verify(publisher, never()).resetPasswordEmail(any());
    }

    @Test
    void shouldResetPasswordSucessfully(){
        String email = "test@test.com";
        User user = new User();
        String token = "token";
        String password = "test123";

        when(tokenService.validateResetToken(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(password)).thenReturn("encodePassword");

        userService.resetPassword(token, password);

        verify(tokenService).validateResetToken(token);
        verify(passwordEncoder).encode(password);
        assertEquals("encodePassword", user.getPassword());
        verify(userRepository).save(user);
    }
    @Test
    void shouldNotResetPasswordWhenEmailNotExist(){
        String email = "test@test.com";
        User user = new User();
        String token = "token";
        String password = "test123";

        when(tokenService.validateResetToken(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());


        userService.resetPassword(token, password);

        verify(tokenService).validateResetToken(token);
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldDeleteUserSucessfully(){
        String email = "test@test.com";
        User user = new User();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        userService.delete(email);

        verify(userRepository).delete(user);
    }

    @Test
    void shouldThrowExceptionOnDeleteUserWhenEmailNotExist(){
        String email = "test@test.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> userService.delete(email));

        verify(userRepository, never()).delete(any());
    }

    @Test
    void shouldGetUserSucessfully(){
        User user = new User("test", "test@test.com", "test1234", UserRoles.USER);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mapper.toResponse(user)).thenReturn(new UserResponseDTO(user.getName(), user.getEmail(), user.getRole()));

        UserResponseDTO response = userService.getUser(user.getEmail());

        assertEquals("test", response.name());
        assertEquals("test@test.com", response.email());
        assertEquals(UserRoles.USER, response.role());
    }

    @Test
    void shouldThrowExceptionOnGetUserEmailNotExist(){
        String email = "test@test.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> userService.getUser(email));
        verify(mapper, never()).toResponse(any());
    }
}
