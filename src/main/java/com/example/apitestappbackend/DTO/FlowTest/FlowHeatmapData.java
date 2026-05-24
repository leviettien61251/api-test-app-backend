package com.example.apitestappbackend.DTO.FlowTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowHeatmapData {
    private Double x;
    private Double y;
    private Double value;
    private String message;
    private Double radius;
}
