package com.sunasterisk.employeemanangement.dto;

/**
 * DTO trả về sau khi đăng nhập / đăng ký thành công.
 *
 * @param token    JWT Bearer token
 * @param username Tên đăng nhập
 * @param role     Vai trò (ROLE_USER / ROLE_ADMIN)
 */
public record AuthResponse(
        String token,
        String username,
        String role
) {
}
