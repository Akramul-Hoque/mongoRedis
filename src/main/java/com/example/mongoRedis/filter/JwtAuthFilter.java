package com.example.mongoRedis.filter;

import com.example.mongoRedis.common.response.ApiResponse;
import com.example.mongoRedis.jwt.JwtService;
import com.example.mongoRedis.jwt.RedisTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RedisTokenService redisTokenService;
    private final ObjectMapper objectMapper;

    public JwtAuthFilter(JwtService jwtService, RedisTokenService redisTokenService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.redisTokenService = redisTokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip JWT for auth endpoints and Swagger UI / OpenAPI
        if (isWhitelisted(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendApiError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Missing or invalid Authorization header");
            return;
        }

        try {
            String token = authHeader.substring(7);
            String userId = jwtService.extractUserId(token);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (!redisTokenService.isAccessTokenValid(userId, token)) {
                    sendApiError(response, HttpServletResponse.SC_UNAUTHORIZED,
                            "Token is not valid or has been revoked");
                    return;
                }

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userId, null, null);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (Exception e) {
            sendApiError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Authentication failed: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    // Only check if the path contains keywords for whitelisted endpoints
    private boolean isWhitelisted(String path) {
        return path.contains("/auth/") || path.contains("/swagger") || path.contains("/v3/api-docs") || path.contains("/webjars") || path.contains("/actuator");
    }

    private void sendApiError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<Void> apiResponse = new ApiResponse<>(false, null, message);
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
