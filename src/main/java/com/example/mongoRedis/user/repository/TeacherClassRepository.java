package com.example.mongoRedis.user.repository;

import com.example.mongoRedis.user.dto.model.TeacherClass;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TeacherClassRepository extends MongoRepository<TeacherClass,String> {
    List<TeacherClass> findByTeacherIdAndSemester(String teacherId, String semester);
}
