package org.sanjeevas.springrest.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Application configuration for caching and JPA auditing
 * 
 * @author Sanjeeva
 * @version 1.0
 */
@Configuration
@EnableCaching
@EnableJpaAuditing
public class ApplicationConfig {

    /**
     * Configure cache manager for application-level caching
     * In production, consider using Redis or other distributed cache
     */
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(
            "employees", "employee", "employeeCount"
        );
        cacheManager.setAllowNullValues(true);
        return cacheManager;
    }
}
