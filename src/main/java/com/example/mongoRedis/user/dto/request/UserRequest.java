package com.example.mongoRedis.user.dto.request;

import com.example.mongoRedis.common.UserType;
import com.example.mongoRedis.user.dto.model.Address;
import com.example.mongoRedis.user.dto.model.Credentials;
import com.example.mongoRedis.user.dto.model.Guardian;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Min(value = 1, message = "Age must be at least 1")
    private int age;

    @NotNull(message = "User type is required")
    private UserType userType;

    @Valid
    private Credentials credentials; // optional, login info
    private List<Address> addresses; // optional

    @Valid
    private Guardian guardian; // optional, only for students
}
