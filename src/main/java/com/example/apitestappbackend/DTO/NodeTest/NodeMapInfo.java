package com.example.apitestappbackend.DTO.NodeTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeMapInfo {
    @JsonProperty("building_name")
    private String buildingName;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("scale_x")
    private Double scaleX;

    @JsonProperty("scale_y")
    private Double scaleY;
}
