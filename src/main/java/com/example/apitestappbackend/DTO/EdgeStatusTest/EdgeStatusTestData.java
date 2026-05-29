package com.example.apitestappbackend.DTO.EdgeStatusTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EdgeStatusTestData {
    private Integer id;

    @JsonProperty("edge_id")
    private String edgeId;

    @JsonProperty("occupancy_rate")
    private Double occupancyRate;
}
