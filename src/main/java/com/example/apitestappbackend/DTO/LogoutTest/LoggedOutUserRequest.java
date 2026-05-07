package com.example.apitestappbackend.DTO.LogoutTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedOutUserRequest {
    private String phoneNumber;
}
