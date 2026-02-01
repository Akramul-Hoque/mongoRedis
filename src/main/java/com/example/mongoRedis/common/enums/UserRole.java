package com.example.mongoRedis.common.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN", "Full system access"),
    TEACHER("ROLE_TEACHER", "Academic staff access"),
    STUDENT("ROLE_STUDENT", "Student access"),
    STAFF("ROLE_STAFF", "Administrative staff access");

    private final String authority;
    private final String description;

    UserRole(String authority, String description) {
        this.authority = authority;
        this.description = description;
    }
}
