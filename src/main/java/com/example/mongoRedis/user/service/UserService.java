package com.example.mongoRedis.user.service;

import com.example.mongoRedis.user.dto.request.UserRequest;
import com.example.mongoRedis.user.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);
    UserResponse getUserById(String id);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(String id, UserRequest request);
    void deleteUser(String id);
}
