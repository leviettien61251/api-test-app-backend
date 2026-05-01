package com.example.apitestappbackend.DTO.PasswordChanged;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangedRequest {
    private String phoneNumber;
    private String oldPassword;
    private String newPassword;
}
