package com.example.mongoRedis.user.service;

import com.example.mongoRedis.common.UserType;
import com.example.mongoRedis.exception.CustomServiceException;
import com.example.mongoRedis.exception.ResourceNotFoundException;
import com.example.mongoRedis.user.controller.AdminUserController;
import com.example.mongoRedis.user.controller.ProfileController;
import com.example.mongoRedis.user.dto.model.StudentCourse;
import com.example.mongoRedis.user.dto.model.TeacherClass;
import com.example.mongoRedis.user.dto.model.User;
import com.example.mongoRedis.user.dto.request.*;
import com.example.mongoRedis.user.dto.response.UserResponse;
import com.example.mongoRedis.user.dto.response.UserSummaryResponse;
import com.example.mongoRedis.user.repository.StudentCourseRepository;
import com.example.mongoRedis.user.repository.TeacherClassRepository;
import com.example.mongoRedis.user.repository.UserRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final TeacherClassRepository teacherClassRepository;
    private final CacheManager cacheManager;

    public UserServiceImplementation(UserRepository userRepository, StudentCourseRepository studentCourseRepository,
            TeacherClassRepository teacherClassRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.studentCourseRepository = studentCourseRepository;
        this.teacherClassRepository = teacherClassRepository;
        this.cacheManager = cacheManager;
    }

    // ---------------- CREATE USER ----------------
    @CachePut(value = "users", key = "#result.id")
    public UserResponse createUser(UserRequest request) {

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            userRepository.findByEmail(request.getEmail())
                    .ifPresent(u -> {
                        throw new CustomServiceException("Email already exists");
                    });
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .userType(request.getUserType() != null ? request.getUserType() : UserType.STUDENT)
                .credentials(request.getCredentials())
                .addresses(request.getAddresses())
                .guardian(request.getGuardian())
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        return null;
    }

    // ---------------- GET USER BY ID ----------------
    @Cacheable(value = "users", key = "#id")
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }

    @Override
    public Page<UserSummaryResponse> getAllUsers(Pageable pageable, UserType userType, Boolean isActive) {
        return null;
    }

    @Override
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        return null;
    }

    // ---------------- UPDATE USER ----------------
    @CachePut(value = "users", key = "#id")
    @CacheEvict(value = "users", key = "'allUsers'", allEntries = true)
    public UserResponse updateUser(String id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            userRepository.findByEmail(request.getEmail())
                    .ifPresent(u -> {
                        throw new CustomServiceException("Email already exists");
                    });
            user.setEmail(request.getEmail());
        }

        if (request.getName() != null)
            user.setName(request.getName());
        if (request.getAge() > 0)
            user.setAge(request.getAge());
        if (request.getAddresses() != null)
            user.setAddresses(request.getAddresses());
        if (request.getGuardian() != null)
            user.setGuardian(request.getGuardian());
        if (request.getCredentials() != null)
            user.setCredentials(request.getCredentials());

        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    // ---------------- DELETE USER ----------------
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(String id) {
        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException("User not found");
        userRepository.deleteById(id);
    }

    @Override
    public void activateUser(String id) {

    }

    @Override
    public void deactivateUser(String id) {

    }

    @Override
    public List<UserSummaryResponse> getUsersSummary(UserType userType, int limit) {
        return List.of();
    }

    @Override
    public Page<UserSummaryResponse> searchUsers(String query, Pageable pageable) {
        return null;
    }

    @Override
    public AdminUserController.UserStatisticsResponse getUserStatistics() {
        return null;
    }

    @Override
    public UserResponse getCurrentUserProfile() {
        return null;
    }

    @Override
    public UserResponse updateCurrentUserProfile(ProfileUpdateRequest request) {
        return null;
    }

    @Override
    public void changePassword(PasswordChangeRequest request) {

    }

    @Override
    public ProfileController.ProfileDashboardResponse getProfileDashboardInfo() {
        return null;
    }

    // ---------------- GET ALL USERS ----------------
    public org.springframework.data.domain.Page<UserResponse> getAllUsers(
            org.springframework.data.domain.Pageable pageable) {
        org.springframework.data.domain.Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(this::mapToResponse);
    }

    // ---------------- STUDENT COURSES ----------------
    public List<StudentCourse> getStudentCourses(String studentId, String semester) {
        return studentCourseRepository.findByStudentIdAndSemester(studentId, semester);
    }

    public StudentCourse addStudentCourse(StudentCourse course) {
        return studentCourseRepository.save(course);
    }

    // ---------------- TEACHER CLASSES ----------------
    public List<TeacherClass> getTeacherClasses(String teacherId, String semester) {
        return teacherClassRepository.findByTeacherIdAndSemester(teacherId, semester);
    }

    public TeacherClass addTeacherClass(TeacherClass teacherClass) {
        return teacherClassRepository.save(teacherClass);
    }

    // ---------------- HELPER ----------------
    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .userType(user.getUserType() != null ? UserType.valueOf(user.getUserType().name()) : null)
                .addresses(user.getAddresses() != null ? user.getAddresses() : new ArrayList<>())
                .guardian(user.getGuardian())
                .build();
    }
}
