package com.example.apitestappbackend.controllers;

import com.example.apitestappbackend.DTO.PasswordChanged.PasswordChangedRequest;
import com.example.apitestappbackend.DTO.PasswordChanged.PasswordChangedResponse;
import com.example.apitestappbackend.models.PasswordChanged;
import com.example.apitestappbackend.services.PasswordChangedService;
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
public class PasswordChangedController {
    private final PasswordChangedService passwordChangedService;

    public PasswordChangedController(PasswordChangedService passwordChangedService) {
        this.passwordChangedService = passwordChangedService;
    }

    @GetMapping("/change-password")
    public List<PasswordChanged> getAll(){
        return passwordChangedService.findAll();
    }

    @PostMapping("/change-password")
    public HttpEntity<PasswordChangedResponse> changePassword(@Valid @RequestBody PasswordChangedRequest request) {
        log.info("Change password for phone: {}", request.getPhoneNumber());
        PasswordChangedResponse res = passwordChangedService.changePassword(request);

        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(res);
    }
}
