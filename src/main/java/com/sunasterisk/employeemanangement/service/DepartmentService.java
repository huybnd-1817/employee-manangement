package com.sunasterisk.employeemanangement.service;

import com.sunasterisk.employeemanangement.exception.ResourceNotFoundException;
import com.sunasterisk.employeemanangement.model.Department;
import com.sunasterisk.employeemanangement.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * GET /api/departments – Lấy tất cả phòng ban
     */
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    /**
     * GET /api/departments/{id} – Lấy phòng ban theo ID
     */
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
    }

    /**
     * POST /api/departments – Tạo phòng ban mới
     */
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    /**
     * PUT /api/departments/{id} – Cập nhật phòng ban
     */
    public Department updateDepartment(Long id, Department request) {
        Department existing = getDepartmentById(id);
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        return departmentRepository.save(existing);
    }

    /**
     * DELETE /api/departments/{id} – Xóa phòng ban
     */
    public void deleteDepartment(Long id) {
        Department existing = getDepartmentById(id);
        departmentRepository.delete(existing);
    }
}
