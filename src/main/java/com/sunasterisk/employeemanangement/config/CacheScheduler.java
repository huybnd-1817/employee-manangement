package com.sunasterisk.employeemanangement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheScheduler {

    private static final Logger log = LoggerFactory.getLogger(CacheScheduler.class);

    /**
     * Manually evict the "employeeCount" cache on demand.
     * TTL-based expiry is handled automatically by Caffeine (expireAfterWrite = 1 minute).
     */
    @CacheEvict(value = CacheConfig.EMPLOYEE_COUNT_CACHE, allEntries = true)
    public void evictEmployeeCountCache() {
        log.debug("Cache '{}' manually evicted", CacheConfig.EMPLOYEE_COUNT_CACHE);
    }

    /**
     * Manually evict the "employeeDeptStats" cache on demand.
     */
    @CacheEvict(value = CacheConfig.EMPLOYEE_DEPT_STATS_CACHE, allEntries = true)
    public void evictEmployeeDeptStatsCache() {
        log.debug("Cache '{}' manually evicted", CacheConfig.EMPLOYEE_DEPT_STATS_CACHE);
    }

    /**
     * Logs "System running" to console every 30 seconds.
     */
    @Scheduled(fixedRate = 30_000)
    public void logSystemRunning() {
        log.info("System running");
    }
}
