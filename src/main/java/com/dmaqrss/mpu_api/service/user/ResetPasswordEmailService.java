package com.dmaqrss.mpu_api.service.user;

import com.dmaqrss.mpu_api.dto.user.ResetPasswordDTO;
import com.dmaqrss.mpu_api.exception.BusinessException;
import com.dmaqrss.mpu_api.service.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ResetPasswordEmailService {
    @Autowired
    EmailService emailService;

    @RabbitListener(queues = "resetPassword.email.queue")
    private void sendRecoverPasswordEmail(ResetPasswordDTO data){
        log.info("[USER] Pedido de reset para o email {} escutado com sucesso", data.email());
        try {
            emailService.sendEmail(data.email(), "reset de senha", "clique " + data.link());
            log.info("[USER] Email enviado com sucesso para: {}", data.email());
        }catch (Exception e){
            log.error("[USER] Falha ao enviar email para: {}", data.email(), e);
            throw new BusinessException("falha ao enviar email");
        }
    }
}
