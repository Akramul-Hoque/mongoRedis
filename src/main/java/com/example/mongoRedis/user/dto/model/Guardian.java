package com.example.mongoRedis.user.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guardian {
    private String name;
    private String relation;  // father, mother
    private String contactNumber;
    private String email;
}