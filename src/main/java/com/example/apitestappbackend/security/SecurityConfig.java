package com.example.apitestappbackend.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/signup").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/refresh-token").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/map/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/map/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/map/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/map/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/**").permitAll()
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/clean/**").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
