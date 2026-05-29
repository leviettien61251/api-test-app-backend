package com.example.apitestappbackend.DTO.ObstacleTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportObstacleRequest {
    @JsonProperty("route_id")
    private Object routeId;

    private Object type;
    private Object x;
    private Object y;
    private Object description;
}
