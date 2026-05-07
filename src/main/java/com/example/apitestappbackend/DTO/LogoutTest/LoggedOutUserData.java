package com.example.apitestappbackend.DTO.LogoutTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedOutUserData {
    private String id;
    private String phoneNumber;
    private String invalidatedToken;
}
