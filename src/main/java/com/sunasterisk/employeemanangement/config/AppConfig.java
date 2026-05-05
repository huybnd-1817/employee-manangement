package com.sunasterisk.employeemanangement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    /**
     * Dinh nghia bean FakePasswordEncoder chi danh cho demo/testing.
     */
    @Bean
    public FakePasswordEncoder passwordEncoder() {
        return new FakePasswordEncoder();
    }
}
