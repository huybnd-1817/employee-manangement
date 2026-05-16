package com.sunasterisk.employeemanangement.service;

import com.sunasterisk.employeemanangement.dto.AuthResponse;
import com.sunasterisk.employeemanangement.dto.LoginRequest;
import com.sunasterisk.employeemanangement.dto.RegisterRequest;
import com.sunasterisk.employeemanangement.model.Role;
import com.sunasterisk.employeemanangement.model.User;
import com.sunasterisk.employeemanangement.repository.UserRepository;
import com.sunasterisk.employeemanangement.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Xử lý logic đăng ký và đăng nhập.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Đăng ký tài khoản mới.
     * - Kiểm tra username chưa tồn tại
     * - Mã hoá password bằng BCrypt
     * - Lưu user vào DB
     * - Trả về JWT token
     *
     * @throws IllegalArgumentException nếu username đã tồn tại
     */
    public AuthResponse register(RegisterRequest request) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username '" + request.username() + "' is already in use");
        }

        // Parse role từ request, mặc định ROLE_USER nếu không truyền
        Role role;
        try {
            role = (request.role() != null && !request.role().isBlank())
                    ? Role.valueOf(request.role().toUpperCase())
                    : Role.ROLE_USER;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Role is invalid: " + request.role()
                    + ". Only accept: ROLE_USER, ROLE_ADMIN");
        }

        // Tạo entity User với password đã mã hoá BCrypt
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);

        userRepository.save(user);

        // Load UserDetails để generate JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }

    /**
     * Đăng nhập.
     * - AuthenticationManager xác thực username + password qua DaoAuthenticationProvider
     * - Nếu sai thông tin → Spring tự ném BadCredentialsException
     * - Trả về JWT token
     */
    public AuthResponse login(LoginRequest request) {
        // Xác thực username + password — ném exception nếu sai
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // Xác thực thành công → load user để generate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        String token = jwtUtil.generateToken(userDetails);

        // Lấy role để trả về trong response
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");

        return new AuthResponse(token, request.username(), role);
    }
}



