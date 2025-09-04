package com.example.mongoRedis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MongoRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongoRedisApplication.class, args);
	}

}
