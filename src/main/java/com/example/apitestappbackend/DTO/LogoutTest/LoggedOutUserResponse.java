package com.example.apitestappbackend.DTO.LogoutTest;


import com.example.apitestappbackend.DTO.Response;
import com.example.apitestappbackend.models.LoggedOutUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LoggedOutUserResponse extends Response {
    private Timestamp timestamp;
    private Boolean usedInTest;
    private LoggedOutUserData data;
}
