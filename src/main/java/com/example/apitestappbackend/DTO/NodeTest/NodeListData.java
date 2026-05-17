package com.example.apitestappbackend.DTO.NodeTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeListData {
    private Integer id;

    @JsonProperty("x_coordinate")
    private Double xCoordinate;

    @JsonProperty("y_coordinate")
    private Double yCoordinate;

    private String type;

    @JsonProperty("is_passable")
    private Boolean isPassable;
}
