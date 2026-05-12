package com.example.apitestappbackend.DTO.NodeTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeRequest {
    private Integer mapId;

    private Double xCoordinate;

    private Double yCoordinate;

    private String type;

    private Boolean isPassable = true;
}
