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
@RequestMapping("/student/courses")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
public class StudentCourseController {

    private final CourseService courseService;

    @GetMapping("/my-courses")
    public ApiResponse<List<CourseSummaryResponse>> getMyCourses() {
        List<CourseSummaryResponse> courses = courseService.getStudentCurrentCourses();
        return new ApiResponse<>(true, courses, "Student courses retrieved successfully");
    }

    @GetMapping("/my-courses/{courseId}")
    public ApiResponse<CourseResponse> getMyCourseById(@PathVariable String courseId) {
        CourseResponse course = courseService.getStudentCourseById(courseId);
        return new ApiResponse<>(true, course, "Course retrieved successfully");
    }

    @GetMapping("/available")
    public ApiResponse<List<CourseSummaryResponse>> getAvailableCourses(
            @RequestParam(required = false) String academicYear,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String grade) {
        
        List<CourseSummaryResponse> courses = courseService.getStudentAvailableCourses(academicYear, semester, grade);
        return new ApiResponse<>(true, courses, "Available courses retrieved successfully");
    }

    @PostMapping("/enroll")
    public ApiResponse<CourseEnrollmentResponse> enrollInCourse(@Valid @RequestBody CourseEnrollmentRequest request) {
        CourseEnrollmentResponse enrollment = courseService.studentEnrollInCourse(request);
        return new ApiResponse<>(true, enrollment, "Enrolled in course successfully");
    }

    @DeleteMapping("/unenroll/{courseId}")
    public ApiResponse<Void> unenrollFromCourse(@PathVariable String courseId) {
        courseService.studentUnenrollFromCourse(courseId);
        return new ApiResponse<>(true, null, "Unenrolled from course successfully");
    }

    @GetMapping("/my-enrollments")
    public ApiResponse<List<CourseEnrollmentResponse>> getMyEnrollments() {
        List<CourseEnrollmentResponse> enrollments = courseService.getStudentEnrollments();
        return new ApiResponse<>(true, enrollments, "Student enrollments retrieved successfully");
    }

    @GetMapping("/my-enrollments/{courseId}")
    public ApiResponse<CourseEnrollmentResponse> getMyEnrollment(@PathVariable String courseId) {
        CourseEnrollmentResponse enrollment = courseService.getStudentEnrollment(courseId);
        return new ApiResponse<>(true, enrollment, "Enrollment details retrieved successfully");
    }

    @GetMapping("/academic-history")
    public ApiResponse<List<CourseEnrollmentResponse>> getAcademicHistory() {
        List<CourseEnrollmentResponse> history = courseService.getStudentAcademicHistory();
        return new ApiResponse<>(true, history, "Academic history retrieved successfully");
    }

    @GetMapping("/transcript")
    public ApiResponse<StudentTranscriptResponse> getTranscript() {
        StudentTranscriptResponse transcript = courseService.getStudentTranscript();
        return new ApiResponse<>(true, transcript, "Transcript retrieved successfully");
    }

    @GetMapping("/progress-summary")
    public ApiResponse<StudentProgressSummaryResponse> getProgressSummary() {
        StudentProgressSummaryResponse summary = courseService.getStudentProgressSummary();
        return new ApiResponse<>(true, summary, "Progress summary retrieved successfully");
    }

    // DTO for student transcript
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class StudentTranscriptResponse {
        private String studentId;
        private String studentName;
        private String academicYear;
        private double overallGPA;
        private double totalCredits;
        private int completedCourses;
        private int inProgressCourses;
        private List<CourseEnrollmentResponse> courses;
        private String status; // GOOD_STANDING, PROBATION, SUSPENDED
        private String graduationStatus;
    }

    // DTO for student progress summary
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class StudentProgressSummaryResponse {
        private String studentId;
        private String studentName;
        private String currentSemester;
        private int enrolledCourses;
        private int completedCourses;
        private double currentGPA;
        private double cumulativeGPA;
        private int totalCredits;
        private int earnedCredits;
        private List<CourseSummaryResponse> currentCourses;
        private List<CourseSummaryResponse> upcomingCourses;
        private List<String> recommendations;
        private String academicStanding; // EXCELLENT, GOOD, SATISFACTORY, PROBATIONARY
    }
}
