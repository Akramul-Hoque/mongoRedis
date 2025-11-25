package com.example.mongoRedis.user.controller;

import com.example.mongoRedis.common.ApiEndpoints.ApiEndpoints;
import com.example.mongoRedis.common.response.ApiResponse;
import com.example.mongoRedis.user.dto.request.UserRequest;
import com.example.mongoRedis.user.dto.response.UserResponse;
import com.example.mongoRedis.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(ApiEndpoints.User.CREATE)
    public ApiResponse<UserResponse> createUser(@RequestBody UserRequest request) {
        return new ApiResponse<>(true, userService.createUser(request), "User created successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String id) {
        return new ApiResponse<>(true, userService.getUserById(id), "User retrieved successfully");
    }

    @GetMapping(ApiEndpoints.User.GET_ALL)
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return new ApiResponse<>(true, userService.getAllUsers(), "Users retrieved successfully");
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String id, @RequestBody UserRequest request) {
        return new ApiResponse<>(true, userService.updateUser(id, request), "User updated successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ApiResponse<>(true, null, "User deleted successfully");
    }
}
