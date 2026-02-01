package com.example.mongoRedis.course.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEnrollmentRequest {
    
    @NotBlank(message = "Course ID is required")
    private String courseId;
    
    @NotBlank(message = "Student ID is required")
    private String studentId;
    
    @NotBlank(message = "Academic year is required")
    private String academicYear;
    
    @NotBlank(message = "Semester is required")
    @Pattern(regexp = "^(FALL|SPRING|SUMMER|WINTER)$", message = "Semester must be FALL, SPRING, SUMMER, or WINTER")
    private String semester;
    
    @Pattern(regexp = "^(REGULAR|AUDIT|EXTRA_CREDIT)$", message = "Enrollment type must be REGULAR, AUDIT, or EXTRA_CREDIT")
    private String enrollmentType;
    
    private String notes;
}
