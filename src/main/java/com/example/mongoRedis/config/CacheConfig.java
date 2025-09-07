package com.example.mongoRedis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.Optional;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

    @Bean
    @Primary
    public CacheManager cacheManager(Optional<RedisConnectionFactory> redisConnectionFactory) {
        return redisConnectionFactory.map(factory -> {
            try {
                factory.getConnection().ping();
                log.info("Redis is available. Using RedisCacheManager.");
                RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer()))
                        .entryTtl(Duration.ofMinutes(30))
                        .disableCachingNullValues();

                return RedisCacheManager.builder(factory)
                        .cacheDefaults(cacheConfig)
                        .build();
            } catch (Exception e) {
                log.warn("Redis not available. Falling back to in-memory cache.", e);
                return new ConcurrentMapCacheManager();
            }
        }).orElseGet(() -> {
            log.warn("RedisConnectionFactory not configured. Using in-memory cache.");
            return new ConcurrentMapCacheManager();
        });
    }
}