package com.example.mongoRedis.user.controller;

import com.example.mongoRedis.common.response.ApiResponse;
import com.example.mongoRedis.user.dto.request.PasswordChangeRequest;
import com.example.mongoRedis.user.dto.request.ProfileUpdateRequest;
import com.example.mongoRedis.user.dto.response.UserResponse;
import com.example.mongoRedis.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'STAFF')")
    public ApiResponse<UserResponse> getCurrentUserProfile() {
        UserResponse user = userService.getCurrentUserProfile();
        return new ApiResponse<>(true, user, "Profile retrieved successfully");
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'STAFF')")
    public ApiResponse<UserResponse> updateProfile(@Valid @RequestBody ProfileUpdateRequest request) {
        UserResponse user = userService.updateCurrentUserProfile(request);
        return new ApiResponse<>(true, user, "Profile updated successfully");
    }

    @PutMapping("/password")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'STAFF')")
    public ApiResponse<Void> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(request);
        return new ApiResponse<>(true, null, "Password changed successfully");
    }

    @GetMapping("/dashboard-info")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'STAFF')")
    public ApiResponse<ProfileDashboardResponse> getProfileDashboardInfo() {
        ProfileDashboardResponse dashboardInfo = userService.getProfileDashboardInfo();
        return new ApiResponse<>(true, dashboardInfo, "Profile dashboard info retrieved successfully");
    }

    // DTO for profile dashboard response
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ProfileDashboardResponse {
        private String userName;
        private String userEmail;
        private String userRole;
        private String lastLoginDate;
        private int profileCompletionPercentage;
        private boolean hasPendingApplications;
        private int unreadNotifications;
        private String academicYear;
        private String departmentOrGrade;
    }
}
