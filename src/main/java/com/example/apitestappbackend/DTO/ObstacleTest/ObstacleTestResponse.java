package com.example.apitestappbackend.DTO.ObstacleTest;

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
public class ObstacleTestResponse extends Response {
    private ObstacleTestData data;
    private Boolean usedInTest;
    private Timestamp timestamp;
}
