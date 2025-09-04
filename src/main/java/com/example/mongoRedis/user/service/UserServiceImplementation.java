package com.example.mongoRedis.user.service;

import com.example.mongoRedis.user.dto.model.User;
import com.example.mongoRedis.user.dto.request.UserRequest;
import com.example.mongoRedis.user.dto.response.UserResponse;
import com.example.mongoRedis.user.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class UserServiceImplementation implements UserService{
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public UserServiceImplementation(UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    // Create user → also put in cache
    @CachePut(value = "users", key = "#result.id")
    public UserResponse createUser(UserRequest request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .build();
        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    // Get single user → cacheable
    @Cacheable(value = "users", key = "#id")
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToResponse(user);
    }

    // Get all users → cacheable
    @Cacheable(value = "allUsers")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Update user → refresh cache
    @CachePut(value = "users", key = "#id")
    public UserResponse updateUser(String id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());

        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    // Delete user → remove from cache
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .build();
    }
}
