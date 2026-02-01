package com.example.mongoRedis.course.repository;

import com.example.mongoRedis.course.dto.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
    
    // Basic queries
    Optional<Course> findByCourseCode(String courseCode);
    Page<Course> findByIsActive(boolean isActive, Pageable pageable);
    List<Course> findByIsActive(boolean isActive);
    
    // Academic year and semester
    Page<Course> findByAcademicYearAndIsActive(String academicYear, boolean isActive, Pageable pageable);
    List<Course> findByAcademicYearAndIsActive(String academicYear, boolean isActive);
    Page<Course> findByAcademicYearAndSemesterAndIsActive(String academicYear, String semester, boolean isActive, Pageable pageable);
    
    // Department and grade
    Page<Course> findByDepartmentAndIsActive(String department, boolean isActive, Pageable pageable);
    Page<Course> findByGradeAndIsActive(String grade, boolean isActive, Pageable pageable);
    Page<Course> findByDepartmentAndGradeAndIsActive(String department, String grade, boolean isActive, Pageable pageable);
    
    // Teacher assignments
    List<Course> findByTeacherIdAndIsActive(String teacherId, boolean isActive);
    Page<Course> findByTeacherIdAndIsActive(String teacherId, boolean isActive, Pageable pageable);
    List<Course> findByTeacherIdAndAcademicYearAndIsActive(String teacherId, String academicYear, boolean isActive);
    
    // Student enrollments
    List<Course> findByEnrolledStudentIdsContainingAndIsActive(String studentId, boolean isActive);
    Page<Course> findByEnrolledStudentIdsContainingAndIsActive(String studentId, boolean isActive, Pageable pageable);
    
    // Search functionality
    @Query("{ '$or': [ { 'name': { '$regex': ?0, '$options': 'i' } }, " +
           "{ 'courseCode': { '$regex': ?0, '$options': 'i' } }, " +
           "{ 'description': { '$regex': ?0, '$options': 'i' } } ], " +
           "'isActive': ?1 }")
    Page<Course> searchCourses(String query, boolean isActive, Pageable pageable);
    
    // Capacity and enrollment
    @Query("{ 'isActive': true, 'currentEnrollment': { '$lt': '$maxCapacity' } }")
    List<Course> findAvailableCourses();
    
    @Query("{ 'isActive': true, 'currentEnrollment': { '$lt': '$maxCapacity' }, " +
           "'academicYear': ?0, 'semester': ?1 }")
    List<Course> findAvailableCoursesByAcademicYearAndSemester(String academicYear, String semester);
    
    // Date-based queries
    List<Course> findByStartDateAfterAndIsActive(LocalDateTime date, boolean isActive);
    List<Course> findByEndDateBeforeAndIsActive(LocalDateTime date, boolean isActive);
    List<Course> findByStartDateBetweenAndIsActive(LocalDateTime startDate, LocalDateTime endDate, boolean isActive);
    
    // Statistics queries
    long countByIsActive(boolean isActive);
    long countByDepartmentAndIsActive(String department, boolean isActive);
    long countByTeacherIdAndIsActive(String teacherId, boolean isActive);
    long countByAcademicYearAndIsActive(String academicYear, boolean isActive);
    long countByAcademicYearAndSemesterAndIsActive(String academicYear, String semester, boolean isActive);
    
    // Complex queries
    @Query("{ 'isActive': true, 'academicYear': ?0, 'teacherId': ?1 }")
    List<Course> findActiveCoursesByTeacherAndAcademicYear(String academicYear, String teacherId);
    
    @Query("{ 'isActive': true, 'academicYear': ?0, 'grade': ?1, 'enrolledStudentIds': { '$size': { '$lt': '$maxCapacity' } } }")
    List<Course> findAvailableCoursesByGrade(String academicYear, String grade);
    
    @Query("{ 'isActive': true, 'startDate': { '$lte': ?0 }, 'endDate': { '$gte': ?0 } }")
    List<Course> findOngoingCourses(LocalDateTime currentDate);
    
    @Query("{ 'isActive': true, 'startDate': { '$gt': ?0 } }")
    List<Course> findUpcomingCourses(LocalDateTime currentDate);
    
    @Query("{ 'isActive': true, 'endDate': { '$lt': ?0 } }")
    List<Course> findCompletedCourses(LocalDateTime currentDate);
    
    // Course level and category
    Page<Course> findByLevelAndIsActive(String level, boolean isActive, Pageable pageable);
    Page<Course> findBySubjectCategoryAndIsActive(String subjectCategory, boolean isActive, Pageable pageable);
    
    // Room and schedule
    List<Course> findByRoomAndIsActive(String room, boolean isActive);
    List<Course> findByTimeSlotAndIsActive(String timeSlot, boolean isActive);
    
    // Custom aggregation queries for statistics
    @Query(value = "{ 'isActive': true }", count = true)
    long countActiveCourses();
    
    @Query(value = "{ 'isActive': true, 'currentEnrollment': { '$gt': 0 } }", count = true)
    long countCoursesWithEnrollments();
    
    @Query("{ 'isActive': true, 'currentEnrollment': { '$gte': 0 } }")
    List<Course> findAllActiveCoursesWithEnrollments();
}
