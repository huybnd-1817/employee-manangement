package com.sunasterisk.employeemanangement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data               // Generates getters, setters, equals, hashCode, toString
@NoArgsConstructor  // Generates default constructor
@AllArgsConstructor // Generates all-args constructor
public class Employee {
    private String id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String department;
    private String position;
    private double salary;
    private LocalDate hireDate;
}
