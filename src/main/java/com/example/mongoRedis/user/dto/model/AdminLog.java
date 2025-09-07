package com.example.mongoRedis.user.dto.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "admin_logs")
public class AdminLog {
    @Id
    private String id;

    private String adminId;  // reference to User.id

    private String action;   // e.g., "CREATE_USER", "UPDATE_COURSE"
    private Date timestamp;
}
