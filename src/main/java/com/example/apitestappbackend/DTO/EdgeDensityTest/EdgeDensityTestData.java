package com.example.apitestappbackend.DTO.EdgeDensityTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EdgeDensityTestData {
    private Integer id;

    @JsonProperty("edge_id")
    private String edgeId;

    @JsonProperty("current_count")
    private Integer currentCount;

    @JsonProperty("fill_percentage")
    private String fillPercentage;
}
