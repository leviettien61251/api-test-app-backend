package com.example.apitestappbackend.DTO.Login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginData {
    private String id;
    private String phoneNumber;
    private Timestamp createdAt;
}
