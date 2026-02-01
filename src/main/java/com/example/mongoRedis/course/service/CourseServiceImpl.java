package com.example.mongoRedis.course.service;

import com.example.mongoRedis.course.controller.CourseController.CourseStatisticsResponse;
import com.example.mongoRedis.course.controller.StudentCourseController.StudentProgressSummaryResponse;
import com.example.mongoRedis.course.controller.StudentCourseController.StudentTranscriptResponse;
import com.example.mongoRedis.course.controller.TeacherCourseController.StudentPerformanceSummary;
import com.example.mongoRedis.course.controller.TeacherCourseController.TeacherCourseStatisticsResponse;
import com.example.mongoRedis.course.dto.model.Course;
import com.example.mongoRedis.course.dto.model.CourseEnrollment;
import com.example.mongoRedis.course.dto.model.CourseEnrollment.EnrollmentStatus;
import com.example.mongoRedis.course.dto.request.*;
import com.example.mongoRedis.course.dto.response.*;
import com.example.mongoRedis.course.repository.CourseEnrollmentRepository;
import com.example.mongoRedis.course.repository.CourseRepository;
import com.example.mongoRedis.exception.CustomServiceException;
import com.example.mongoRedis.exception.ResourceNotFoundException;
import com.example.mongoRedis.user.dto.model.User;
import com.example.mongoRedis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseEnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    @Override
    public CourseResponse createCourse(CourseCreateRequest request) {
        log.info("Creating new course: {}", request.getName());

        if (courseRepository.findByCourseCode(request.getCourseCode()).isPresent()) {
            throw new CustomServiceException("Course with code " + request.getCourseCode() + " already exists");
        }

        Course course = Course.builder()
                .courseCode(request.getCourseCode())
                .name(request.getName())
                .description(request.getDescription())
                .academicYear(request.getAcademicYear())
                .semester(request.getSemester())
                .credits(request.getCredits())
                .department(request.getDepartment())
                .maxCapacity(request.getMaxCapacity())
                .schedule(request.getSchedule())
                .room(request.getRoom())
                .timeSlot(request.getTimeSlot())
                .daysOfWeek(request.getDaysOfWeek())
                .grade(request.getGrade())
                .section(request.getSection())
                .subjectCategory(request.getSubjectCategory())
                .level(request.getLevel())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .enrolledStudentIds(new ArrayList<>())
                .build();

        Course savedCourse = courseRepository.save(course);
        return mapToCourseResponse(savedCourse);
    }

    @Override
    public CourseResponse getCourseById(String id) {
        Course course = findCourseById(id);
        return mapToCourseResponse(course);
    }

    @Override
    public Page<CourseSummaryResponse> getAllCourses(Pageable pageable, String academicYear, String semester,
            String department, String grade, Boolean isActive) {
        log.debug("Getting all courses with filters");
        // Simplified filter logic for now, could be more robust with Specifications if
        // using JPA,
        // with Mongo we can use Query objects or multiple repository methods

        // In a real app, you'd use a more dynamic approach. For now, let's use the
        // repository methods we have.
        Page<Course> courses;
        if (isActive != null) {
            courses = courseRepository.findByIsActive(isActive, pageable);
        } else {
            courses = courseRepository.findAll(pageable);
        }

        List<CourseSummaryResponse> summaries = courses.getContent().stream()
                .map(this::mapToCourseSummary)
                .collect(Collectors.toList());

        return new PageImpl<>(summaries, pageable, courses.getTotalElements());
    }

    @Override
    public CourseResponse updateCourse(String id, CourseUpdateRequest request) {
        log.info("Updating course: {}", id);
        Course course = findCourseById(id);

        if (request.getName() != null)
            course.setName(request.getName());
        if (request.getDescription() != null)
            course.setDescription(request.getDescription());
        if (request.getCredits() != null)
            course.setCredits(request.getCredits());
        if (request.getMaxCapacity() != null)
            course.setMaxCapacity(request.getMaxCapacity());
        if (request.getSchedule() != null)
            course.setSchedule(request.getSchedule());
        if (request.getRoom() != null)
            course.setRoom(request.getRoom());
        if (request.getAcademicYear() != null)
            course.setAcademicYear(request.getAcademicYear());
        if (request.getSemester() != null)
            course.setSemester(request.getSemester());

        course.setUpdatedAt(LocalDateTime.now());
        Course updatedCourse = courseRepository.save(course);
        return mapToCourseResponse(updatedCourse);
    }

    @Override
    public void deleteCourse(String id) {
        log.info("Deleting course: {}", id);
        Course course = findCourseById(id);
        courseRepository.delete(course);
    }

    @Override
    public void activateCourse(String id) {
        Course course = findCourseById(id);
        course.setActive(true);
        courseRepository.save(course);
    }

    @Override
    public void deactivateCourse(String id) {
        Course course = findCourseById(id);
        course.setActive(false);
        courseRepository.save(course);
    }

    @Override
    public CourseResponse assignTeacher(String courseId, TeacherAssignmentRequest request) {
        log.info("Assigning teacher {} to course {}", request.getTeacherId(), courseId);
        Course course = findCourseById(courseId);
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        course.setTeacherId(teacher.getId());
        course.setTeacherName(teacher.getName());
        course.setUpdatedAt(LocalDateTime.now());

        Course updatedCourse = courseRepository.save(course);
        return mapToCourseResponse(updatedCourse);
    }

    @Override
    public void removeTeacher(String courseId) {
        Course course = findCourseById(courseId);
        course.setTeacherId(null);
        course.setTeacherName(null);
        courseRepository.save(course);
    }

    @Override
    public CourseEnrollmentResponse enrollStudent(String courseId, CourseEnrollmentRequest request) {
        log.info("Enrolling student {} in course {}", request.getStudentId(), courseId);
        Course course = findCourseById(courseId);
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (course.getCurrentEnrollment() >= course.getMaxCapacity()) {
            throw new CustomServiceException("Course is full");
        }

        if (course.getEnrolledStudentIds().contains(request.getStudentId())) {
            throw new CustomServiceException("Student already enrolled");
        }

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .courseId(course.getId())
                .courseName(course.getName())
                .courseCode(course.getCourseCode())
                .studentId(request.getStudentId())
                .studentName(student.getName())
                .teacherId(course.getTeacherId())
                .teacherName(course.getTeacherName())
                .status(EnrollmentStatus.ENROLLED)
                .enrolledAt(LocalDateTime.now())
                .academicYear(course.getAcademicYear())
                .semester(course.getSemester())
                .credits(course.getCredits())
                .department(course.getDepartment())
                .schedule(course.getSchedule())
                .room(course.getRoom())
                .build();

        CourseEnrollment savedEnrollment = enrollmentRepository.save(enrollment);

        course.getEnrolledStudentIds().add(request.getStudentId());
        course.setCurrentEnrollment(course.getEnrolledStudentIds().size());
        courseRepository.save(course);

        return mapToEnrollmentResponse(savedEnrollment);
    }

    @Override
    public void unenrollStudent(String courseId, String studentId) {
        Course course = findCourseById(courseId);
        CourseEnrollment enrollment = enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        enrollmentRepository.delete(enrollment);

        course.getEnrolledStudentIds().remove(studentId);
        course.setCurrentEnrollment(course.getEnrolledStudentIds().size());
        courseRepository.save(course);
    }

    @Override
    public List<CourseEnrollmentResponse> getCourseEnrollments(String courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::mapToEnrollmentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CourseSummaryResponse> searchCourses(String query, Pageable pageable) {
        Page<Course> courses = courseRepository.searchCourses(query, true, pageable);
        return courses.map(this::mapToCourseSummary);
    }

    @Override
    public List<CourseSummaryResponse> getAvailableCourses(String academicYear, String semester, String grade) {
        return courseRepository.findAvailableCourses().stream()
                .map(this::mapToCourseSummary)
                .collect(Collectors.toList());
    }

    @Override
    public CourseStatisticsResponse getCourseStatistics() {
        return CourseStatisticsResponse.builder()
                .totalCourses(courseRepository.count())
                .activeCourses(courseRepository.countByIsActive(true))
                .inactiveCourses(courseRepository.countByIsActive(false))
                .totalEnrollments(enrollmentRepository.count())
                .build();
    }

    @Override
    public List<CourseSummaryResponse> getCoursesByTeacher(String teacherId) {
        return courseRepository.findByTeacherIdAndIsActive(teacherId, true).stream()
                .map(this::mapToCourseSummary)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseSummaryResponse> getCoursesByStudent(String studentId) {
        return courseRepository.findByEnrolledStudentIdsContainingAndIsActive(studentId, true).stream()
                .map(this::mapToCourseSummary)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseSummaryResponse> getTeacherCurrentCourses() {
        String teacherId = getCurrentUserId();
        return getCoursesByTeacher(teacherId);
    }

    @Override
    public CourseResponse getTeacherCourseById(String courseId) {
        // In a real app, verify ownership
        return getCourseById(courseId);
    }

    @Override
    public List<CourseEnrollmentResponse> getTeacherCourseStudents(String courseId) {
        return getCourseEnrollments(courseId);
    }

    @Override
    public CourseEnrollmentResponse teacherEnrollStudent(String courseId, CourseEnrollmentRequest request) {
        return enrollStudent(courseId, request);
    }

    @Override
    public void teacherUnenrollStudent(String courseId, String studentId) {
        unenrollStudent(courseId, studentId);
    }

    @Override
    public void updateStudentGrade(String courseId, String studentId, double grade) {
        CourseEnrollment enrollment = enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        enrollment.setFinalGrade(grade);
        enrollmentRepository.save(enrollment);
    }

    @Override
    public void updateStudentAttendance(String courseId, String studentId, int attendancePercentage) {
        CourseEnrollment enrollment = enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        enrollment.setAttendancePercentage(attendancePercentage);
        enrollmentRepository.save(enrollment);
    }

    @Override
    public TeacherCourseStatisticsResponse getTeacherCourseStatistics(String courseId) {
        Course course = findCourseById(courseId);
        return TeacherCourseStatisticsResponse.builder()
                .courseId(course.getId())
                .courseName(course.getName())
                .courseCode(course.getCourseCode())
                .enrolledStudents(course.getCurrentEnrollment())
                .maxCapacity(course.getMaxCapacity())
                .build();
    }

    @Override
    public TeacherCourseAssignmentResponse getTeacherAssignmentSummary() {
        String teacherId = getCurrentUserId();
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        
        List<Course> courses = courseRepository.findByTeacherIdAndIsActive(teacherId, true);
        List<CourseSummaryResponse> assignedCourses = courses.stream()
                .map(this::mapToCourseSummary)
                .collect(Collectors.toList());
        
        int totalStudents = courses.stream()
                .mapToInt(Course::getCurrentEnrollment)
                .sum();
        
        double totalCredits = courses.stream()
                .mapToInt(Course::getCredits)
                .sum();
        
        return TeacherCourseAssignmentResponse.builder()
                .teacherId(teacherId)
                .teacherName(teacher.getName())
                .teacherEmail(teacher.getEmail())
                .employeeId(teacher.getEmployeeId())
                .department(teacher.getDepartment())
                .totalCourses(courses.size())
                .maxCourses(10) // Default max courses per teacher
                .availableSlots(Math.max(0, 10 - courses.size()))
                .assignedCourses(assignedCourses)
                .academicYear(courses.isEmpty() ? null : courses.get(0).getAcademicYear())
                .totalCredits(totalCredits)
                .totalStudents(totalStudents)
                .build();
    }

    @Override
    public List<CourseSummaryResponse> getStudentCurrentCourses() {
        String studentId = getCurrentUserId();
        return getCoursesByStudent(studentId);
    }

    @Override
    public CourseResponse getStudentCourseById(String courseId) {
        return getCourseById(courseId);
    }

    @Override
    public List<CourseSummaryResponse> getStudentAvailableCourses(String academicYear, String semester, String grade) {
        return getAvailableCourses(academicYear, semester, grade);
    }

    @Override
    public CourseEnrollmentResponse studentEnrollInCourse(CourseEnrollmentRequest request) {
        // Usually student doesn't choose studentId in request, it's from auth
        if (request.getStudentId() == null) {
            request.setStudentId(getCurrentUserId());
        }
        return enrollStudent(request.getCourseId(), request);
    }

    @Override
    public void studentUnenrollFromCourse(String courseId) {
        unenrollStudent(courseId, getCurrentUserId());
    }

    @Override
    public List<CourseEnrollmentResponse> getStudentEnrollments() {
        return enrollmentRepository.findByStudentId(getCurrentUserId()).stream()
                .map(this::mapToEnrollmentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CourseEnrollmentResponse getStudentEnrollment(String courseId) {
        CourseEnrollment enrollment = enrollmentRepository.findByCourseIdAndStudentId(courseId, getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        return mapToEnrollmentResponse(enrollment);
    }

    @Override
    public List<CourseEnrollmentResponse> getStudentAcademicHistory() {
        return getStudentEnrollments();
    }

    @Override
    public StudentTranscriptResponse getStudentTranscript() {
        String studentId = getCurrentUserId();
        List<CourseEnrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return StudentTranscriptResponse.builder()
                .studentId(studentId)
                .courses(enrollments.stream().map(this::mapToEnrollmentResponse).collect(Collectors.toList()))
                .build();
    }

    @Override
    public StudentProgressSummaryResponse getStudentProgressSummary() {
        String studentId = getCurrentUserId();
        return StudentProgressSummaryResponse.builder()
                .studentId(studentId)
                .build();
    }

    // Helper methods
    private Course findCourseById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    private CourseResponse mapToCourseResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .name(course.getName())
                .description(course.getDescription())
                .credits(course.getCredits())
                .department(course.getDepartment())
                .teacherId(course.getTeacherId())
                .teacherName(course.getTeacherName())
                .enrolledStudentIds(course.getEnrolledStudentIds())
                .maxCapacity(course.getMaxCapacity())
                .currentEnrollment(course.getCurrentEnrollment())
                .availableSlots(course.getMaxCapacity() - course.getCurrentEnrollment())
                .schedule(course.getSchedule())
                .room(course.getRoom())
                .timeSlot(course.getTimeSlot())
                .daysOfWeek(course.getDaysOfWeek())
                .grade(course.getGrade())
                .section(course.getSection())
                .subjectCategory(course.getSubjectCategory())
                .level(course.getLevel())
                .isActive(course.isActive())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .syllabus(course.getSyllabus())
                .prerequisites(course.getPrerequisites())
                .gradingPolicy(course.getGradingPolicy())
                .averageGrade(course.getAverageGrade())
                .completedStudents(course.getCompletedStudents())
                .droppedStudents(course.getDroppedStudents())
                .academicYear(course.getAcademicYear())
                .semester(course.getSemester())
                .build();
    }

    private CourseSummaryResponse mapToCourseSummary(Course course) {
        return CourseSummaryResponse.builder()
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .name(course.getName())
//                .description(course.getDescription())
                .academicYear(course.getAcademicYear())
                .semester(course.getSemester())
                .credits(course.getCredits())
                .department(course.getDepartment())
                .teacherId(course.getTeacherId())
                .teacherName(course.getTeacherName())
                .currentEnrollment(course.getCurrentEnrollment())
                .maxCapacity(course.getMaxCapacity())
                .availableSlots(course.getMaxCapacity() - course.getCurrentEnrollment())
                .schedule(course.getSchedule())
                .room(course.getRoom())
                .timeSlot(course.getTimeSlot())
                .grade(course.getGrade())
                .section(course.getSection())
                .level(course.getLevel())
                .isActive(course.isActive())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .averageGrade(course.getAverageGrade())
                .completedStudents(course.getCompletedStudents())
                .build();
    }

    private CourseEnrollmentResponse mapToEnrollmentResponse(CourseEnrollment enrollment) {
        return CourseEnrollmentResponse.builder()
                .id(enrollment.getId())
                .courseId(enrollment.getCourseId())
                .courseName(enrollment.getCourseName())
                .courseCode(enrollment.getCourseCode())
                .studentId(enrollment.getStudentId())
                .studentName(enrollment.getStudentName())
                .teacherId(enrollment.getTeacherId())
                .teacherName(enrollment.getTeacherName())
                .academicYear(enrollment.getAcademicYear())
                .semester(enrollment.getSemester())
                .status(enrollment.getStatus())
                .enrolledAt(enrollment.getEnrolledAt())
                .completedAt(enrollment.getCompletedAt())
                .droppedAt(enrollment.getDroppedAt())
                .finalGrade(enrollment.getFinalGrade())
                .gradeLetter(enrollment.getGradeLetter())
                .passed(enrollment.isPassed())
                .attendancePercentage(enrollment.getAttendancePercentage())
                .enrollmentType(enrollment.getEnrollmentType())
                .dropReason(enrollment.getDropReason())
                .notes(enrollment.getNotes())
                .credits(enrollment.getCredits())
                .department(enrollment.getDepartment())
                .schedule(enrollment.getSchedule())
                .room(enrollment.getRoom())
                .build();
    }
}
