package com.example.mongoRedis.course.repository;

import com.example.mongoRedis.course.dto.model.CourseEnrollment;
import com.example.mongoRedis.course.dto.model.CourseEnrollment.EnrollmentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseEnrollmentRepository extends MongoRepository<CourseEnrollment, String> {
    
    // Basic queries
    Optional<CourseEnrollment> findByCourseIdAndStudentId(String courseId, String studentId);
    List<CourseEnrollment> findByCourseId(String courseId);
    List<CourseEnrollment> findByStudentId(String studentId);
    List<CourseEnrollment> findByTeacherId(String teacherId);
    
    // Status-based queries
    List<CourseEnrollment> findByStatus(EnrollmentStatus status);
    List<CourseEnrollment> findByCourseIdAndStatus(String courseId, EnrollmentStatus status);
    List<CourseEnrollment> findByStudentIdAndStatus(String studentId, EnrollmentStatus status);
    List<CourseEnrollment> findByTeacherIdAndStatus(String teacherId, EnrollmentStatus status);
    
    // Academic year and semester
    List<CourseEnrollment> findByAcademicYearAndSemester(String academicYear, String semester);
    List<CourseEnrollment> findByStudentIdAndAcademicYearAndSemester(String studentId, String academicYear, String semester);
    List<CourseEnrollment> findByCourseIdAndAcademicYearAndSemester(String courseId, String academicYear, String semester);
    
    // Date-based queries
    List<CourseEnrollment> findByEnrolledAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<CourseEnrollment> findByCompletedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<CourseEnrollment> findByDroppedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Performance queries
    List<CourseEnrollment> findByStudentIdAndStatusOrderByFinalGradeDesc(String studentId, EnrollmentStatus status);
    List<CourseEnrollment> findByCourseIdAndStatusOrderByFinalGradeDesc(String courseId, EnrollmentStatus status);
    
    // Attendance queries
    List<CourseEnrollment> findByStudentIdAndAttendancePercentageLessThan(String studentId, int threshold);
    List<CourseEnrollment> findByCourseIdAndAttendancePercentageLessThan(String courseId, int threshold);
    
    // Statistics queries
    long countByCourseIdAndStatus(String courseId, EnrollmentStatus status);
    long countByStudentIdAndStatus(String studentId, EnrollmentStatus status);
    long countByTeacherIdAndStatus(String teacherId, EnrollmentStatus status);
    long countByAcademicYearAndSemesterAndStatus(String academicYear, String semester, EnrollmentStatus status);
    
    // Complex queries
    @Query("{ 'studentId': ?0, 'status': 'COMPLETED', 'academicYear': ?1 }")
    List<CourseEnrollment> findCompletedCoursesByStudentAndAcademicYear(String studentId, String academicYear);
    
    @Query("{ 'courseId': ?0, 'status': 'ENROLLED', 'academicYear': ?1, 'semester': ?2 }")
    List<CourseEnrollment> findEnrolledStudentsByCourseAndAcademicYear(String courseId, String academicYear, String semester);
    
    @Query("{ 'teacherId': ?0, 'status': 'COMPLETED', 'academicYear': ?1 }")
    List<CourseEnrollment> findCompletedCoursesByTeacherAndAcademicYear(String teacherId, String academicYear);
    
    // Grade distribution queries
    @Query("{ 'courseId': ?0, 'status': 'COMPLETED', 'finalGrade': { '$gte': ?1, '$lte': ?2 } }")
    List<CourseEnrollment> findStudentsByGradeRange(String courseId, double minGrade, double maxGrade);
    
    @Query("{ 'studentId': ?0, 'status': 'COMPLETED', 'finalGrade': { '$gte': ?1 } }")
    List<CourseEnrollment> findStudentCoursesAboveGrade(String studentId, double minGrade);
    
    // Enrollment type queries
    List<CourseEnrollment> findByEnrollmentType(String enrollmentType);
    List<CourseEnrollment> findByCourseIdAndEnrollmentType(String courseId, String enrollmentType);
    List<CourseEnrollment> findByStudentIdAndEnrollmentType(String studentId, String enrollmentType);
    
    // Recent activity queries
    @Query("{ 'enrolledAt': { '$gte': ?0 } }")
    List<CourseEnrollment> findRecentEnrollments(LocalDateTime since);
    
    @Query("{ 'completedAt': { '$gte': ?0 } }")
    List<CourseEnrollment> findRecentCompletions(LocalDateTime since);
    
    @Query("{ 'droppedAt': { '$gte': ?0 } }")
    List<CourseEnrollment> findRecentDrops(LocalDateTime since);
    
    // Aggregation queries for statistics
    @Query(value = "{ 'status': 'COMPLETED' }", count = true)
    long countCompletedEnrollments();
    
    @Query(value = "{ 'status': 'ENROLLED' }", count = true)
    long countActiveEnrollments();
    
    @Query(value = "{ 'status': 'DROPPED' }", count = true)
    long countDroppedEnrollments();
    
    @Query("{ 'status': 'COMPLETED' }")
    List<CourseEnrollment> findAllCompletedEnrollments();
    
    @Query("{ 'status': 'ENROLLED' }")
    List<CourseEnrollment> findAllActiveEnrollments();
}
