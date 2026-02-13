package com.dmaqrss.mpu_api.service;

import com.dmaqrss.mpu_api.dto.user.ResetPasswordDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private void sendEmailBase(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @RabbitListener(queues = "resetPassword.email.queue")
    private void sendRecoverPasswordEmail(ResetPasswordDTO data){
        sendEmailBase(data.email(), "reset de senha", "clique " + data.link());
    }
}
