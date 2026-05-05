package com.sunasterisk.employeemanangement.controller;

import com.sunasterisk.employeemanangement.dto.EmployeeRequest;
import com.sunasterisk.employeemanangement.model.Employee;
import com.sunasterisk.employeemanangement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Lấy danh sách tất cả nhân viên.
     * GET /employees
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    /**
     * Thêm nhân viên mới.
     * POST /employees
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        Employee created = employeeService.createEmployee(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }
}
