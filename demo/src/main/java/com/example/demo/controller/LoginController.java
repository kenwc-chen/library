package com.example.demo.controller;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            // 調用 AuthService 進行登入，並返回 JWT token
            String token = authService.login(loginRequestDto.getPhoneNumber(), loginRequestDto.getPassword());
            return ResponseEntity.ok(token);  // 成功登入後返回 JWT token
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }
}
