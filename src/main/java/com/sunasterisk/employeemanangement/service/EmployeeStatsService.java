package com.sunasterisk.employeemanangement.service;

import com.sunasterisk.employeemanangement.config.CacheConfig;
import com.sunasterisk.employeemanangement.dto.DepartmentStatDto;
import com.sunasterisk.employeemanangement.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeStatsService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeStatsService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeStatsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Lấy tổng số nhân viên trong hệ thống.
     * Kết quả được cache trong 1 phút.
     */
    @Cacheable(CacheConfig.EMPLOYEE_COUNT_CACHE)
    public long getEmployeeCount() {
        long count = employeeRepository.countAllEmployees();
        log.info("Total number of employees: {}", count);
        return count;
    }

    /**
     * Thống kê số lượng nhân viên theo từng phòng ban.
     * Kết quả được cache trong 1 phút.
     */
    @Cacheable(CacheConfig.EMPLOYEE_DEPT_STATS_CACHE)
    public List<DepartmentStatDto> getEmployeeCountByDepartment() {
        List<DepartmentStatDto> stats = employeeRepository.countEmployeesByDepartment();
        log.info("Employee count by department: {} departments", stats.size());
        return stats;
    }
}
