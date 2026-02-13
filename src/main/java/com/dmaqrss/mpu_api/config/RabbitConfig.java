package com.dmaqrss.mpu_api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "email.exchange";

    public static final String RESETPASSWORD_EMAIL_QUEUE = "resetPassword.email.queue";

    public static final String PAYMENT_CONFIRMED_EMAIL_QUEUE = "payment.confirmed.email.queue";

    public static final String PAYMENT_FAILED_EMAIL_QUEUE = "payment.failed.email.queue";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue resetPasswordEmailqueue(){
        return new Queue(RESETPASSWORD_EMAIL_QUEUE, true);
    }

    @Bean
    public Queue paymentConfirmdQueue(){
        return new Queue(PAYMENT_CONFIRMED_EMAIL_QUEUE);
    }

    @Bean
    public Queue paymentFailedQueue(){
        return new Queue(PAYMENT_FAILED_EMAIL_QUEUE);
    }

    @Bean
    public Binding resetPasswordEmailBinding(@Qualifier("resetPasswordEmailqueue") Queue queue, DirectExchange exchange){
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with("resetPassword.email");
    }

    @Bean
    public Binding paymentConfirmedEmailBinding(@Qualifier("paymentConfirmdQueue") Queue queue, DirectExchange exchange){
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with("payment.confirmed");
    }

    @Bean
    public Binding paymentFailedEmailBinding(@Qualifier("paymentFailedQueue") Queue queue, DirectExchange exchange){
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with("payment.failed");
    }


}
