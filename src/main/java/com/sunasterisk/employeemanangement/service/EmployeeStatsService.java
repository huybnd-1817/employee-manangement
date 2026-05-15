package com.sunasterisk.employeemanangement.service;

import com.sunasterisk.employeemanangement.config.CacheConfig;
import com.sunasterisk.employeemanangement.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeStatsService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeStatsService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeStatsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * GET /api/stats/employees/count - Lấy tổng số nhân viên
     */
    @Cacheable(CacheConfig.EMPLOYEE_COUNT_CACHE)
    public long getEmployeeCount() {
        long count = employeeRepository.count();
        log.info("Total number of employees: {}", count);
        return count;
    }
}
