package com.sunasterisk.employeemanangement.controller;

import com.sunasterisk.employeemanangement.dto.DepartmentStatDto;
import com.sunasterisk.employeemanangement.service.EmployeeStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stats/employees")
public class EmployeeStatsController {
    private final EmployeeStatsService employeeStatsService;

    public EmployeeStatsController(EmployeeStatsService employeeStatsService) {
        this.employeeStatsService = employeeStatsService;
    }

    /**
     * GET /api/stats/employees/count
     * Trả về tổng số nhân viên trong hệ thống.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getEmployeeCount() {
        long count = employeeStatsService.getEmployeeCount();
        return ResponseEntity.ok(count);
    }

    /**
     * GET /api/stats/employees/by-department
     * Trả về thống kê số lượng nhân viên theo từng phòng ban.
     */
    @GetMapping("/by-department")
    public ResponseEntity<List<DepartmentStatDto>> getEmployeeCountByDepartment() {
        List<DepartmentStatDto> stats = employeeStatsService.getEmployeeCountByDepartment();
        return ResponseEntity.ok(stats);
    }
}
