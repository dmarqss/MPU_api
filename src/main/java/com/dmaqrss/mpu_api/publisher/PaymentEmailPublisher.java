package com.dmaqrss.mpu_api.publisher;

import com.dmaqrss.mpu_api.dto.payment.PaymentEmailDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentEmailPublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void paymentConfirmedEmail(PaymentEmailDTO payment){
        rabbitTemplate.convertAndSend("email.exchange","payment.confirmed",payment);
    }

    public void paymentFailedEmail(PaymentEmailDTO payment){
        rabbitTemplate.convertAndSend("email.exchange","payment.failed",payment);
    }
}
