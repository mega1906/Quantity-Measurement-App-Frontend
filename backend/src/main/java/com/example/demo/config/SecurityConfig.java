package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * The SecurityConfig class is responsible for configuring the security settings
 * of the application. It uses Spring Security to:
 *
 * 1. Set up CORS (Cross-Origin Resource Sharing)
 * 2. Disable CSRF protection (required for stateless REST APIs)
 * 3. Configure session management to be stateless
 * 4. Allow all requests without authentication
 * 5. Disable frame options to allow access to the H2 console
 *
 * The class defines two beans:
 * - securityFilterChain: Configures the security filter chain
 * - corsConfigurationSource: Configures CORS settings
 *
 * This configuration is essential for enabling communication between
 * frontend and backend services running on different origins.
 *
 * NOTE:
 * - Suitable for development and testing environments
 * - Should be reviewed and tightened for production use
 *
 * @author Developer
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the SecurityFilterChain for the application.
     *
     * This method:
     * 1. Enables CORS using a custom configuration source
     * 2. Disables CSRF protection (required for REST APIs)
     * 3. Sets session management to stateless mode
     * 4. Allows all HTTP requests without authentication
     * 5. Disables frame options to support the H2 Console
     *
     * @param http the HttpSecurity object used to configure security
     * @return SecurityFilterChain applying the configured security settings
     * @throws Exception if any security configuration error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Enable CORS with custom configuration
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Disable CSRF protection (important for REST APIs)
            .csrf(csrf -> csrf.disable())

            // Configure stateless session management
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Allow all requests without authentication
            .authorizeHttpRequests(authz ->
                authz.anyRequest().permitAll()
            );

        // Disable frame options to allow H2 Console access
        http.headers(headers ->
            headers.frameOptions(frame -> frame.disable())
        );

        return http.build();
    }

    /**
     * Configures CORS settings for the application.
     *
     * This configuration:
     * - Allows requests from specific frontend origins
     * - Permits common HTTP methods (GET, POST, PUT, DELETE, OPTIONS)
     * - Allows all headers
     * - Enables credentials
     * - Applies the configuration to all endpoints
     *
     * @return a CorsConfigurationSource bean used by Spring Security
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed frontend origins
        configuration.setAllowedOrigins(
            Arrays.asList(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://3.107.229.154:3001"
            )
        );

        // Allowed HTTP methods
        configuration.setAllowedMethods(
            Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
            )
        );

        // Allow all headers
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);

        // Apply CORS configuration to all endpoints
        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
