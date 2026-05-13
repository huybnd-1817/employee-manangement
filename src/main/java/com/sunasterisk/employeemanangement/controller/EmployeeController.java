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
     * GET /api/employees
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    /**
     * GET /api/employees/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    /**
     * GET /api/employees?name={}
     */
    @GetMapping(params = "name")
    public ResponseEntity<List<Employee>> getEmployeesByName(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.getEmployeesByName(name));
    }

    /**
     * Get /api/employees?departmentName={}
     */
    @GetMapping(params = "departmentName")
    public ResponseEntity<List<Employee>> getEmployeesByDepartmentName(@RequestParam String departmentName) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartmentName(departmentName));
    }

    /**
     * POST /api/employees
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

    /**
     * PUT /api/employees/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,
                                                   @Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    /**
     * DELETE /api/employees/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
