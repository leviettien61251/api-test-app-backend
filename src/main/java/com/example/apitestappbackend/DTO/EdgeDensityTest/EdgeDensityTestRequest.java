package com.example.apitestappbackend.DTO.EdgeDensityTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EdgeDensityTestRequest {
    @JsonProperty("edge_id")
    private Object edgeId;

    @JsonProperty("current_count")
    private Object currentCount;

    @JsonProperty("fill_percentage")
    private Object fillPercentage;
}
