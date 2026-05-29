package com.example.apitestappbackend.DTO.EdgeStatusTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EdgeStatusTestRequest {
    @JsonProperty("edge_id")
    private Object edgeId;

    @JsonProperty("occupancy_rate")
    private Object occupancyRate;
}
