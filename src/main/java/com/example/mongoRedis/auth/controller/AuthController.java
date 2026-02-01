package com.example.mongoRedis.auth.controller;

import com.example.mongoRedis.auth.dto.request.LoginRequest;
import com.example.mongoRedis.auth.dto.request.RefreshTokenRequest;
import com.example.mongoRedis.auth.dto.response.LoginResponse;
import com.example.mongoRedis.auth.dto.response.RefreshTokenResponse;
import com.example.mongoRedis.auth.service.AuthService;
import com.example.mongoRedis.common.ApiEndpoints.ApiEndpoints;
import com.example.mongoRedis.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(ApiEndpoints.Auth.LOGIN)
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(ApiEndpoints.Auth.REFRESH)
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping(ApiEndpoints.Auth.LOGOUT)
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok(new ApiResponse<>(true, null, "Successfully logged out"));
    }
}