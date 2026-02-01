package com.example.mongoRedis.user.dto.request;

import com.example.mongoRedis.common.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phone;
    
    private Integer age;
    
    private UserType userType;
    
    // School-specific fields
    private String academicYear;
    private String studentId;
    private String employeeId;
    private String department;
    private String grade;
    private String section;
    
    // Address information
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    
    // Guardian information (for students)
    private String guardianName;
    private String guardianPhone;
    private String guardianEmail;
    private String guardianRelation;
    
    private boolean isActive;
}
