package com.example.apitestappbackend.DTO.AreaTest;

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
public class AreaTestResponse extends Response {
    private List<AreaTestData> data;
    private Boolean usedInTest;
    private Timestamp timestamp;
}
