package com.example.mongoRedis.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourseAssignmentResponse {
    
    private String teacherId;
    private String teacherName;
    private String teacherEmail;
    private String employeeId;
    private String department;
    
    private int totalCourses;
    private int maxCourses;
    private int availableSlots;
    
    private List<CourseSummaryResponse> assignedCourses;
    private List<CourseSummaryResponse> availableCourses;
    
    private String academicYear;
    private double totalCredits;
    private int totalStudents;
    
    // Assignment statistics
    private int coursesByLevel;
    private int coursesByDepartment;
    private List<String> subjects;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignmentStatistics {
        private int beginnerCourses;
        private int intermediateCourses;
        private int advancedCourses;
        private int totalStudents;
        private double averageClassSize;
        private int overloadedCourses; // Courses over 80% capacity
    }
}
