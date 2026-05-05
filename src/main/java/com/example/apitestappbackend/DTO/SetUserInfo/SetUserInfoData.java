package com.example.apitestappbackend.DTO.SetUserInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetUserInfoData {
    private String id;
    private String fullName;
    private String phoneNumber;
    private String password;
}
