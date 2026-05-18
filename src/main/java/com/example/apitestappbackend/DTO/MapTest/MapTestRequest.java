package com.example.apitestappbackend.DTO.MapTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapTestRequest {
    private String buildingCode;

    private String buildingName;

    private String imageUrl;

    private Object scaleX;

    private Object scaleY;
}
