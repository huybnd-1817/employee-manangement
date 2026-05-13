package com.sunasterisk.employeemanangement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Loại department ra khỏi toString() để tránh kích hoạt Hibernate lazy proxy
@ToString(exclude = "department")
// Loại department ra khỏi equals()/hashCode() để tránh StackOverflowError
// khi so sánh object có quan hệ JPA
@EqualsAndHashCode(exclude = "department")
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    // optional = true: department có thể NULL (khớp với ON DELETE SET NULL)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;
}
