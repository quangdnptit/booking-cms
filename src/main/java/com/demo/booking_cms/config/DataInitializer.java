package com.demo.booking_cms.config;

import com.demo.booking_cms.entity.User;
import com.demo.booking_cms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@cms.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@cms.com")
                    .fullName("System Admin")
                    .passwordHash(passwordEncoder.encode("password"))
                    .role("ADMIN")
                    .build();

            userRepository.save(admin);
        }
    }
}