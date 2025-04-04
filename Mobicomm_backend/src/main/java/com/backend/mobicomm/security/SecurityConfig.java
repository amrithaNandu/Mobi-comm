package com.backend.mobicomm.security;

import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Order(1)
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Explicitly allow registration
                    .requestMatchers("/auth/login").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/plans/**").permitAll()
                .requestMatchers("/user/**").permitAll()
                .requestMatchers("/recharge/**").permitAll()
                .requestMatchers("/log/send-otp", "/log/verify-otp").permitAll()
                .requestMatchers("/userdash/**").authenticated() 
                .anyRequest().authenticated()
            )
            .cors().and() 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow all endpoints
                        .allowedOrigins("http://127.0.0.1:5500") // Allow requests from the frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow specific HTTP methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(false); // Disable credentials (if not needed)
            }
        };
    }
}

//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
//    http.cors().and().csrf().disable() // Disable CSRF and enable CORS
//        .authorizeHttpRequests(auth -> auth
//            .requestMatchers("/plans/**").permitAll() // Allow all access to /plans
//            .requestMatchers("/admin/login").permitAll() // Allow access to /admin/login
//            .requestMatchers("/admin/**").authenticated() // Secure all other /admin endpoints
//            .anyRequest().permitAll() // Allow all other requests
//        )
//        .sessionManagement(session -> session
//            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Use sessions if required
//        )
//        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter
//
//    return http.build();
//}
