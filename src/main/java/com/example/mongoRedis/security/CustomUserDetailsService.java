package com.example.mongoRedis.security;

import com.example.mongoRedis.user.dto.model.User;
import com.example.mongoRedis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user by email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        log.debug("User found: {}, role: {}", user.getEmail(), user.getUserType());
        
        return CustomUserDetails.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getCredentials().getPassword())
                .role(mapToUserRole(user.getUserType()))
                .isActive(true) // You can add active field to User model
                .build();
    }

    private com.example.mongoRedis.common.enums.UserRole mapToUserRole(com.example.mongoRedis.common.UserType userType) {
        switch (userType) {
            case ADMIN:
                return com.example.mongoRedis.common.enums.UserRole.ADMIN;
            case TEACHER:
                return com.example.mongoRedis.common.enums.UserRole.TEACHER;
            case STUDENT:
                return com.example.mongoRedis.common.enums.UserRole.STUDENT;
            case STAFF:
                return com.example.mongoRedis.common.enums.UserRole.STAFF;
            default:
                throw new IllegalArgumentException("Unknown user type: " + userType);
        }
    }
}
