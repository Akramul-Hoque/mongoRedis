package com.example.mongoRedis.user.repository;

import com.example.mongoRedis.common.UserType;
import com.example.mongoRedis.user.dto.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    
    // Dashboard statistics methods
    long countByUserType(UserType userType);
    long countByIsActive(boolean isActive);
    long countByUserTypeAndIsActive(UserType userType, boolean isActive);
    long countByCreatedAtAfter(LocalDateTime dateTime);
    
    // User management methods
    Page<User> findByUserType(UserType userType, Pageable pageable);
    Page<User> findByIsActive(boolean isActive, Pageable pageable);
    Page<User> findByUserTypeAndIsActive(UserType userType, boolean isActive, Pageable pageable);
    List<User> findByUserTypeAndIsActive(UserType userType, boolean isActive);
    List<User> findByIsActive(boolean isActive);
    
    // Search methods
    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);
    
    // Additional query methods
    List<User> findByAcademicYear(String academicYear);
    List<User> findByDepartment(String department);
    List<User> findByGrade(String grade);
    List<User> findBySection(String section);
}
