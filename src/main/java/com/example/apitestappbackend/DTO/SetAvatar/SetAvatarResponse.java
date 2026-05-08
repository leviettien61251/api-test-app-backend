package com.example.apitestappbackend.DTO.SetAvatar;

import com.example.apitestappbackend.DTO.Response;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;


@Data
@SuperBuilder
public class SetAvatarResponse extends Response {
    private Timestamp timestamp;
    private SetAvatarData data;
    private Boolean usedInTest;
}
