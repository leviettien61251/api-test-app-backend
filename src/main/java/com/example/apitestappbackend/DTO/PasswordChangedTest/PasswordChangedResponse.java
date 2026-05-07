package com.example.apitestappbackend.DTO.PasswordChangedTest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangedResponse {
    private String status;
    private String oldPasswordStatus;     // "PROVIDED" | "NULL" | "EMPTY"
    private String newPasswordStatus;     // "PROVIDED" | "NULL" | "TOO_SHORT" | "SAME_AS_OLD"
    private Timestamp passwordChangedTimestamp;
    private String code;
    private String message;
    private Boolean usedInTest;
    private PasswordChangedData data;
}
