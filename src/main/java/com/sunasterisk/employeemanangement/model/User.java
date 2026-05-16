package com.sunasterisk.employeemanangement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entity đại diện cho tài khoản người dùng trong hệ thống.
 * Implements Serializable để hỗ trợ session replication và caching.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tên đăng nhập — duy nhất trong hệ thống
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Mật khẩu đã được mã hoá (BCrypt)
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Vai trò: ROLE_USER hoặc ROLE_ADMIN
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;
}
