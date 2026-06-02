package com.example.apitestappbackend.DTO.HeatmapTest;

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
public class HeatmapTestResponse extends Response {
    private List<HeatmapTestData> data;
    private Boolean usedInTest;
    private Timestamp timestamp;
}
