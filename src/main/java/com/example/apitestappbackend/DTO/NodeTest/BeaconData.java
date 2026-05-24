package com.example.apitestappbackend.DTO.NodeTest;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface BeaconData {
    Integer getId();

    @JsonProperty("x_coordinate")
    Double getXCoordinate();

    @JsonProperty("y_coordinate")
    Double getYCoordinate();

    String getType();

    @JsonProperty("ward_name")
    String getWardName();
}
