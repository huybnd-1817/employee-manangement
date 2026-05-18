package com.sunasterisk.employeemanangement.dto;

/**
 * DTO dùng để trả về thống kê số lượng nhân viên theo phòng ban.
 */
public record DepartmentStatDto(String departmentName, long employeeCount) {
}
