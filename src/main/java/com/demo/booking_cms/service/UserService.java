package com.demo.booking_cms.service;

import com.demo.booking_cms.dto.request.UserRequest;
import com.demo.booking_cms.dto.response.UserResponse;
import com.demo.booking_cms.entity.User;
import com.demo.booking_cms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public UserResponse findById(UUID id) {
        return userRepository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    public UserResponse create(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already strongly in use");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(hashedPassword)
                .role(request.getRole() != null ? request.getRole() : "CUSTOMER")
                .build();
                
        return mapToResponse(userRepository.save(user));
    }

    public UserResponse update(UUID id, UserRequest request) {
        return userRepository.findById(id).map(user -> {
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            }
            if (request.getRole() != null) {
                user.setRole(request.getRole());
            }
            return mapToResponse(userRepository.save(user));
        }).orElse(null);
    }

    public boolean delete(UUID id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
