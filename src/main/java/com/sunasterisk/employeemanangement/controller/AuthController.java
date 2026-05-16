package com.sunasterisk.employeemanangement.controller;

import com.sunasterisk.employeemanangement.dto.AuthResponse;
import com.sunasterisk.employeemanangement.dto.LoginRequest;
import com.sunasterisk.employeemanangement.dto.RegisterRequest;
import com.sunasterisk.employeemanangement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller xử lý đăng ký và đăng nhập.
 * Tất cả endpoint ở đây đều PUBLIC (không cần JWT).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/register
     * Đăng ký tài khoản mới, trả về JWT token ngay sau khi đăng ký thành công.
     * <p>
     * Request body:
     * {
     * "username": "huy",
     * "password": "123456",
     * "role": "ROLE_USER"   // tuỳ chọn, mặc định ROLE_USER
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        // 201 Created — resource mới (user) đã được tạo
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/auth/login
     * Đăng nhập, trả về JWT token nếu thông tin hợp lệ.
     * <p>
     * Request body:
     * {
     * "username": "huy",
     * "password": "123456"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        // 200 OK
        return ResponseEntity.ok(response);
    }
}
