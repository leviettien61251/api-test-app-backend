package com.example.apitestappbackend.DTO.EdgeStatusTest;

import com.example.apitestappbackend.DTO.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EdgeStatusTestResponse extends Response {
    private EdgeStatusTestData data;
    private Boolean usedInTest;
    private Timestamp timestamp;
}
