package com.example.mongoRedis.filter;

import com.example.mongoRedis.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final int MAX_REQUESTS_PER_MINUTE = 60;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        String key = "ratelimit:" + ip;

        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1) {
            redisTemplate.expire(key, 1, TimeUnit.MINUTES);
        }

        if (count != null && count > MAX_REQUESTS_PER_MINUTE) {
            sendApiError(response, 429, "Too many requests. Please try again later.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendApiError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<Void> apiResponse = new ApiResponse<>(false, null, message);
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
