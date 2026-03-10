package com.demo.booking_cms.controller;

import com.demo.booking_cms.dto.UserRequest;
import com.demo.booking_cms.entity.User;
import com.demo.booking_cms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(hashedPassword)
                .role(request.getRole() != null ? request.getRole() : "CUSTOMER")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable UUID id, @RequestBody UserRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFullName(request.getFullName());
                    user.setEmail(request.getEmail());
                    if (request.getPassword() != null && !request.getPassword().isBlank()) {
                        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
                    }
                    if (request.getRole() != null) {
                        user.setRole(request.getRole());
                    }
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
