package com.example.mongoRedis.user.dto.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "student_courses")
public class StudentCourse {
    @Id
    private String id;

    private String studentId;  // reference to User.id

    private String courseCode;
    private String courseName;
    private String semester;
    private String academicYear;

    private String status;  // enrolled, completed, dropped
}
