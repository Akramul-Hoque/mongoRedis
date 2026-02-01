package com.example.mongoRedis.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDashboardResponse {
    private String teacherName;
    private String employeeId;
    private String department;
    private long totalStudents;
    private long totalCourses;
    private long pendingApplications;
    private int todayClasses;
    private int upcomingClasses;
    
    private List<CourseSummary> assignedCourses;
    private List<StudentSummary> recentStudents;
//    private List<ApplicationSummary> pendingApplications;
    private List<ClassSummary> todaySchedule;
    private NotificationSummary notifications;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseSummary {
        private String id;
        private String name;
        private String code;
        private long enrolledStudents;
        private String grade;
        private String section;
        private String schedule;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentSummary {
        private String id;
        private String name;
        private String studentId;
        private String grade;
        private String section;
        private double averageGrade;
        private int attendanceRate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationSummary {
        private String id;
        private String studentName;
        private String type;
        private String submittedAt;
        private String priority;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClassSummary {
        private String courseId;
        private String courseName;
        private String time;
        private String room;
        private String grade;
        private String section;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationSummary {
        private int total;
        private int unread;
        private List<NotificationItem> recent;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationItem {
        private String id;
        private String message;
        private String type;
        private String createdAt;
        private boolean isRead;
    }
}
