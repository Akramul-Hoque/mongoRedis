package com.example.mongoRedis.dashboard.service;

import com.example.mongoRedis.dashboard.dto.response.AdminDashboardResponse;
import com.example.mongoRedis.dashboard.dto.response.StudentDashboardResponse;
import com.example.mongoRedis.dashboard.dto.response.TeacherDashboardResponse;

public interface DashboardService {
    AdminDashboardResponse getAdminDashboard();
    TeacherDashboardResponse getTeacherDashboard();
    StudentDashboardResponse getStudentDashboard();
}
