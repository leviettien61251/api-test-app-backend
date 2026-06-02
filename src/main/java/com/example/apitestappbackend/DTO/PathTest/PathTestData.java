package com.example.apitestappbackend.DTO.PathTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PathTestData {
    private Integer id;
    private String userId;
    private Integer startNodeId;
    private Integer endNodeId;
    private Double totalDistance;
    private Double pathStatus;
}
