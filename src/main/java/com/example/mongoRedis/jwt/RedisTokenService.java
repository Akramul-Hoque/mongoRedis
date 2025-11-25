package com.example.mongoRedis.jwt;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final long ACCESS_TOKEN_EXPIRY = 15; // minutes
    private static final long REFRESH_TOKEN_EXPIRY = 7; // days

    public RedisTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Store refresh token with TTL
    public void storeRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set("refresh:" + userId, refreshToken, REFRESH_TOKEN_EXPIRY, TimeUnit.DAYS);
    }

    // Store access token with TTL
    public void storeAccessToken(String userId, String accessToken) {
        redisTemplate.opsForValue().set("access:" + userId, accessToken, ACCESS_TOKEN_EXPIRY, TimeUnit.MINUTES);
    }

    public boolean isRefreshTokenValid(String userId, String refreshToken) {
        String storedToken = redisTemplate.opsForValue().get("refresh:" + userId);
        return storedToken != null && storedToken.equals(refreshToken);
    }

    public boolean isAccessTokenValid(String userId, String accessToken) {
        String storedToken = redisTemplate.opsForValue().get("access:" + userId);
        return storedToken != null && storedToken.equals(accessToken);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete("refresh:" + userId);
    }

    public void deleteAccessToken(String userId) {
        redisTemplate.delete("access:" + userId);
    }

}
