package com.example.mongoRedis.user.dto.request;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private String name;
    private String email;
    private int age;

    private UserType userType;
    private Credentials credentials;     // optional, login info
    private List<Address> addresses;     // optional
    private Guardian guardian;           // optional, only for students
}
