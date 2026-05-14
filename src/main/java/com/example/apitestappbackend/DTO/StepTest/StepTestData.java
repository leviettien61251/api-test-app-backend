package com.example.apitestappbackend.DTO.StepTest;

import com.example.apitestappbackend.models.MapTest;
import com.example.apitestappbackend.models.NodeTest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepTestData {
    private MapTest mapTest;

    private NodeTest startNodeId;

    private NodeTest endNodeId;

    private Double distance;

    private String direction;

    private String instruction;
}
