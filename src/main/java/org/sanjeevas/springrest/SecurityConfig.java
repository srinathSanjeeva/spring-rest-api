package org.sanjeevas.springrest;

import org.sanjeevas.springrest.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Security configuration for the Employee REST API
 * Implements enterprise-level security best practices
 * 
 * @author Sanjeeva
 * @version 2.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";

    private final SecurityProperties securityProperties;

    public SecurityConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    /**
     * Configure security filter chain with comprehensive security measures
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CORS configuration
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // CSRF configuration - enabled for browser clients, disabled for stateless APIs
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/actuator/health",
                    "/actuator/info",
                    "/actuator/**",
                    "/h2-console/**",
                    "/api/v1/employees/**"
                )
            )

            // Session management - stateless for REST API
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Authorization configuration
            .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers(
                    "/swagger-ui/**", 
                    "/v3/api-docs/**", 
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/actuator/health",
                    "/actuator/info"
                ).permitAll()
                
                // Admin endpoints
                .requestMatchers(
                    "/actuator/**",
                    "/h2-console/**"
                ).hasRole(ROLE_ADMIN)
                
                // API endpoints
                .requestMatchers("/api/v1/employees/**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            
            // HTTP Basic authentication
            .httpBasic(withDefaults())
            
            // Security headers
            .headers(withDefaults())
            
            // Exception handling
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.setContentType("application/json");
                    response.getWriter().write(
                        "{\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}"
                    );
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(403);
                    response.setContentType("application/json");
                    response.getWriter().write(
                        "{\"error\":\"Forbidden\",\"message\":\"Access denied\"}"
                    );
                })
            );

        // Note: H2 console frame options would need to be configured separately in newer Spring Security versions

        return http.build();
    }

    /**
     * Configure CORS settings
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowedOrigins(Arrays.asList(securityProperties.getAllowedOrigins()));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Location", "X-Total-Count"));
        configuration.setMaxAge(3600L); // 1 hour
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        
        return source;
    }

    /**
     * Configure password encoder using BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Strength 12 for better security
    }

    /**
     * Configure in-memory user details manager
     * Note: In production, this should be replaced with a proper user store (database, LDAP, etc.)
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        // Admin user - validate password is set
        String adminPassword = securityProperties.getAdmin().getPassword();
        if (adminPassword == null || adminPassword.isEmpty()) {
            throw new IllegalStateException("Admin password must be set via environment variable APP_SECURITY_ADMIN_PASSWORD");
        }
        
        UserDetails admin = User.builder()
                .username(securityProperties.getAdmin().getUsername())
                .password(passwordEncoder().encode(adminPassword))
                .roles(ROLE_ADMIN, ROLE_USER)
                .build();

        // Regular user - validate password is set
        String userPassword = securityProperties.getUser().getPassword();
        if (userPassword == null || userPassword.isEmpty()) {
            throw new IllegalStateException("User password must be set via environment variable APP_SECURITY_USER_PASSWORD");
        }
        
        UserDetails user = User.builder()
                .username(securityProperties.getUser().getUsername())  
                .password(passwordEncoder().encode(userPassword))
                .roles(ROLE_USER)
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }


}
