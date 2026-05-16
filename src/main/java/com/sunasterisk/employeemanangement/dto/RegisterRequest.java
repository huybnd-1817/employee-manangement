package com.sunasterisk.employeemanangement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO nhận dữ liệu đăng ký tài khoản mới.
 */
public record RegisterRequest(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username phải từ 3–50 ký tự")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password requires at least 6 characters")
        String password,

        /**
         *  Vai trò: ROLE_USER hoặc ROLE_ADMIN. Nếu null sẽ mặc định là ROLE_USER
         */
        String role
) {
}
