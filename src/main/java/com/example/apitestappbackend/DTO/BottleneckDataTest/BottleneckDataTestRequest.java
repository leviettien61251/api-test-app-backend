package com.example.apitestappbackend.DTO.BottleneckDataTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BottleneckDataTestRequest {
    @JsonProperty("route_id")
    private Object routeId;

    @JsonProperty("edge_name")
    private Object edgeName;

    private Object x;
    private Object y;

    @JsonProperty("occupancy_rate")
    private Object occupancyRate;
}
