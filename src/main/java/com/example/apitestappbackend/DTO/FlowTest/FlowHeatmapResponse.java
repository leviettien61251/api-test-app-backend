package com.example.apitestappbackend.DTO.FlowTest;

import com.example.apitestappbackend.DTO.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FlowHeatmapResponse extends Response {
    private List<FlowHeatmapData> data;
    private Boolean usedInTest;
    private Timestamp timestamp;
}
