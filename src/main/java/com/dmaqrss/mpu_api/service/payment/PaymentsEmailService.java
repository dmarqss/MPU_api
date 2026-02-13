package com.dmaqrss.mpu_api.service.payment;

import com.dmaqrss.mpu_api.dto.payment.PaymentEmailDTO;
import com.dmaqrss.mpu_api.service.email.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentsEmailService {

    @Autowired
    EmailService emailService;

    @RabbitListener(queues = "payment.confirmed.email.queue")
    public void paymentConfirmed(PaymentEmailDTO payment){
        emailService.sendEmail(payment.email(), "mpu_api", "O pagamento numero: " + payment.id() + " confirmado");
    }

    @RabbitListener(queues = "payment.failed.email.queue")
    public void paymentFailed(PaymentEmailDTO payment){
        emailService.sendEmail(payment.email(), "mpu_api", "O pagamento numero: " + payment.id() + " falhou");
    }
}
