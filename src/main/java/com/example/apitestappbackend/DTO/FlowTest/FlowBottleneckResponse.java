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
public class FlowBottleneckResponse extends Response {
    private List<FlowBottleneckData> data;
    private Boolean usedInTest;
    private Timestamp timestamp;
}
