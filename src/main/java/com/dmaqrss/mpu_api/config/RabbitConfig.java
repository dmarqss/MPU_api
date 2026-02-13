package com.dmaqrss.mpu_api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "email.exchange";

    public static final String RESETPASSWORD_EMAIL_QUEUE = "resetPassword.email.queue";

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
    public Queue createPaymentqueue(){
        return new Queue(RESETPASSWORD_EMAIL_QUEUE, true);
    }


    @Bean
    public Binding resetPasswordEmailBinding(Queue resetPasswordQueue, DirectExchange exchange){
        return BindingBuilder.bind(resetPasswordQueue)
                .to(exchange)
                .with("resetPassword.email");
    }


}
