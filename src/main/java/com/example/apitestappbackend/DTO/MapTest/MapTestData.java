package com.example.apitestappbackend.DTO.MapTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapTestData {
    private Integer id;

    private String buildingCode;

    private String buildingName;

    private String imageUrl;

    private Double scaleX;

    private Double scaleY;
}
