package com.demo.booking_cms.controller;

import com.demo.booking_cms.dto.request.LoginRequest;
import com.demo.booking_cms.dto.response.LoginResponse;
import com.demo.booking_cms.service.AuthService;
import io.micrometer.tracing.Tracer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    private final Tracer tracer;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trace")
    public String trace() {
        return tracer.currentSpan().context().traceId();
    }
}
