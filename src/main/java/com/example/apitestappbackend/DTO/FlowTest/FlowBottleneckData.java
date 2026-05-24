package com.example.apitestappbackend.DTO.FlowTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowBottleneckData {
    @JsonProperty("edge_name")
    private String edgeName;

    private Double x;

    private Double y;

    private String severity;
}
