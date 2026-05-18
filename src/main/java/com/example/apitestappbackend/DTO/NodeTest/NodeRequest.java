package com.example.apitestappbackend.DTO.NodeTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeRequest {
    private Object mapId;

    private Object xCoordinate;

    private Object yCoordinate;

    private Object type;

    private Object isPassable;
}
