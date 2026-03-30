package com.dmaqrss.mpu_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MpuApiApplicationTests {

	@MockBean
	JavaMailSender mailSender;

	@MockBean
	RedisConnectionFactory redisConnectionFactory;

	@MockBean
	RedisCacheManager cacheManager;

	@Test
	void contextLoads() {
	}


}
