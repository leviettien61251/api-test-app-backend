package com.example.apitestappbackend.DTO;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Data
@SuperBuilder
public class Response {
    private String status;
    private String code;
    private String message;
    private Timestamp createdAt;

    public Response() {
        super();
    }

    public Response(String status, String code, String message, Timestamp createdAt) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.createdAt = createdAt;
    }
}
