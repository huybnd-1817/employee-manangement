package com.sunasterisk.employeemanangement.service;

import com.sunasterisk.employeemanangement.dto.EmployeeRequest;
import com.sunasterisk.employeemanangement.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;

@Service
public class EmployeeService {

    // In-memory storage
    private final List<Employee> employees = new ArrayList<>();

    private final UtilityService utilityService;

    public EmployeeService(UtilityService utilityService) {
        this.utilityService = utilityService;

        // Khởi tạo một số nhân viên mẫu
        employees.add(new Employee(
                utilityService.generateEmployeeCode(),
                utilityService.formatFullName("nguyen van an"),
                "an.nguyen@company.com", "0912345678",
                "Engineering", "Backend Developer",
                15_000_000, LocalDate.of(2022, 3, 1)
        ));
        employees.add(new Employee(
                utilityService.generateEmployeeCode(),
                utilityService.formatFullName("tran thi bich"),
                "bich.tran@company.com", "0987654321",
                "Design", "UI/UX Designer",
                12_000_000, LocalDate.of(2023, 6, 15)
        ));
        employees.add(new Employee(
                utilityService.generateEmployeeCode(),
                utilityService.formatFullName("le minh hoang"),
                "hoang.le@company.com", "0901122334",
                "Engineering", "Frontend Developer",
                13_500_000, LocalDate.of(2021, 9, 10)
        ));
    }

    /**
     * Lấy toàn bộ danh sách nhân viên.
     */
    public List<Employee> getAllEmployees() {
        return Collections.unmodifiableList(employees);
    }

    /**
     * Thêm nhân viên mới vào danh sách.
     */
    public Employee createEmployee(EmployeeRequest request) {
        // Kiểm tra email trùng
        boolean emailExists = employees.stream()
                .anyMatch(e -> e.getEmail().equalsIgnoreCase(request.getEmail()));
        if (emailExists) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        // Map DTO → model, tạo object mới thay vì mutate
        Employee newEmployee = new Employee(
                utilityService.generateEmployeeCode(),
                utilityService.formatFullName(request.getFullName()),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getDepartment(),
                request.getPosition(),
                request.getSalary(),
                request.getHireDate()
        );

        employees.add(newEmployee);
        return newEmployee;
    }
}
