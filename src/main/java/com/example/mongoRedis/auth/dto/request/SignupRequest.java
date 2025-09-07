package com.example.mongoRedis.auth.dto.request;

import com.example.mongoRedis.common.UserType;
import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private int age;
    private UserType userType;
}
