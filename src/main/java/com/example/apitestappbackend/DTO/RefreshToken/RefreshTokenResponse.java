package com.example.apitestappbackend.DTO.RefreshToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponse {
    private String status;
    private Timestamp timestamp;
    private String token;
    private String refreshToken;
    private Timestamp tokenExpiresAt;
    private String code;
    private String message;
    private Boolean usedInTest;
}
