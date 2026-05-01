package com.example.apitestappbackend.DTO.SignUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpData {
    private String id;
    private String phoneNumber;
    private Timestamp createdAt;
}
