package com.sunasterisk.employeemanangement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO nhận dữ liệu từ client khi tạo/cập nhật nhân viên.
 */
@Data
public class EmployeeRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    // nullable: nhân viên có thể chưa thuộc phòng ban nào
    private Long departmentId;
}
