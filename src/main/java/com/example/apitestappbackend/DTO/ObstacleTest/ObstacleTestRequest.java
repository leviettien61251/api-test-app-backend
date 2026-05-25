package com.example.apitestappbackend.DTO.ObstacleTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObstacleTestRequest {
    @JsonProperty("route_id")
    private Object routeId;

    private Object type;

    @JsonProperty("x_coordinate")
    private Object xCoordinate;

    @JsonProperty("y_coordinate")
    private Object yCoordinate;

    private Object description;

    @JsonProperty("obstacle_status")
    private Object obstacleStatus;
}
