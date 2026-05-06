package com.example.apitestappbackend.DTO.GetUserInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserInfoRequest {
    private String phoneNumber;
}
