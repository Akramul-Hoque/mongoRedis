package com.example.mongoRedis.course.dto.request;

import jakarta.validation.constraints.*;
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
public class CourseCreateRequest {
    
    @NotBlank(message = "Course code is required")
    @Size(min = 3, max = 20, message = "Course code must be between 3 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Course code must contain only uppercase letters and numbers")
    private String courseCode;
    
    @NotBlank(message = "Course name is required")
    @Size(min = 5, max = 200, message = "Course name must be between 5 and 200 characters")
    private String name;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @NotBlank(message = "Academic year is required")
    private String academicYear;
    
    @NotBlank(message = "Semester is required")
    @Pattern(regexp = "^(FALL|SPRING|SUMMER|WINTER)$", message = "Semester must be FALL, SPRING, SUMMER, or WINTER")
    private String semester;
    
    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 10, message = "Credits must not exceed 10")
    private Integer credits;
    
    @NotBlank(message = "Department is required")
    private String department;
    
    // Teacher assignment
    @NotBlank(message = "Teacher ID is required")
    private String teacherId;
    
    // Schedule information
    @Size(max = 500, message = "Schedule must not exceed 500 characters")
    private String schedule;
    
    @Size(max = 50, message = "Room must not exceed 50 characters")
    private String room;
    
    @Size(max = 100, message = "Time slot must not exceed 100 characters")
    private String timeSlot;
    
    private List<String> daysOfWeek;
    
    // Course details
    private String grade;
    private String section;
    private String subjectCategory;
    
    @Pattern(regexp = "^(BEGINNER|INTERMEDIATE|ADVANCED)$", message = "Level must be BEGINNER, INTERMEDIATE, or ADVANCED")
    private String level;
    
    // Capacity
    @NotNull(message = "Maximum capacity is required")
    @Min(value = 1, message = "Maximum capacity must be at least 1")
    @Max(value = 500, message = "Maximum capacity must not exceed 500")
    private Integer maxCapacity;
    
    // Dates
    @Future(message = "Start date must be in the future")
    private LocalDateTime startDate;
    
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;
    
    // Course materials
    @Size(max = 2000, message = "Syllabus must not exceed 2000 characters")
    private String syllabus;
    
    @Size(max = 1000, message = "Prerequisites must not exceed 1000 characters")
    private String prerequisites;
    
    @Size(max = 1000, message = "Grading policy must not exceed 1000 characters")
    private String gradingPolicy;
}
