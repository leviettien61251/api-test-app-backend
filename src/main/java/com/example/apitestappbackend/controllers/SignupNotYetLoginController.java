package com.example.apitestappbackend.controllers;

import com.example.apitestappbackend.DTO.SignUpTest.SignUpRequest;
import com.example.apitestappbackend.DTO.SignUpTest.SignUpResponse;
import com.example.apitestappbackend.models.SignupNotYetLogin;
import com.example.apitestappbackend.services.SignupNotYetLoginService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class SignupNotYetLoginController {
    private final SignupNotYetLoginService signupNotYetLoginService;

    public SignupNotYetLoginController(SignupNotYetLoginService signupNotYetLoginService) {
        this.signupNotYetLoginService = signupNotYetLoginService;
    }

    @GetMapping("/signup-not-yet-login")
    public List<SignupNotYetLogin> getAll() {
        return signupNotYetLoginService.findAll_();
    }

    @GetMapping("/test/signup-not-yet-login")
    public ResponseEntity<?> getAllTest() {

        return ResponseEntity.ok(signupNotYetLoginService.findAll_());
    }

    @PostMapping("/generate10000")
    public HttpEntity<String> generate10000SignUpData() {
        try {
            signupNotYetLoginService.generate10000SignUpData();
            return ResponseEntity.ok("Successfully generated and saved 10000 signup records");
        } catch (Exception e) {
            log.error("Error generating 10000 records: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/signup-not-yet-login")
    public void insert(@RequestBody SignupNotYetLogin s) {
        signupNotYetLoginService.insert(s);
    }

    @PostMapping("/signup")
    public HttpEntity<SignUpResponse> signup(@Valid @RequestBody SignUpRequest request) {
        log.info("Sign up request for phone: {}", request.getPhoneNumber());
        SignUpResponse res = signupNotYetLoginService.signUp(request);

        HttpStatus status = res.getSignupStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(res);
    }

    @PutMapping("/signup-not-yet-login/{id}")
    public SignupNotYetLogin update(@PathVariable String id, @RequestBody SignupNotYetLogin s) {
        return signupNotYetLoginService.update(id, s);
    }

    @DeleteMapping("/signup-not-yet-login/{id}")
    public void delete(@PathVariable String id) {
        signupNotYetLoginService.deleteById(id);
    }

    @DeleteMapping("/signup/clean")
    public HttpEntity<String> cleanSignUpData() {
        return ResponseEntity.ok(signupNotYetLoginService.cleanSignUpData());
    }
}
