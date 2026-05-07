package com.example.apitestappbackend.DTO.LoginTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String phoneNumber;
    private String password;
}
