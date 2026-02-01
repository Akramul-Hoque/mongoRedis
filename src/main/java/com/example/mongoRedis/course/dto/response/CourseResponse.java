package com.example.mongoRedis.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    
    private String id;
    private String courseCode;
    private String name;
    private String description;
    private String academicYear;
    private String semester;
    private int credits;
    private String department;
    
    // Teacher information
    private String teacherId;
    private String teacherName;
    
    // Enrollment information
    private List<String> enrolledStudentIds;
    private int maxCapacity;
    private int currentEnrollment;
    private int availableSlots;
    
    // Schedule information
    private String schedule;
    private String room;
    private String timeSlot;
    private List<String> daysOfWeek;
    
    // Course details
    private String grade;
    private String section;
    private String subjectCategory;
    private String level;
    
    // Status and dates
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    // Course materials
    private String syllabus;
    private String prerequisites;
    private String gradingPolicy;
    
    // Statistics
    private double averageGrade;
    private int completedStudents;
    private int droppedStudents;
    
    // Additional information
    private boolean isEnrollmentOpen;
    private String status; // UPCOMING, ONGOING, COMPLETED, CANCELLED
    private List<StudentSummary> enrolledStudents;
    private TeacherSummary teacher;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentSummary {
        private String id;
        private String name;
        private String studentId;
        private String email;
        private String enrollmentDate;
        private String status; // ENROLLED, COMPLETED, DROPPED
        private Double currentGrade;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeacherSummary {
        private String id;
        private String name;
        private String email;
        private String employeeId;
        private String department;
        private int assignedCourses;
    }
}
