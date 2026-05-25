package com.example.apitestappbackend.DTO.RouteDensityTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDensityTestRequest {
    @JsonProperty("route_id")
    private Object routeId;

    @JsonProperty("current_people")
    private Object currentPeople;
}
