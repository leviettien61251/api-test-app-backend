package com.example.apitestappbackend.DTO.SetUserInfo;

import com.example.apitestappbackend.DTO.Response;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
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
