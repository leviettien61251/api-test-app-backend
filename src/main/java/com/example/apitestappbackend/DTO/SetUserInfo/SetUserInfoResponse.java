package com.example.apitestappbackend.DTO.SetUserInfo;

import com.example.apitestappbackend.DTO.Response;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Data
@SuperBuilder
public class SetUserInfoResponse extends Response {

    private Timestamp timestamp;
    private Boolean usedInTest;
    private SetUserInfoData data;

    public SetUserInfoResponse(Timestamp timestamp, Boolean usedInTest, SetUserInfoData data) {
        this.timestamp = timestamp;
        this.usedInTest = usedInTest;
        this.data = data;
    }

    public SetUserInfoResponse(String status, String code, String message, Timestamp createdAt, Timestamp timestamp, Boolean usedInTest, SetUserInfoData data) {
        super(status, code, message, createdAt);
        this.timestamp = timestamp;
        this.usedInTest = usedInTest;
        this.data = data;
    }
}
