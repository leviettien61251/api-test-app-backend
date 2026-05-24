package com.example.apitestappbackend.security;

import com.example.apitestappbackend.repository.LoggedOutUserRepository;
import com.example.apitestappbackend.repository.UserTestRepository;
import com.example.apitestappbackend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserTestRepository userTestRepository;
    private final LoggedOutUserRepository loggedOutUserRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   UserTestRepository userTestRepository,
                                   LoggedOutUserRepository loggedOutUserRepository) {
        this.jwtUtil = jwtUtil;
        this.userTestRepository = userTestRepository;
        this.loggedOutUserRepository = loggedOutUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7).trim();
        if (token.isBlank()
                || !jwtUtil.validateToken(token)
                || !userTestRepository.existsByToken(token)
                || loggedOutUserRepository.existsByInvalidatedToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String phoneNumber = jwtUtil.extractPhoneNumber(token);
        if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(phoneNumber, null, List.of());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
