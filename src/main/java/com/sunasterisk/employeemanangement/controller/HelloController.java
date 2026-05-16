package com.sunasterisk.employeemanangement.controller;

import com.sunasterisk.employeemanangement.service.UtilityService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final UtilityService utilityService;
    private final PasswordEncoder passwordEncoder;

    public HelloController(UtilityService utilityService, PasswordEncoder passwordEncoder) {
        this.utilityService = utilityService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World! The project is running successfully!";
    }

    /**
     * Format ho ten nhan vien va tra ve ma nhan vien moi.
     * Vi du: GET /employee/info?name=nguyen duc huy
     */
    @GetMapping("/employee/info")
    public String getEmployeeInfo(@RequestParam String name) {
        String formattedName = utilityService.formatFullName(name);
        String employeeCode = utilityService.generateEmployeeCode();
        return String.format("Name: %s | Code: %s", formattedName, employeeCode);
    }

    /**
     * Ma hoa mat khau bang BCryptPasswordEncoder.
     * Vi du: GET /encode-password?password=123456
     */
    @GetMapping("/encode-password")
    public String encodePassword(@RequestParam String password) {
        String encoded = passwordEncoder.encode(password);
        return String.format("Raw: %s | Encoded: %s", password, encoded);
    }
}
