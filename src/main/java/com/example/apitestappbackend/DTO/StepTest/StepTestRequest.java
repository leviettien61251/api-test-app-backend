package com.example.apitestappbackend.DTO.StepTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepTestRequest {
    private Object mapId;

    private Object startNodeId;

    private Object endNodeId;

    private Object distance;

    private Object direction;

    private Object instruction;
}
