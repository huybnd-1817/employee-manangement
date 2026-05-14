package com.sunasterisk.employeemanangement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO nhận dữ liệu từ client khi tạo/cập nhật nhân viên.
 */
@Data
public class EmployeeRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Size(max = 150, message = "Email must be at most 150 characters")
    @Email(message = "Email is invalid")
    private String email;

    // nullable: nhân viên có thể chưa thuộc phòng ban nào
    private Long departmentId;
}
