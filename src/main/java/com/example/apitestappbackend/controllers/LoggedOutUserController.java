package com.example.apitestappbackend.controllers;

import com.example.apitestappbackend.DTO.LogoutTest.LoggedOutUserResponse;
import com.example.apitestappbackend.services.LoggedOutUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LoggedOutUserController {
    private final LoggedOutUserService loggedOutUserService;

    public LoggedOutUserController(LoggedOutUserService loggedOutUserService) {
        this.loggedOutUserService = loggedOutUserService;
    }

    @PostMapping("/test-logout-data")
    public HttpEntity<String> generateLogoutData() {
        loggedOutUserService.testLogoutData();

        return ResponseEntity.ok("Successfully generated and saved logout records");
    }


    @PostMapping("/logout")
    public HttpEntity<LoggedOutUserResponse> logout(HttpServletRequest header) {

        String authHeader = header.getHeader("Authorization");

        LoggedOutUserResponse res = loggedOutUserService.logout(authHeader);

        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(res);

    }

    @DeleteMapping("/clean/logout")
    public HttpEntity<String> cleanLogoutData() {
        return ResponseEntity.ok(loggedOutUserService.cleanLogoutData());
    }
}
