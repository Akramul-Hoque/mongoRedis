package com.example.mongoRedis.course.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "course_enrollments")
public class CourseEnrollment {
    
    @Id
    private String id;
    
    private String courseId;
    private String courseName;
    private String courseCode;
    private String studentId;
    private String studentName;
    private String teacherId;
    private String teacherName;
    
    private String academicYear;
    private String semester;
    
    // Enrollment status
    private EnrollmentStatus status;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;
    private LocalDateTime droppedAt;
    
    // Academic performance
    private double finalGrade;
    private String gradeLetter;
    private boolean passed;
    private int attendancePercentage;
    
    // Enrollment details
    private String enrollmentType; // REGULAR, AUDIT, EXTRA_CREDIT
    private String dropReason;
    private String notes;
    
    // Course information
    private int credits;
    private String department;
    private String schedule;
    private String room;
    
    public enum EnrollmentStatus {
        ENROLLED,
        COMPLETED,
        DROPPED,
        IN_PROGRESS,
        SUSPENDED
    }
}
