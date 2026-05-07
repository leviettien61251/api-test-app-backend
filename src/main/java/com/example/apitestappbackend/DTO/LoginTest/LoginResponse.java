package com.example.apitestappbackend.DTO.LoginTest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String loginStatus;
    private Timestamp loginTimestamp;
    private String token;
    private String refreshToken;
    private Timestamp tokenExpiresAt;
    private String code;
    private String message;
    private Boolean usedInTest;
    private LoginData data;
}
