package com.sunasterisk.employeemanangement.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO nhận dữ liệu đăng nhập.
 */
public record LoginRequest(

        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password
) {
}
