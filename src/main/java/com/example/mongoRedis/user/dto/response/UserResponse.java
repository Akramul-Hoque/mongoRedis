package com.example.mongoRedis.user.dto.response;

import com.example.mongoRedis.common.UserType;
import com.example.mongoRedis.user.dto.model.Address;
import com.example.mongoRedis.user.dto.model.Credentials;
import com.example.mongoRedis.user.dto.model.Guardian;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    private UserType userType;
    private Credentials credentials;
    private List<Address> addresses;
    private Guardian guardian;
}