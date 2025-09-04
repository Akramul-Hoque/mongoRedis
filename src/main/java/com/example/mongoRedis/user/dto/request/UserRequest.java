package com.example.mongoRedis.user.dto.request;

import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String email;
    private int age;
}