package com.example.apitestappbackend.DTO.GetUserInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserInfoData {
    private String id;
    private String fullName;
    private String phoneNumber;
    private String address;
}
