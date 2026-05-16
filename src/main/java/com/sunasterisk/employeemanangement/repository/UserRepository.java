package com.sunasterisk.employeemanangement.repository;

import com.sunasterisk.employeemanangement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Tìm người dùng theo username để xác thực
     */
    Optional<User> findByUsername(String username);

    /**
     * Kiểm tra username đã tồn tại chưa trước khi đăng ký
     */
    boolean existsByUsername(String username);
}
