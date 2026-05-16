package com.sunasterisk.employeemanangement.model;

/**
 * Enum đại diện cho vai trò người dùng trong hệ thống.
 * ROLE_USER : chỉ được xem danh sách nhân viên (GET).
 * ROLE_ADMIN : toàn quyền CRUD nhân viên.
 */
public enum Role {
    ROLE_USER,
    ROLE_ADMIN
}
