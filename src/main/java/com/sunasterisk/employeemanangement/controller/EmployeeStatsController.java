package com.sunasterisk.employeemanangement.controller;

import com.sunasterisk.employeemanangement.service.EmployeeStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats/employees")
public class EmployeeStatsController {
    private final EmployeeStatsService employeeStatsService;

    public EmployeeStatsController(EmployeeStatsService employeeStatsService) {
        this.employeeStatsService = employeeStatsService;
    }

    /**
     * GET /api/stats/employees/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getEmployeeCount() {
        long count = employeeStatsService.getEmployeeCount();
        return ResponseEntity.ok(count);
    }
}
