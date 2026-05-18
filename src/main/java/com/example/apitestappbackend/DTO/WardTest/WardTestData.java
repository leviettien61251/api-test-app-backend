package com.example.apitestappbackend.DTO.WardTest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WardTestData {
    private Integer id;

    private String name;

    @JsonProperty("map_node_id")
    private Integer mapNodeId;

    @JsonProperty("map_id")
    private Integer mapId;

    @JsonProperty("ward_status")
    private String wardStatus;
}
