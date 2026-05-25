package com.example.apitestappbackend.DTO.RouteTest;

import com.example.apitestappbackend.DTO.Response;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.List;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteTestResponse extends Response {
    @JsonIgnore
    private RouteTestData data;
    @JsonIgnore
    private List<RouteTestData> dataList;
    private Boolean usedInTest;
    private Timestamp timestamp;

    @JsonProperty("data")
    public Object getResponseData() {
        return dataList != null ? dataList : data;
    }
}
