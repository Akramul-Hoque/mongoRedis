package com.example.mongoRedis.user.dto.model;

import com.example.mongoRedis.common.UserType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private int age;

    private UserType userType;

    private Credentials credentials;

    private List<Address> addresses;

    private Guardian guardian;
}