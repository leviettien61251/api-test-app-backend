package com.example.apitestappbackend.DTO.HeatmapDataTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeatmapDataTestData {
    private Integer id;

    @JsonProperty("route_id")
    private String routeId;

    private Double x;
    private Double y;

    @JsonProperty("density_value")
    private Double densityValue;

    @JsonProperty("status_message")
    private String statusMessage;

    private Double radius;
}
