package com.example.apitestappbackend.DTO.SignUp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {
    private String signupStatus;
    private Timestamp signupTimestamp;
    private String code;
    private String message;
    private Boolean usedInTest;
    private SignUpData data;
}
