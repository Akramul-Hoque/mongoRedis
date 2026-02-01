package com.example.mongoRedis.user.controller;

import com.example.mongoRedis.common.response.ApiResponse;
import com.example.mongoRedis.common.UserType;
import com.example.mongoRedis.user.dto.request.UserCreateRequest;
import com.example.mongoRedis.user.dto.request.UserUpdateRequest;
import com.example.mongoRedis.user.dto.response.UserResponse;
import com.example.mongoRedis.user.dto.response.UserSummaryResponse;
import com.example.mongoRedis.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse user = userService.createUser(request);
        return new ApiResponse<>(true, user, "User created successfully");
    }

    @GetMapping
    public ApiResponse<Page<UserSummaryResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) UserType userType,
            @RequestParam(required = false) Boolean isActive) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<UserSummaryResponse> users = userService.getAllUsers(pageable, userType, isActive);
        return new ApiResponse<>(true, users, "Users retrieved successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String id) {
        UserResponse user = userService.getUserById(id);
        return new ApiResponse<>(true, user, "User retrieved successfully");
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String id, 
                                             @Valid @RequestBody UserUpdateRequest request) {
        UserResponse user = userService.updateUser(id, request);
        return new ApiResponse<>(true, user, "User updated successfully");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ApiResponse<>(true, null, "User deleted successfully");
    }

    @PatchMapping("/{id}/activate")
    public ApiResponse<Void> activateUser(@PathVariable String id) {
        userService.activateUser(id);
        return new ApiResponse<>(true, null, "User activated successfully");
    }

    @PatchMapping("/{id}/deactivate")
    public ApiResponse<Void> deactivateUser(@PathVariable String id) {
        userService.deactivateUser(id);
        return new ApiResponse<>(true, null, "User deactivated successfully");
    }

    @GetMapping("/summary")
    public ApiResponse<List<UserSummaryResponse>> getUsersSummary(
            @RequestParam(required = false) UserType userType,
            @RequestParam(defaultValue = "50") int limit) {
        
        List<UserSummaryResponse> users = userService.getUsersSummary(userType, limit);
        return new ApiResponse<>(true, users, "Users summary retrieved successfully");
    }

    @GetMapping("/search")
    public ApiResponse<Page<UserSummaryResponse>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<UserSummaryResponse> users = userService.searchUsers(query, pageable);
        return new ApiResponse<>(true, users, "Search results retrieved successfully");
    }

    @GetMapping("/statistics")
    public ApiResponse<UserStatisticsResponse> getUserStatistics() {
        UserStatisticsResponse statistics = userService.getUserStatistics();
        return new ApiResponse<>(true, statistics, "User statistics retrieved successfully");
    }

    // DTO for statistics response
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class UserStatisticsResponse {
        private long totalUsers;
        private long totalStudents;
        private long totalTeachers;
        private long totalStaff;
        private long totalAdmins;
        private long activeUsers;
        private long inactiveUsers;
        private long newUsersThisMonth;
        private long newUsersThisYear;
    }
}
