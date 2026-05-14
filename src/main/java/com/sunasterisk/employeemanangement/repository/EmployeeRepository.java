package com.sunasterisk.employeemanangement.repository;

import com.sunasterisk.employeemanangement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
