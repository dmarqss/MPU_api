package com.dmaqrss.mpu_api;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class MpuApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MpuApiApplication.class, args);
	}

}
