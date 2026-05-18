package com.sunasterisk.employeemanangement.repository;

import com.sunasterisk.employeemanangement.dto.DepartmentStatDto;
import com.sunasterisk.employeemanangement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Employee entity.
 * Extends JpaRepository to inherit basic CRUD operations.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Kiểm tra email đã tồn tại trong database chưa.
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Tìm danh sách nhân viên theo name.
     */
    List<Employee> findByNameContainingIgnoreCase(String name);

    /**
     * Tìm danh sách nhân viên theo department.
     */
    List<Employee> findByDepartmentNameContainingIgnoreCase(String departmentName);

    /**
     * Tìm danh sách nhân viên theo name VÀ department.
     */
    List<Employee> findByNameContainingIgnoreCaseAndDepartmentNameContainingIgnoreCase(
            String name, String departmentName);

    /**
     * Thống kê số lượng nhân viên theo từng phòng ban (có nhân viên).
     * Sử dụng LEFT JOIN để bao gồm cả phòng ban chưa có nhân viên.
     */
    @Query("""
            SELECT new com.sunasterisk.employeemanangement.dto.DepartmentStatDto(
                d.name, COUNT(e)
            )
            FROM com.sunasterisk.employeemanangement.model.Department d
            LEFT JOIN com.sunasterisk.employeemanangement.model.Employee e
                ON e.department = d
            GROUP BY d.id, d.name
            ORDER BY COUNT(e) DESC
            """)
    List<DepartmentStatDto> countEmployeesByDepartment();

    /**
     * Tổng số nhân viên trong hệ thống (dùng @Query để minh họa).
     */
    @Query("SELECT COUNT(e) FROM Employee e")
    long countAllEmployees();
}
