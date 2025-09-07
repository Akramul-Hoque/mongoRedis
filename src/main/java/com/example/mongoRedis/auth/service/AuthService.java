package com.example.mongoRedis.auth.service;

import com.example.mongoRedis.auth.dto.request.LoginRequest;
import com.example.mongoRedis.auth.dto.request.RefreshTokenRequest;
import com.example.mongoRedis.auth.dto.request.SignupRequest;
import com.example.mongoRedis.auth.dto.response.LoginResponse;
import com.example.mongoRedis.auth.dto.response.RefreshTokenResponse;
import com.example.mongoRedis.common.response.ApiResponse;

public interface AuthService {
    ApiResponse<Void> signup(SignupRequest request);

    ApiResponse<LoginResponse> login(LoginRequest request);

    ApiResponse<RefreshTokenResponse> refreshToken(RefreshTokenRequest request);

    ApiResponse<Void> logout(String token);
}
