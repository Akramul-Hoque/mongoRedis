package com.example.mongoRedis.user.dto.model;

import com.example.mongoRedis.common.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String name;

    @Indexed(unique = true, sparse = true)
    private String email;

    private String phone;

    private int age;

    private UserType userType;

    private Credentials credentials;

    private List<Address> addresses;

    private Guardian guardian;

    // Enhanced fields for school management
    private boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
    private String academicYear;
    private String studentId; // For students
    private String employeeId; // For teachers/staff
    private String department; // For teachers/staff
    private List<String> enrolledCourses; // For students
    private List<String> assignedCourses; // For teachers
    private String grade; // For students
    private String section; // For students
}