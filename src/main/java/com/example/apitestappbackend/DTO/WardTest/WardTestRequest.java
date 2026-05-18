package com.example.apitestappbackend.DTO.WardTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WardTestRequest {
    @JsonProperty("map_node_id")
    private Integer mapNodeId;

    private String name;

    @JsonProperty("ward_status")
    private String wardStatus;
}
