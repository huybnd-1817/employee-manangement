package com.sunasterisk.employeemanangement.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO nhận dữ liệu từ client khi tạo nhân viên mới.
 * Tách biệt khỏi model Employee để kiểm soát input độc lập.
 */
@Data
public class EmployeeRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number must be 10-11 digits")
    private String phoneNumber;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Position is required")
    private String position;

    @Positive(message = "Salary must be greater than 0")
    private double salary;

    @NotNull(message = "Hire date is required")
    @PastOrPresent(message = "Hire date cannot be in the future")
    private LocalDate hireDate;
}

