package com.example.apitestappbackend.DTO.MapTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapMetaData {
    @JsonProperty("building_code")
    private String buildingCode;

    @JsonProperty("building_name")
    private String buildingName;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("scale_x")
    private Double scaleX;

    @JsonProperty("scale_y")
    private Double scaleY;
}
