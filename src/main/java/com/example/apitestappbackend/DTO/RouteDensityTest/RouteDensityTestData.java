package com.example.apitestappbackend.DTO.RouteDensityTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDensityTestData {
    private Integer id;

    @JsonProperty("route_id")
    private String routeId;

    @JsonProperty("current_people")
    private Integer currentPeople;
}
