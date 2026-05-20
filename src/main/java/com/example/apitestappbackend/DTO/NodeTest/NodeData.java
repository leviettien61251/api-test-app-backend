package com.example.apitestappbackend.DTO.NodeTest;

import com.example.apitestappbackend.models.hospitaldb.MapTest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeData {
    private Integer id;

    private MapTest mapTest;

    private Double xCoordinate;

    private Double yCoordinate;

    private String type;

    private Boolean isPassable;

}
