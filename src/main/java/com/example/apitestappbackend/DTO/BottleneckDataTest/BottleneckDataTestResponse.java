package com.example.apitestappbackend.DTO.BottleneckDataTest;

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
public class BottleneckDataTestResponse extends Response {
    private BottleneckDataTestData data;
    private Boolean usedInTest;
    private Timestamp timestamp;
}
