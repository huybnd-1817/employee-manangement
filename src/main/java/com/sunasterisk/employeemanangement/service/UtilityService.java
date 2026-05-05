package com.sunasterisk.employeemanangement.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UtilityService {

    private static final AtomicInteger counter = new AtomicInteger(1);

    /**
     * Format họ tên về dạng viết hoa chữ cái đầu mỗi từ.
     * Ví dụ: "nguyen duc huy" → "Nguyen Duc Huy"
     */
    public String formatFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) return "";
        String[] parts = fullName.trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                result.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return result.toString().trim();
    }

    /**
     * Tạo mã nhân viên tự động.
     * Format: EMP-YYYYMMDD-XXXX (ví dụ: EMP-20260505-0001)
     */
    public String generateEmployeeCode() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String sequence = String.format("%04d", counter.getAndIncrement());
        return "EMP-" + date + "-" + sequence;
    }
}
