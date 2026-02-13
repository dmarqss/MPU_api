package com.dmaqrss.mpu_api.publisher;

import com.dmaqrss.mpu_api.dto.user.ResetPasswordDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordEmailPublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void resetPasswordEmail(ResetPasswordDTO data){
        rabbitTemplate.convertAndSend("email.exchange","resetPassword.email", data);
    }
}
