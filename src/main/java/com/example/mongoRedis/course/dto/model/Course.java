package com.example.mongoRedis.course.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "courses")
public class Course {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String courseCode;
    
    @Indexed
    private String name;
    
    private String description;
    
    private String academicYear;
    
    private String semester;
    
    private int credits;
    
    private String department;
    
    // Teacher assignment
    private String teacherId;
    private String teacherName;
    
    // Student enrollment
    private List<String> enrolledStudentIds;
    private int maxCapacity;
    private int currentEnrollment;
    
    // Schedule information
    private String schedule;
    private String room;
    private String timeSlot;
    private List<String> daysOfWeek;
    
    // Course metadata
    private String grade;
    private String section;
    private String subjectCategory;
    private String level; // BEGINNER, INTERMEDIATE, ADVANCED
    
    // Status and tracking
    private boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    // Course materials and resources
    private List<String> materialIds;
    private String syllabus;
    private String prerequisites;
    
    // Assessment information
    private String gradingPolicy;
    private List<String> assignmentIds;
    private List<String> examIds;
    
    // Statistics
    private double averageGrade;
    private int completedStudents;
    private int droppedStudents;
}
