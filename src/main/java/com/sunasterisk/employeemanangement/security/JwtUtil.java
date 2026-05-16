package com.sunasterisk.employeemanangement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Tiện ích tạo, phân tích và xác thực JWT token.
 */
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String secret, // Đọc giá trị của key jwt.secret từ config file
            @Value("${jwt.expiration-ms:3600000}") long expirationMs // Đọc jwt.expiration-ms, nếu không có thì dùng giá trị mặc định 3600000 (1 giờ)
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); // Decode Base64 → bytes → tạo SecretKey
        this.expirationMs = expirationMs;
    }

    // ────────────────────────── Generate ──────────────────────────

    /**
     * Tạo JWT từ UserDetails (subject = username, claim "role").
     */
    public String generateToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority())
                .orElse("");

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    // ────────────────────────── Validate ──────────────────────────

    /**
     * Kiểm tra token hợp lệ và thuộc về userDetails.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // ────────────────────────── Extract ───────────────────────────

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey) // xác thực chữ ký bằng secret key
                .build()
                .parseSignedClaims(token) // giải mã token → JWS object
                .getPayload();  // lấy phần payload (Claims)
        return claimsResolver.apply(claims);
    }
}
