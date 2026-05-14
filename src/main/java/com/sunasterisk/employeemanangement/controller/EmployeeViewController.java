package com.sunasterisk.employeemanangement.controller;

import com.sunasterisk.employeemanangement.dto.EmployeeRequest;
import com.sunasterisk.employeemanangement.model.Employee;
import com.sunasterisk.employeemanangement.service.DepartmentService;
import com.sunasterisk.employeemanangement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * MVC Controller – xử lý các trang web liên quan đến nhân viên.
 */
@Controller
@RequestMapping("/employees")
public class EmployeeViewController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public EmployeeViewController(EmployeeService employeeService,
                                  DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    // =========================================================
    // GET /employees/list  – Hiển thị danh sách, hỗ trợ tìm kiếm
    // =========================================================
    @GetMapping("/list")
    public String listEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String departmentName,
            Model model) {

        List<Employee> employees = employeeService.search(name, departmentName);

        model.addAttribute("employees", employees);
        model.addAttribute("searchName", name != null ? name : "");
        model.addAttribute("searchDepartment", departmentName != null ? departmentName : "");

        return "employees/list";   // templates/employees/list.html
    }

    // =========================================================
    // GET /employees/add  – Hiển thị form thêm nhân viên mới
    // =========================================================
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employeeRequest", new EmployeeRequest());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "employees/add";    // templates/employees/add.html
    }

    // =========================================================
    // POST /employees/add  – Xử lý form thêm nhân viên
    // =========================================================
    @PostMapping("/add")
    public String handleAddForm(
            @Valid @ModelAttribute("employeeRequest") EmployeeRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Nếu có lỗi validation, render lại form
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employees/add";
        }

        try {
            employeeService.createEmployee(request);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm nhân viên thành công!");
            return "redirect:/employees/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employees/add";
        }
    }
}


