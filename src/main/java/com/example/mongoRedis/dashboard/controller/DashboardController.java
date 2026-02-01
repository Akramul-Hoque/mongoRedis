package com.example.mongoRedis.dashboard.controller;

import com.example.mongoRedis.common.response.ApiResponse;
import com.example.mongoRedis.dashboard.dto.response.AdminDashboardResponse;
import com.example.mongoRedis.dashboard.dto.response.StudentDashboardResponse;
import com.example.mongoRedis.dashboard.dto.response.TeacherDashboardResponse;
import com.example.mongoRedis.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AdminDashboardResponse> getAdminDashboard() {
        AdminDashboardResponse dashboard = dashboardService.getAdminDashboard();
        return new ApiResponse<>(true, dashboard, "Admin dashboard retrieved successfully");
    }

    @GetMapping("/teacher")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ApiResponse<TeacherDashboardResponse> getTeacherDashboard() {
        TeacherDashboardResponse dashboard = dashboardService.getTeacherDashboard();
        return new ApiResponse<>(true, dashboard, "Teacher dashboard retrieved successfully");
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<StudentDashboardResponse> getStudentDashboard() {
        StudentDashboardResponse dashboard = dashboardService.getStudentDashboard();
        return new ApiResponse<>(true, dashboard, "Student dashboard retrieved successfully");
    }
}
