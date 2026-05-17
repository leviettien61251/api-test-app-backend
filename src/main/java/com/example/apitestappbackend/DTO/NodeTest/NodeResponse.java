package com.example.apitestappbackend.DTO.NodeTest;

import com.example.apitestappbackend.DTO.Response;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NodeResponse extends Response {
    @JsonIgnore
    private NodeData data;
    @JsonIgnore
    private List<NodeListData> nodeListData;
    @JsonProperty("map_info")
    private NodeMapInfo mapInfo;
    private Boolean usedInTest;
    private Timestamp timestamp;

    @JsonProperty("data")
    public Object getResponseData() {
        return nodeListData != null ? nodeListData : data;
    }
}
