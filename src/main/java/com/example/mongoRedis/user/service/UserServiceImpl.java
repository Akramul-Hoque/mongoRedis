package com.example.mongoRedis.user.service;

import com.example.mongoRedis.common.UserType;
import com.example.mongoRedis.common.util.PasswordEncoderUtil;
import com.example.mongoRedis.exception.CustomServiceException;
import com.example.mongoRedis.exception.ResourceNotFoundException;
import com.example.mongoRedis.user.controller.AdminUserController.UserStatisticsResponse;
import com.example.mongoRedis.user.controller.ProfileController.ProfileDashboardResponse;
import com.example.mongoRedis.user.dto.model.Address;
import com.example.mongoRedis.user.dto.model.Credentials;
import com.example.mongoRedis.user.dto.model.Guardian;
import com.example.mongoRedis.user.dto.model.User;
import com.example.mongoRedis.user.dto.request.*;
import com.example.mongoRedis.user.dto.response.UserResponse;
import com.example.mongoRedis.user.dto.response.UserSummaryResponse;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoder;

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        log.info("Creating new user with email: {}", request.getEmail());

        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomServiceException("User with email " + request.getEmail() + " already exists");
        }

        // Create credentials
        Credentials credentials = Credentials.builder()
                .username(request.getEmail())
                .password(passwordEncoder.encryptPassword(request.getPassword()))
                .roles(List.of("ROLE_" + request.getUserType().name()))
                .build();

        // Create address if provided
        Address address = null;
        if (request.getStreet() != null || request.getCity() != null) {
            address = Address.builder()
                    .street(request.getStreet())
                    .city(request.getCity())
                    .state(request.getState())
//                    .zipCode(request.getZipCode())
                    .country(request.getCountry())
                    .build();
        }

        // Create guardian if provided
        Guardian guardian = null;
        if (request.getGuardianName() != null) {
            guardian = Guardian.builder()
                    .name(request.getGuardianName())
//                    .phone(request.getGuardianPhone())
                    .email(request.getGuardianEmail())
                    .relation(request.getGuardianRelation())
                    .build();
        }

        // Create user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .age(request.getAge())
                .userType(request.getUserType())
                .credentials(credentials)
                .addresses(address != null ? List.of(address) : null)
                .guardian(guardian)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .academicYear(request.getAcademicYear())
                .studentId(request.getStudentId())
                .employeeId(request.getEmployeeId())
                .department(request.getDepartment())
                .grade(request.getGrade())
                .section(request.getSection())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());

        return mapToUserResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToUserResponse(user);
    }

    @Override
    public Page<UserSummaryResponse> getAllUsers(Pageable pageable, UserType userType, Boolean isActive) {
        log.debug("Getting all users with filters - userType: {}, isActive: {}", userType, isActive);
        
        Page<User> users;
        if (userType != null && isActive != null) {
            users = userRepository.findByUserTypeAndIsActive(userType, isActive, pageable);
        } else if (userType != null) {
            users = userRepository.findByUserType(userType, pageable);
        } else if (isActive != null) {
            users = userRepository.findByIsActive(isActive, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        List<UserSummaryResponse> summaries = users.getContent().stream()
                .map(this::mapToUserSummary)
                .collect(Collectors.toList());

        return new PageImpl<>(summaries, pageable, users.getTotalElements());
    }

    @Override
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        log.info("Updating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check if email is being changed and if it's already taken
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new CustomServiceException("Email " + request.getEmail() + " is already taken");
            }
            user.setEmail(request.getEmail());
        }

        // Update fields
        if (request.getName() != null) user.setName(request.getName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAge() != null) user.setAge(request.getAge());
        if (request.getUserType() != null) user.setUserType(request.getUserType());
        if (request.getAcademicYear() != null) user.setAcademicYear(request.getAcademicYear());
        if (request.getStudentId() != null) user.setStudentId(request.getStudentId());
        if (request.getEmployeeId() != null) user.setEmployeeId(request.getEmployeeId());
        if (request.getDepartment() != null) user.setDepartment(request.getDepartment());
        if (request.getGrade() != null) user.setGrade(request.getGrade());
        if (request.getSection() != null) user.setSection(request.getSection());
        
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());

        return mapToUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(String id) {
        log.info("Deleting user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
        log.info("User deleted successfully with ID: {}", id);
    }

    @Override
    public void activateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        log.info("User activated with ID: {}", id);
    }

    @Override
    public void deactivateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        log.info("User deactivated with ID: {}", id);
    }

    @Override
    public List<UserSummaryResponse> getUsersSummary(UserType userType, int limit) {
        log.debug("Getting users summary - userType: {}, limit: {}", userType, limit);
        
        List<User> users;
        if (userType != null) {
            users = userRepository.findByUserTypeAndIsActive(userType, true);
        } else {
            users = userRepository.findByIsActive(true);
        }
        
        return users.stream()
                .limit(limit)
                .map(this::mapToUserSummary)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserSummaryResponse> searchUsers(String query, Pageable pageable) {
        log.debug("Searching users with query: {}", query);
        
        List<User> users = userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query, pageable);
        
        List<UserSummaryResponse> summaries = users.stream()
                .map(this::mapToUserSummary)
                .collect(Collectors.toList());
        
        return new PageImpl<>(summaries, pageable, summaries.size());
    }

    @Override
    public UserStatisticsResponse getUserStatistics() {
        log.debug("Getting user statistics");
        
        long totalUsers = userRepository.count();
        long totalStudents = userRepository.countByUserType(UserType.STUDENT);
        long totalTeachers = userRepository.countByUserType(UserType.TEACHER);
        long totalStaff = userRepository.countByUserType(UserType.STAFF);
        long totalAdmins = userRepository.countByUserType(UserType.ADMIN);
        long activeUsers = userRepository.countByIsActive(true);
        long inactiveUsers = userRepository.countByIsActive(false);
        
        // Calculate new users (mock implementation - you might want to add actual date filtering)
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        
        long newUsersThisMonth = userRepository.countByCreatedAtAfter(oneMonthAgo);
        long newUsersThisYear = userRepository.countByCreatedAtAfter(oneYearAgo);
        
        return UserStatisticsResponse.builder()
                .totalUsers(totalUsers)
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalStaff(totalStaff)
                .totalAdmins(totalAdmins)
                .activeUsers(activeUsers)
                .inactiveUsers(inactiveUsers)
                .newUsersThisMonth(newUsersThisMonth)
                .newUsersThisYear(newUsersThisYear)
                .build();
    }

    @Override
    public UserResponse getCurrentUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); // This will be the user ID from JWT
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return mapToUserResponse(user);
    }

    @Override
    public UserResponse updateCurrentUserProfile(ProfileUpdateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Update profile fields
        if (request.getName() != null) user.setName(request.getName());
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new CustomServiceException("Email " + request.getEmail() + " is already taken");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        
        // Update address if provided
        if (request.getStreet() != null || request.getCity() != null) {
            Address address = Address.builder()
                    .street(request.getStreet())
                    .city(request.getCity())
                    .state(request.getState())
//                    .zipCode(request.getZipCode())
                    .country(request.getCountry())
                    .build();
            user.setAddresses(List.of(address));
        }
        
        // Update guardian if provided
        if (request.getGuardianName() != null) {
            Guardian guardian = Guardian.builder()
                    .name(request.getGuardianName())
//                    .phone(request.getGuardianPhone())
                    .email(request.getGuardianEmail())
                    .relation(request.getGuardianRelation())
                    .build();
            user.setGuardian(guardian);
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        
        return mapToUserResponse(updatedUser);
    }

    @Override
    public void changePassword(PasswordChangeRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Validate current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getCredentials().getPassword())) {
            throw new CustomServiceException("Current password is incorrect");
        }
        
        // Validate new password confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new CustomServiceException("New password and confirmation do not match");
        }
        
        // Update password
        String encryptedPassword = passwordEncoder.encryptPassword(request.getNewPassword());
        user.getCredentials().setPassword(encryptedPassword);
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
        
        log.info("Password changed for user ID: {}", userId);
    }

    @Override
    public ProfileDashboardResponse getProfileDashboardInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return ProfileDashboardResponse.builder()
                .userName(user.getName())
                .userEmail(user.getEmail())
                .userRole(user.getUserType().name())
                .lastLoginDate(user.getLastLoginAt() != null ? user.getLastLoginAt().toString() : "Never")
                .profileCompletionPercentage(calculateProfileCompletion(user))
                .hasPendingApplications(false) // No pending applications system implemented yet
                .unreadNotifications(0) // No notification system implemented yet
                .academicYear(user.getAcademicYear())
                .departmentOrGrade(user.getDepartment() != null ? user.getDepartment() : user.getGrade())
                .build();
    }

    // Legacy methods for backward compatibility
    @Override
    @Deprecated
    public UserResponse createUser(com.example.mongoRedis.user.dto.request.UserRequest request) {
        // Convert legacy request to new request
        UserCreateRequest newRequest = UserCreateRequest.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .userType(request.getUserType())
                .password("defaultPassword123!") // This should be handled properly
                .build();
        
        return createUser(newRequest);
    }

    @Override
    @Deprecated
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<UserResponse> responses = users.getContent().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
        
        return new PageImpl<>(responses, pageable, users.getTotalElements());
    }

    @Override
    @Deprecated
    public UserResponse updateUser(String id, com.example.mongoRedis.user.dto.request.UserRequest request) {
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .userType(request.getUserType())
                .build();
        
        return updateUser(id, updateRequest);
    }

    // Helper methods
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .age(user.getAge())
                .userType(user.getUserType())
                .credentials(user.getCredentials())
                .addresses(user.getAddresses())
                .guardian(user.getGuardian())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .academicYear(user.getAcademicYear())
                .studentId(user.getStudentId())
                .employeeId(user.getEmployeeId())
                .department(user.getDepartment())
                .enrolledCourses(user.getEnrolledCourses())
                .assignedCourses(user.getAssignedCourses())
                .grade(user.getGrade())
                .section(user.getSection())
                .build();
    }

    private UserSummaryResponse mapToUserSummary(User user) {
        return UserSummaryResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userType(user.getUserType())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .academicYear(user.getAcademicYear())
                .studentId(user.getStudentId())
                .employeeId(user.getEmployeeId())
                .department(user.getDepartment())
                .grade(user.getGrade())
                .section(user.getSection())
                .build();
    }

    private int calculateProfileCompletion(User user) {
        int completedFields = 0;
        int totalFields = 8; // Adjust based on required fields
        
        if (user.getName() != null && !user.getName().trim().isEmpty()) completedFields++;
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) completedFields++;
        if (user.getPhone() != null && !user.getPhone().trim().isEmpty()) completedFields++;
        if (user.getAddresses() != null && !user.getAddresses().isEmpty()) completedFields++;
        if (user.getGuardian() != null && user.getUserType() == UserType.STUDENT) completedFields++;
        if (user.getAcademicYear() != null) completedFields++;
        if (user.getDepartment() != null || user.getGrade() != null) completedFields++;
        if (user.getStudentId() != null || user.getEmployeeId() != null) completedFields++;
        
        return (completedFields * 100) / totalFields;
    }
}
