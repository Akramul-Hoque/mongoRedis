package com.example.mongoRedis.course.controller;

import com.example.mongoRedis.common.response.ApiResponse;
import com.example.mongoRedis.course.dto.request.CourseEnrollmentRequest;
import com.example.mongoRedis.course.dto.response.*;
import com.example.mongoRedis.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher/courses")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class TeacherCourseController {

    private final CourseService courseService;

    @GetMapping("/my-courses")
    public ApiResponse<List<CourseSummaryResponse>> getMyCourses() {
        List<CourseSummaryResponse> courses = courseService.getTeacherCurrentCourses();
        return new ApiResponse<>(true, courses, "Teacher courses retrieved successfully");
    }

    @GetMapping("/my-courses/{courseId}")
    public ApiResponse<CourseResponse> getMyCourseById(@PathVariable String courseId) {
        CourseResponse course = courseService.getTeacherCourseById(courseId);
        return new ApiResponse<>(true, course, "Course retrieved successfully");
    }

    @GetMapping("/my-courses/{courseId}/students")
    public ApiResponse<List<CourseEnrollmentResponse>> getMyCourseStudents(@PathVariable String courseId) {
        List<CourseEnrollmentResponse> students = courseService.getTeacherCourseStudents(courseId);
        return new ApiResponse<>(true, students, "Course students retrieved successfully");
    }

    @PostMapping("/my-courses/{courseId}/enroll-student")
    public ApiResponse<CourseEnrollmentResponse> enrollStudent(@PathVariable String courseId, 
                                                             @Valid @RequestBody CourseEnrollmentRequest request) {
        CourseEnrollmentResponse enrollment = courseService.teacherEnrollStudent(courseId, request);
        return new ApiResponse<>(true, enrollment, "Student enrolled successfully");
    }

    @DeleteMapping("/my-courses/{courseId}/unenroll-student/{studentId}")
    public ApiResponse<Void> unenrollStudent(@PathVariable String courseId, 
                                            @PathVariable String studentId) {
        courseService.teacherUnenrollStudent(courseId, studentId);
        return new ApiResponse<>(true, null, "Student unenrolled successfully");
    }

    @PatchMapping("/my-courses/{courseId}/student/{studentId}/grade")
    public ApiResponse<Void> updateStudentGrade(@PathVariable String courseId, 
                                              @PathVariable String studentId,
                                              @RequestParam double grade) {
        courseService.updateStudentGrade(courseId, studentId, grade);
        return new ApiResponse<>(true, null, "Student grade updated successfully");
    }

    @PatchMapping("/my-courses/{courseId}/student/{studentId}/attendance")
    public ApiResponse<Void> updateStudentAttendance(@PathVariable String courseId, 
                                                   @PathVariable String studentId,
                                                   @RequestParam int attendancePercentage) {
        courseService.updateStudentAttendance(courseId, studentId, attendancePercentage);
        return new ApiResponse<>(true, null, "Student attendance updated successfully");
    }

    @GetMapping("/my-courses/{courseId}/statistics")
    public ApiResponse<TeacherCourseStatisticsResponse> getMyCourseStatistics(@PathVariable String courseId) {
        TeacherCourseStatisticsResponse statistics = courseService.getTeacherCourseStatistics(courseId);
        return new ApiResponse<>(true, statistics, "Course statistics retrieved successfully");
    }

    @GetMapping("/assignment-summary")
    public ApiResponse<TeacherCourseAssignmentResponse> getAssignmentSummary() {
        TeacherCourseAssignmentResponse summary = courseService.getTeacherAssignmentSummary();
        return new ApiResponse<>(true, summary, "Assignment summary retrieved successfully");
    }

    // DTO for teacher course statistics
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class TeacherCourseStatisticsResponse {
        private String courseId;
        private String courseName;
        private String courseCode;
        private int totalStudents;
        private int enrolledStudents;
        private int completedStudents;
        private int droppedStudents;
        private double averageGrade;
        private double averageAttendance;
        private int maxCapacity;
        private int availableSlots;
        private String academicYear;
        private String semester;
        private List<StudentPerformanceSummary> topPerformers;
        private List<StudentPerformanceSummary> needAttention;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class StudentPerformanceSummary {
        private String studentId;
        private String studentName;
        private double grade;
        private int attendancePercentage;
        private String status; // EXCELLENT, GOOD, NEEDS_IMPROVEMENT, AT_RISK
    }
}
