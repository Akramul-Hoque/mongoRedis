package com.example.mongoRedis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

        @Value("${spring.data.redis.host:localhost}")
        private String redisHost;

        @Value("${spring.data.redis.port:6379}")
        private int redisPort;

        @Bean
        public LettuceConnectionFactory redisConnectionFactory() {
                RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
                redisConfig.setHostName(redisHost);
                redisConfig.setPort(redisPort);
                return new LettuceConnectionFactory(redisConfig);
        }

        @Bean
        public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
                RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(10))
                                .serializeValuesWith(
                                                RedisSerializationContext.SerializationPair.fromSerializer(
                                                                new GenericJackson2JsonRedisSerializer()));

                java.util.Map<String, RedisCacheConfiguration> cacheConfigurations = new java.util.HashMap<>();

                // 30 minutes for user data
                cacheConfigurations.put("users", defaultConfig.entryTtl(Duration.ofMinutes(30)));

                // 1 hour for roles or metadata
                cacheConfigurations.put("roles", defaultConfig.entryTtl(Duration.ofHours(1)));

                return RedisCacheManager.builder(connectionFactory)
                                .cacheDefaults(defaultConfig)
                                .withInitialCacheConfigurations(cacheConfigurations)
                                .build();
        }
}
