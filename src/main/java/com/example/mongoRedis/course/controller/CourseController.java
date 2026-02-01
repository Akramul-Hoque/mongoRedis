package com.example.mongoRedis.course.controller;

import com.example.mongoRedis.common.response.ApiResponse;
import com.example.mongoRedis.course.dto.request.*;
import com.example.mongoRedis.course.dto.response.*;
import com.example.mongoRedis.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/courses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CourseResponse> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        CourseResponse course = courseService.createCourse(request);
        return new ApiResponse<>(true, course, "Course created successfully");
    }

    @GetMapping
    public ApiResponse<Page<CourseSummaryResponse>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String academicYear,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) Boolean isActive) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<CourseSummaryResponse> courses = courseService.getAllCourses(
            pageable, academicYear, semester, department, grade, isActive);
        return new ApiResponse<>(true, courses, "Courses retrieved successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<CourseResponse> getCourseById(@PathVariable String id) {
        CourseResponse course = courseService.getCourseById(id);
        return new ApiResponse<>(true, course, "Course retrieved successfully");
    }

    @PutMapping("/{id}")
    public ApiResponse<CourseResponse> updateCourse(@PathVariable String id, 
                                                 @Valid @RequestBody CourseUpdateRequest request) {
        CourseResponse course = courseService.updateCourse(id, request);
        return new ApiResponse<>(true, course, "Course updated successfully");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        return new ApiResponse<>(true, null, "Course deleted successfully");
    }

    @PatchMapping("/{id}/activate")
    public ApiResponse<Void> activateCourse(@PathVariable String id) {
        courseService.activateCourse(id);
        return new ApiResponse<>(true, null, "Course activated successfully");
    }

    @PatchMapping("/{id}/deactivate")
    public ApiResponse<Void> deactivateCourse(@PathVariable String id) {
        courseService.deactivateCourse(id);
        return new ApiResponse<>(true, null, "Course deactivated successfully");
    }

    // Teacher Assignment Endpoints
    @PostMapping("/{courseId}/assign-teacher")
    public ApiResponse<CourseResponse> assignTeacher(@PathVariable String courseId, 
                                                   @Valid @RequestBody TeacherAssignmentRequest request) {
        CourseResponse course = courseService.assignTeacher(courseId, request);
        return new ApiResponse<>(true, course, "Teacher assigned successfully");
    }

    @DeleteMapping("/{courseId}/remove-teacher")
    public ApiResponse<Void> removeTeacher(@PathVariable String courseId) {
        courseService.removeTeacher(courseId);
        return new ApiResponse<>(true, null, "Teacher removed successfully");
    }

    // Student Enrollment Endpoints
    @PostMapping("/{courseId}/enroll")
    public ApiResponse<CourseEnrollmentResponse> enrollStudent(@PathVariable String courseId, 
                                                             @Valid @RequestBody CourseEnrollmentRequest request) {
        CourseEnrollmentResponse enrollment = courseService.enrollStudent(courseId, request);
        return new ApiResponse<>(true, enrollment, "Student enrolled successfully");
    }

    @DeleteMapping("/{courseId}/unenroll/{studentId}")
    public ApiResponse<Void> unenrollStudent(@PathVariable String courseId, 
                                            @PathVariable String studentId) {
        courseService.unenrollStudent(courseId, studentId);
        return new ApiResponse<>(true, null, "Student unenrolled successfully");
    }

    @GetMapping("/{courseId}/enrollments")
    public ApiResponse<List<CourseEnrollmentResponse>> getCourseEnrollments(@PathVariable String courseId) {
        List<CourseEnrollmentResponse> enrollments = courseService.getCourseEnrollments(courseId);
        return new ApiResponse<>(true, enrollments, "Course enrollments retrieved successfully");
    }

    // Search and Filter Endpoints
    @GetMapping("/search")
    public ApiResponse<Page<CourseSummaryResponse>> searchCourses(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<CourseSummaryResponse> courses = courseService.searchCourses(query, pageable);
        return new ApiResponse<>(true, courses, "Search results retrieved successfully");
    }

    @GetMapping("/available")
    public ApiResponse<List<CourseSummaryResponse>> getAvailableCourses(
            @RequestParam(required = false) String academicYear,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String grade) {
        
        List<CourseSummaryResponse> courses = courseService.getAvailableCourses(academicYear, semester, grade);
        return new ApiResponse<>(true, courses, "Available courses retrieved successfully");
    }

    // Statistics and Reports
    @GetMapping("/statistics")
    public ApiResponse<CourseStatisticsResponse> getCourseStatistics() {
        CourseStatisticsResponse statistics = courseService.getCourseStatistics();
        return new ApiResponse<>(true, statistics, "Course statistics retrieved successfully");
    }

    @GetMapping("/teacher/{teacherId}")
    public ApiResponse<List<CourseSummaryResponse>> getCoursesByTeacher(@PathVariable String teacherId) {
        List<CourseSummaryResponse> courses = courseService.getCoursesByTeacher(teacherId);
        return new ApiResponse<>(true, courses, "Teacher courses retrieved successfully");
    }

    @GetMapping("/student/{studentId}")
    public ApiResponse<List<CourseSummaryResponse>> getCoursesByStudent(@PathVariable String studentId) {
        List<CourseSummaryResponse> courses = courseService.getCoursesByStudent(studentId);
        return new ApiResponse<>(true, courses, "Student courses retrieved successfully");
    }

    // DTO for statistics response
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class CourseStatisticsResponse {
        private long totalCourses;
        private long activeCourses;
        private long inactiveCourses;
        private long coursesByDepartment;
        private long coursesByLevel;
        private double averageEnrollment;
        private long totalEnrollments;
        private long completedCourses;
        private long upcomingCourses;
        private long ongoingCourses;
        private double averageCapacity;
        private int totalStudents;
        private int totalTeachers;
    }
}
