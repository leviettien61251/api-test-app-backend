package com.example.apitestappbackend.DTO.StepTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepEdgeData {
    private Integer id;

    @JsonProperty("start_node_id")
    private Integer startNodeId;

    @JsonProperty("end_node_id")
    private Integer endNodeId;

    private Double distance;

    private String direction;

    private String instruction;
}
