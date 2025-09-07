package com.example.mongoRedis.user.dto.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "teacher_classes")
public class TeacherClass {
    @Id
    private String id;

    private String teacherId;  // reference to User.id

    private String classCode;
    private String className;
    private String semester;
    private String academicYear;

    private List<String> studentIds;  // students in the class
}

