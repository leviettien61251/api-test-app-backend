package com.example.apitestappbackend.DTO.StepTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepTestRequest {
    private Integer mapId;

    private Integer startNodeId;

    private Integer endNodeId;

    private Double distance;

    private String direction;

    private String instruction;
}
