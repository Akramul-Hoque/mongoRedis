package com.example.mongoRedis.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final long REFRESH_TOKEN_EXPIRY = 7;

    public void storeRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set("refresh:" + userId, refreshToken, REFRESH_TOKEN_EXPIRY, TimeUnit.DAYS);
    }

    public boolean isRefreshTokenValid(String userId, String refreshToken) {
        String storedToken = redisTemplate.opsForValue().get("refresh:" + userId);
        return storedToken != null && storedToken.equals(refreshToken);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete("refresh:" + userId);
    }

    public void storeAccessToken(String userId, String accessToken, long duration, TimeUnit unit) {
        redisTemplate.opsForValue().set("access:" + userId, accessToken, duration, unit);
    }

    public boolean isAccessTokenValid(String userId, String accessToken) {
        String storedToken = redisTemplate.opsForValue().get("access:" + userId);
        return storedToken != null && storedToken.equals(accessToken);
    }

    public void deleteAccessToken(String userId) {
        redisTemplate.delete("access:" + userId);
    }

}
