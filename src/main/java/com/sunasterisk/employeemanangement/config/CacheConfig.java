package com.sunasterisk.employeemanangement.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {

    /**
     * Tên cache dùng cho thống kê tổng số nhân viên.
     */
    public static final String EMPLOYEE_COUNT_CACHE = "employeeCount";

    /**
     * CacheManager sử dụng Caffeine với TTL 1 phút.
     * Sau 1 phút kể từ lần ghi cuối, entry sẽ tự động hết hạn.
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(EMPLOYEE_COUNT_CACHE);
        manager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .maximumSize(100)
                        .recordStats()
        );
        return manager;
    }
}
