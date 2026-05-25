package com.example.apitestappbackend.DTO.HeatmapDataTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeatmapDataTestRequest {
    @JsonProperty("route_id")
    private Object routeId;

    private Object x;
    private Object y;

    @JsonProperty("density_value")
    private Object densityValue;

    @JsonProperty("status_message")
    private Object statusMessage;

    private Object radius;
}
