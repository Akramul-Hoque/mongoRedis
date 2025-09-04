package com.example.mongoRedis.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // Jackson needs this
@AllArgsConstructor
@Builder
public class UserResponse {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String email;
    private int age;
}
