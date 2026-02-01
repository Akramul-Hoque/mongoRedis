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
public class StudentDashboardResponse {
    private String studentName;
    private String studentId;
    private String grade;
    private String section;
    private String academicYear;
    
    private List<CourseSummary> currentCourses;
    private GradeSummary grades;
    private AttendanceSummary attendance;
    private FeeSummary feeStatus;
    private List<ApplicationSummary> applications;
    private NotificationSummary notifications;
    private List<AnnouncementSummary> announcements;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseSummary {
        private String id;
        private String name;
        private String code;
        private String teacherName;
        private String schedule;
        private String room;
        private double currentGrade;
        private int attendanceRate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GradeSummary {
        private double overallGPA;
        private double thisSemesterGPA;
        private int totalSubjects;
        private int passedSubjects;
        private int failedSubjects;
        private List<SubjectGrade> subjectGrades;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectGrade {
        private String subjectName;
        private double grade;
        private String status; // PASS, FAIL, PENDING
        private String teacherName;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttendanceSummary {
        private int totalDays;
        private int presentDays;
        private int absentDays;
        private int lateDays;
        private double attendanceRate;
        private int consecutiveAbsent;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeeSummary {
        private BigDecimal totalFees;
        private BigDecimal paidFees;
        private BigDecimal pendingFees;
        private BigDecimal overdueFees;
        private String nextPaymentDue;
        private List<PaymentRecord> paymentHistory;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentRecord {
        private String id;
        private BigDecimal amount;
        private String paymentDate;
        private String method;
        private String status;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationSummary {
        private String id;
        private String type;
        private String status;
        private String submittedAt;
        private String lastUpdated;
        private String response;
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
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnnouncementSummary {
        private String id;
        private String title;
        private String message;
        private String postedBy;
        private String postedAt;
        private String priority;
    }
}
