package com.example.apitestappbackend.controllers;

import com.example.apitestappbackend.DTO.LoginTest.LoginRequest;
import com.example.apitestappbackend.DTO.LoginTest.LoginResponse;
import com.example.apitestappbackend.models.LoggedInUsers;
import com.example.apitestappbackend.services.LoggedInUsersService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class LoggedInUsersController {
    private final LoggedInUsersService loggedInUsersService;

    public LoggedInUsersController(LoggedInUsersService loggedInUsersService) {
        this.loggedInUsersService = loggedInUsersService;
    }

    @GetMapping("/logged-in-users")
    public List<LoggedInUsers> getAll() {
        return loggedInUsersService.findAll();
    }

    @PostMapping("generate-relogin-data")
    public HttpEntity<String> generateReloginData() {
        loggedInUsersService.reloginEvenPhoneNumbersAndDivisionBy16();
        return ResponseEntity.ok("Successfully generated and saved login records");
    }

    @PostMapping("generate-login-data")
    public HttpEntity<String> generateLoginData() {
        loggedInUsersService.generateLoginData();
        return ResponseEntity.ok("Successfully generated and saved login records");
    }

    @PostMapping("/login")
    public HttpEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request for phone: {}", request.getPhoneNumber());
        LoginResponse res = loggedInUsersService.login(request);

        HttpStatus status = res.getLoginStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(res);
    }
}
