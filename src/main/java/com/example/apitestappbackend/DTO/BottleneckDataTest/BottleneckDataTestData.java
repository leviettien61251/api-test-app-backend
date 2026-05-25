package com.example.apitestappbackend.DTO.BottleneckDataTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BottleneckDataTestData {
    private Integer id;

    @JsonProperty("route_id")
    private String routeId;

    @JsonProperty("edge_name")
    private String edgeName;

    private Double x;
    private Double y;

    @JsonProperty("occupancy_rate")
    private Double occupancyRate;
}
