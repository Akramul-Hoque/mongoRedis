package com.example.mongoRedis.course.service;

import com.example.mongoRedis.course.controller.CourseController.CourseStatisticsResponse;
import com.example.mongoRedis.course.controller.StudentCourseController.StudentProgressSummaryResponse;
import com.example.mongoRedis.course.controller.StudentCourseController.StudentTranscriptResponse;
import com.example.mongoRedis.course.controller.TeacherCourseController.TeacherCourseStatisticsResponse;
import com.example.mongoRedis.course.dto.request.*;
import com.example.mongoRedis.course.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
    
    // Basic CRUD operations
    CourseResponse createCourse(CourseCreateRequest request);
    CourseResponse getCourseById(String id);
    Page<CourseSummaryResponse> getAllCourses(Pageable pageable, String academicYear, String semester, 
                                            String department, String grade, Boolean isActive);
    CourseResponse updateCourse(String id, CourseUpdateRequest request);
    void deleteCourse(String id);
    
    // Course status management
    void activateCourse(String id);
    void deactivateCourse(String id);
    
    // Teacher assignment
    CourseResponse assignTeacher(String courseId, TeacherAssignmentRequest request);
    void removeTeacher(String courseId);
    
    // Student enrollment
    CourseEnrollmentResponse enrollStudent(String courseId, CourseEnrollmentRequest request);
    void unenrollStudent(String courseId, String studentId);
    List<CourseEnrollmentResponse> getCourseEnrollments(String courseId);
    
    // Search and availability
    Page<CourseSummaryResponse> searchCourses(String query, Pageable pageable);
    List<CourseSummaryResponse> getAvailableCourses(String academicYear, String semester, String grade);
    
    // Statistics and reports
    CourseStatisticsResponse getCourseStatistics();
    List<CourseSummaryResponse> getCoursesByTeacher(String teacherId);
    List<CourseSummaryResponse> getCoursesByStudent(String studentId);
    
    // Teacher-specific methods
    List<CourseSummaryResponse> getTeacherCurrentCourses();
    CourseResponse getTeacherCourseById(String courseId);
    List<CourseEnrollmentResponse> getTeacherCourseStudents(String courseId);
    CourseEnrollmentResponse teacherEnrollStudent(String courseId, CourseEnrollmentRequest request);
    void teacherUnenrollStudent(String courseId, String studentId);
    void updateStudentGrade(String courseId, String studentId, double grade);
    void updateStudentAttendance(String courseId, String studentId, int attendancePercentage);
    TeacherCourseStatisticsResponse getTeacherCourseStatistics(String courseId);
    TeacherCourseAssignmentResponse getTeacherAssignmentSummary();
    
    // Student-specific methods
    List<CourseSummaryResponse> getStudentCurrentCourses();
    CourseResponse getStudentCourseById(String courseId);
    List<CourseSummaryResponse> getStudentAvailableCourses(String academicYear, String semester, String grade);
    CourseEnrollmentResponse studentEnrollInCourse(CourseEnrollmentRequest request);
    void studentUnenrollFromCourse(String courseId);
    List<CourseEnrollmentResponse> getStudentEnrollments();
    CourseEnrollmentResponse getStudentEnrollment(String courseId);
    List<CourseEnrollmentResponse> getStudentAcademicHistory();
    StudentTranscriptResponse getStudentTranscript();
    StudentProgressSummaryResponse getStudentProgressSummary();
}
