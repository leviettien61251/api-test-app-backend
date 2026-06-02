package com.example.apitestappbackend.DTO.HeatmapTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeatmapTestData {
    private Integer id;
    private Integer nodeId;
    private Integer densityScore;
    private Timestamp recordedAt;
}
