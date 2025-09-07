package com.example.mongoRedis.user.repository;

import com.example.mongoRedis.user.dto.model.StudentCourse;
import com.example.mongoRedis.user.dto.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentCourseRepository extends MongoRepository<StudentCourse, String> {
    List<StudentCourse> findByStudentIdAndSemester(String studentId, String semester);
}
