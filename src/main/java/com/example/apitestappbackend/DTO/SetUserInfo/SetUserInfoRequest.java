package com.example.apitestappbackend.DTO.SetUserInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetUserInfoRequest {
    private String fullName;
    private String phoneNumber;
    private String address;
}
