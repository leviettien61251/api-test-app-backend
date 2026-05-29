package com.example.apitestappbackend.DTO.EdgeTest;

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
public class EdgeTestResponse extends Response {
    private EdgeTestData data;
    private Boolean usedInTest;
    private Timestamp timestamp;
}
