package com.sunasterisk.employeemanangement.security;

import com.sunasterisk.employeemanangement.model.User;
import com.sunasterisk.employeemanangement.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation của UserDetailsService — được Spring Security gọi
 * khi cần load thông tin user theo username (trong quá trình xác thực).
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Tìm user trong DB theo username.
     * Trả về UserDetails chuẩn của Spring Security gồm: username, password, danh sách role.
     *
     * @throws UsernameNotFoundException nếu không tìm thấy user
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm user trong DB, ném exception nếu không tồn tại
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + username));

        // Chuyển Role enum → GrantedAuthority (ví dụ: ROLE_ADMIN)
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

        // Trả về UserDetails chuẩn của Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),      // username
                user.getPassword(),      // password đã BCrypt
                List.of(authority)       // danh sách quyền
        );
    }
}
