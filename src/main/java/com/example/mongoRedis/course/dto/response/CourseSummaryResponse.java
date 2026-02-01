package com.example.mongoRedis.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseSummaryResponse {
    
    private String id;
    private String courseCode;
    private String name;
    private String academicYear;
    private String semester;
    private int credits;
    private String department;
    
    // Teacher information
    private String teacherId;
    private String teacherName;
    
    // Enrollment information
    private int currentEnrollment;
    private int maxCapacity;
    private int availableSlots;
    
    // Schedule information
    private String schedule;
    private String room;
    private String timeSlot;
    
    // Course details
    private String grade;
    private String section;
    private String level;
    
    // Status
    private boolean isActive;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    // Quick stats
    private double averageGrade;
    private int completedStudents;
}
