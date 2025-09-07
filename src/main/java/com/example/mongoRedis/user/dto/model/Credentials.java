package com.example.mongoRedis.user.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credentials {
    private String username;  // optional, unique
    private String password;  // hashed
    private List<String> roles;  // e.g., ROLE_STUDENT, ROLE_TEACHER, ROLE_ADMIN
}