package com.dmaqrss.mpu_api.service.user;

import com.dmaqrss.mpu_api.dto.user.ResetPasswordDTO;
import com.dmaqrss.mpu_api.service.email.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordEmailService {
    @Autowired
    EmailService emailService;

    @RabbitListener(queues = "resetPassword.email.queue")
    private void sendRecoverPasswordEmail(ResetPasswordDTO data){
        emailService.sendEmail(data.email(), "reset de senha", "clique " + data.link());
    }
}
