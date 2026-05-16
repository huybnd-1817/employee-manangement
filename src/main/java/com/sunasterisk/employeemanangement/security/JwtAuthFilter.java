package com.sunasterisk.employeemanangement.security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter chạy một lần mỗi request.
 * Đọc JWT từ header "Authorization: Bearer <token>",
 * xác thực và đặt Authentication vào SecurityContext.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {

        // Bước 1: Đọc header Authorization
        final String authHeader = request.getHeader("Authorization");

        // Bước 2: Bỏ qua nếu không có header hoặc không phải Bearer token
        // → request không mang JWT, để Spring Security tự xử lý (có thể là public endpoint)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bước 3: Cắt bỏ "Bearer " (7 ký tự) → lấy chuỗi JWT thuần
        final String jwt = authHeader.substring(7);
        String username;

        // Bước 4: Giải mã token để lấy username
        try {
            username = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            // Token bị giả mạo, sai chữ ký, hoặc hết hạn → không set authentication
            // Spring Security sẽ từ chối request ở bước kiểm tra phân quyền phía sau
            filterChain.doFilter(request, response);
            return;
        }

        // Bước 5: Chỉ xử lý nếu username hợp lệ và chưa có authentication trong SecurityContext
        // (tránh xử lý lại nếu request đã được authenticate trước đó trong cùng chuỗi filter)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Bước 5a: Load thông tin user từ DB (bao gồm roles/authorities)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Bước 5b: Kiểm tra token có khớp với user và chưa hết hạn không
            if (jwtUtil.isTokenValid(jwt, userDetails)) {

                // Bước 5c: Tạo Authentication object
                // - principal   = userDetails (thông tin user)
                // - credentials = null (không cần password sau khi đã xác thực qua JWT)
                // - authorities = danh sách role (ROLE_USER, ROLE_ADMIN...)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                // Gắn thêm thông tin request (IP, session...) vào authentication
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Đặt vào SecurityContext → Spring Security coi request này đã xác thực
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Bước 6: Chuyển tiếp sang filter tiếp theo (hoặc tới Controller nếu đây là filter cuối)
        filterChain.doFilter(request, response);
    }
}
