package com.example.mongoRedis.course.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherAssignmentRequest {
    
    @NotBlank(message = "Teacher ID is required")
    private String teacherId;
    
    @NotBlank(message = "Teacher name is required")
    private String teacherName;
    
    private List<String> courseIds;
    
    private String department;
    
    private int maxCourses;
    
    private String academicYear;
    
    private String notes;
}
