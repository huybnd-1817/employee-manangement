package com.sunasterisk.employeemanangement.service;

import com.sunasterisk.employeemanangement.dto.EmployeeRequest;
import com.sunasterisk.employeemanangement.exception.ResourceNotFoundException;
import com.sunasterisk.employeemanangement.model.Department;
import com.sunasterisk.employeemanangement.model.Employee;
import com.sunasterisk.employeemanangement.repository.DepartmentRepository;
import com.sunasterisk.employeemanangement.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UtilityService utilityService;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository,
                           UtilityService utilityService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.utilityService = utilityService;
    }

    /**
     * GET /api/employees – Lấy tất cả nhân viên
     */
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * GET /api/employees/{id} – Lấy nhân viên theo ID
     */
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
    }

    /**
     * Get /api/employees?name={} - Lấy danh sách nhân viên theo tên
     */
    public List<Employee> getEmployeesByName(String name) {
        return employeeRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * GET /api/employees?departmentName={} - Lấy danh sách nhân viên theo departmentName
     */
    public List<Employee> getEmployeesByDepartmentName(String departmentName) {
        return employeeRepository.findByDepartmentNameContainingIgnoreCase(departmentName);
    }

    /**
     * Tìm kiếm nhân viên theo name hoặc departmentName (dùng cho trang web).
     * Nếu cả hai đều trống, trả về toàn bộ danh sách.
     */
    public List<Employee> search(String name, String departmentName) {
        boolean hasName = name != null && !name.isBlank();
        boolean hasDept = departmentName != null && !departmentName.isBlank();

        if (hasName && hasDept) {
            return employeeRepository.findByNameContainingIgnoreCaseAndDepartmentNameContainingIgnoreCase(name, departmentName);
        } else if (hasName) {
            return employeeRepository.findByNameContainingIgnoreCase(name);
        } else if (hasDept) {
            return employeeRepository.findByDepartmentNameContainingIgnoreCase(departmentName);
        } else {
            return employeeRepository.findAll();
        }
    }

    /**
     * POST /api/employees – Tạo nhân viên mới
     */
    public Employee createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        Department department = resolveDepartment(request.getDepartmentId());

        Employee employee = new Employee();
        employee.setName(utilityService.formatFullName(request.getName()));
        employee.setEmail(request.getEmail());
        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }

    /**
     * PUT /api/employees/{id} – Cập nhật nhân viên
     */
    public Employee updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = getEmployeeById(id);

        // Nếu email thay đổi, kiểm tra trùng lặp
        if (!employee.getEmail().equalsIgnoreCase(request.getEmail())
                && employeeRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        Department department = resolveDepartment(request.getDepartmentId());

        employee.setName(utilityService.formatFullName(request.getName()));
        employee.setEmail(request.getEmail());
        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }

    /**
     * DELETE /api/employees/{id} – Xóa nhân viên
     */
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }

    /**
     * Tìm Department theo ID (nullable)
     */
    private Department resolveDepartment(Long departmentId) {
        if (departmentId == null) return null;
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", departmentId));
    }
}
