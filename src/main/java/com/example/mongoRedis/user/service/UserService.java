package com.example.mongoRedis.user.service;

import com.example.mongoRedis.common.UserType;
import com.example.mongoRedis.user.controller.AdminUserController.UserStatisticsResponse;
import com.example.mongoRedis.user.controller.ProfileController.ProfileDashboardResponse;
import com.example.mongoRedis.user.dto.request.*;
import com.example.mongoRedis.user.dto.response.UserResponse;
import com.example.mongoRedis.user.dto.response.UserSummaryResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    // Basic CRUD operations
    UserResponse createUser(UserCreateRequest request);
    UserResponse getUserById(String id);
    Page<UserSummaryResponse> getAllUsers(Pageable pageable, UserType userType, Boolean isActive);
    UserResponse updateUser(String id, UserUpdateRequest request);
    void deleteUser(String id);
    
    // User management operations
    void activateUser(String id);
    void deactivateUser(String id);
    List<UserSummaryResponse> getUsersSummary(UserType userType, int limit);
    Page<UserSummaryResponse> searchUsers(String query, Pageable pageable);
    UserStatisticsResponse getUserStatistics();
    
    // Profile management
    UserResponse getCurrentUserProfile();
    UserResponse updateCurrentUserProfile(ProfileUpdateRequest request);
    void changePassword(PasswordChangeRequest request);
    ProfileDashboardResponse getProfileDashboardInfo();
    
    // Legacy methods for backward compatibility
    @Deprecated
    UserResponse createUser(com.example.mongoRedis.user.dto.request.UserRequest request);
    
    @Deprecated
    org.springframework.data.domain.Page<UserResponse> getAllUsers(org.springframework.data.domain.Pageable pageable);
    
    @Deprecated
    UserResponse updateUser(String id, com.example.mongoRedis.user.dto.request.UserRequest request);
}
