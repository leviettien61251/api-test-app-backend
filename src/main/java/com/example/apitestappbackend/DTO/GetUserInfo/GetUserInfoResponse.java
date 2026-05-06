package com.example.apitestappbackend.DTO.GetUserInfo;


import com.example.apitestappbackend.DTO.Response;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Data
@SuperBuilder
public class GetUserInfoResponse extends Response {
    private Timestamp timestamp;
    private Boolean usedInTest;
    private GetUserInfoData data;

    public GetUserInfoResponse(Timestamp timestamp, Boolean usedInTest) {
        this.timestamp = timestamp;
        this.usedInTest = usedInTest;
    }

    public GetUserInfoResponse(String status, String code, String message, Timestamp createdAt, Timestamp timestamp, Boolean usedInTest) {
        super(status, code, message, createdAt);
        this.timestamp = timestamp;
        this.usedInTest = usedInTest;
    }
}
