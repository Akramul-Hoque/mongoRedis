package com.example.mongoRedis.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {
    private long totalStudents;
    private long totalTeachers;
    private long totalStaff;
    private BigDecimal totalFeesCollected;
    private BigDecimal pendingFees;
    private long pendingApplications;
    private long activeCourses;
    private long totalCourses;
    
    private List<UserSummary> recentUsers;
    private List<ApplicationSummary> recentApplications;
    private List<CourseSummary> popularCourses;
    private FinancialSummary financialSummary;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSummary {
        private String id;
        private String name;
        private String email;
        private String userType;
        private String createdAt;
        private boolean isActive;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationSummary {
        private String id;
        private String studentName;
        private String type;
        private String status;
        private String submittedAt;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseSummary {
        private String id;
        private String name;
        private String code;
        private long enrolledStudents;
        private String teacherName;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FinancialSummary {
        private BigDecimal thisMonth;
        private BigDecimal lastMonth;
        private BigDecimal thisYear;
        private int paymentCount;
    }
}
