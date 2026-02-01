package com.example.mongoRedis.user.dto.response;

import com.example.mongoRedis.common.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
    private UserType userType;
    private boolean isActive;
    private LocalDateTime createdAt;
    private String academicYear;
    private String studentId;
    private String employeeId;
    private String department;
    private String grade;
    private String section;
}
