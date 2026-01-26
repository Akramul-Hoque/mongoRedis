package com.example.mongoRedis.user.service;

import com.example.mongoRedis.user.dto.request.UserRequest;
import com.example.mongoRedis.user.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);

    UserResponse getUserById(String id);

    org.springframework.data.domain.Page<UserResponse> getAllUsers(org.springframework.data.domain.Pageable pageable);

    UserResponse updateUser(String id, UserRequest request);

    void deleteUser(String id);
}
