package com.sunasterisk.employeemanangement.config;

import com.sunasterisk.employeemanangement.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Cấu hình Spring Security:
 * - Stateless session (JWT, không dùng HTTP session)
 * - Phân quyền URL theo role ADMIN / USER
 * - Đăng ký JwtAuthFilter trước UsernamePasswordAuthenticationFilter
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cấu hình chuỗi filter và phân quyền.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF vì dùng JWT stateless (không cần CSRF token)
                .csrf(AbstractHttpConfigurer::disable)

                // Tắt HTTP Basic Authentication mặc định
                .httpBasic(AbstractHttpConfigurer::disable)

                // Không lưu session — mỗi request xác thực độc lập qua JWT
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Phân quyền theo URL
                .authorizeHttpRequests(auth -> auth

                        // Cho phép tất cả truy cập vào endpoint đăng ký / đăng nhập
                        .requestMatchers("/api/auth/**").permitAll()

                        // Trang Thymeleaf công khai — không cần đăng nhập
                        .requestMatchers("/employees/**").permitAll()

                        // USER + ADMIN được xem thống kê (GET)
                        .requestMatchers(HttpMethod.GET, "/api/stats/**").hasAnyRole("USER", "ADMIN")

                        // USER + ADMIN được xem danh sách nhân viên & thống kê (GET)
                        .requestMatchers(HttpMethod.GET, "/api/employees/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/stats/**").hasAnyRole("USER", "ADMIN")

                        // Chỉ ADMIN được tạo / sửa / xoá nhân viên (POST, PUT, DELETE)
                        .requestMatchers(HttpMethod.POST, "/api/employees/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/employees/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")

                        // Tất cả request còn lại bắt buộc phải xác thực
                        .anyRequest().authenticated()
                )

                // Gắn JwtAuthFilter chạy trước filter xác thực mặc định của Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * AuthenticationProvider: dùng DB + BCrypt để xác thực.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Spring Security 6.3+: UserDetailsService bắt buộc truyền vào constructor
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * AuthenticationManager: dùng trong AuthService để thực hiện login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}

