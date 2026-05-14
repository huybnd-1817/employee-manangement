package com.sunasterisk.employeemanangement.service;

import com.sunasterisk.employeemanangement.exception.ResourceNotFoundException;
import com.sunasterisk.employeemanangement.model.Department;
import com.sunasterisk.employeemanangement.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);

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
        log.info("Creating department: {}", department.getName());
        Department saved = departmentRepository.save(department);
        log.info("Department created successfully – id={}, name={}", saved.getId(), saved.getName());
        return saved;
    }

    /**
     * PUT /api/departments/{id} – Cập nhật phòng ban
     */
    public Department updateDepartment(Long id, Department request) {
        log.info("Updating department id={}", id);
        Department existing = getDepartmentById(id);
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        Department updated = departmentRepository.save(existing);
        log.info("Department updated successfully – id={}, name={}", updated.getId(), updated.getName());
        return updated;
    }

    /**
     * DELETE /api/departments/{id} – Xóa phòng ban
     */
    public void deleteDepartment(Long id) {
        Department existing = getDepartmentById(id);
        log.info("Deleting department – id={}, name={}", existing.getId(), existing.getName());
        departmentRepository.delete(existing);
        log.info("Department deleted successfully – id={}", id);
    }
}
