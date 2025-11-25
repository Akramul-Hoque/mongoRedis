package com.example.mongoRedis.auth.service;

import com.example.mongoRedis.auth.dto.request.LoginRequest;
import com.example.mongoRedis.auth.dto.request.RefreshTokenRequest;
import com.example.mongoRedis.auth.dto.request.SignupRequest;
import com.example.mongoRedis.auth.dto.response.LoginResponse;
import com.example.mongoRedis.auth.dto.response.RefreshTokenResponse;
import com.example.mongoRedis.common.response.ApiResponse;
import com.example.mongoRedis.common.util.PasswordEncoderUtil;
import com.example.mongoRedis.exception.CustomServiceException;
import com.example.mongoRedis.jwt.JwtService;
import com.example.mongoRedis.jwt.RedisTokenService;
import com.example.mongoRedis.user.dto.model.Credentials;
import com.example.mongoRedis.user.dto.model.User;
import com.example.mongoRedis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoder;
    private final JwtService jwtService;
    private final RedisTokenService redisTokenService;

    @Override
    public ApiResponse<LoginResponse> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomServiceException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getCredentials().getPassword())) {
            throw new CustomServiceException("Invalid email or password");
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        redisTokenService.storeAccessToken(user.getId(), accessToken);
        redisTokenService.storeRefreshToken(user.getId(), refreshToken);

        LoginResponse response = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return new ApiResponse<>(true, response, "Login successful");
    }

    @Override
    public ApiResponse<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {
        String userId = jwtService.extractUserId(request.getRefreshToken());

        if (!redisTokenService.isRefreshTokenValid(userId, request.getRefreshToken())) {
            throw new CustomServiceException("Invalid refresh token");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomServiceException("User not found"));

        if (!redisTokenService.isRefreshTokenValid(userId, request.getRefreshToken())) {
            throw new CustomServiceException("Invalid or expired refresh token");
        }

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        redisTokenService.storeAccessToken(userId, newAccessToken);
        redisTokenService.storeRefreshToken(userId, newRefreshToken);

        RefreshTokenResponse response = RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();

        return new ApiResponse<>(true, response, "Token refreshed successfully");
    }

    @Override
    public ApiResponse<Void> logout(String token) {
        String userId = jwtService.extractUserId(token);
        redisTokenService.deleteAccessToken(userId);
        redisTokenService.deleteRefreshToken(userId);
        return new ApiResponse<>(true, null, "Successfully logged out");
    }

//    private User mapToUserEntity(SignupRequest request) {
//        Credentials credentials = Credentials.builder()
//                .username(request.getEmail())
//                .password(passwordEncoder.encryptPassword(request.getPassword()))
//                .roles(List.of("ROLE_" + request.getUserType().name()))
//                .build();
//
//        return User.builder()
//                .name(request.getName())
//                .email(request.getEmail())
//                .age(request.getAge())
//                .userType(request.getUserType())
//                .credentials(credentials)
//                .build();
//    }
}
