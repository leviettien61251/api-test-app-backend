package com.example.apitestappbackend.DTO.EdgeDensityTest;

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
public class EdgeDensityTestResponse extends Response {
    private EdgeDensityTestData data;
    private Boolean usedInTest;
    private Timestamp timestamp;
}
