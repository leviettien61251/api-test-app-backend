package com.example.apitestappbackend.DTO.PathTest;

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
public class PathTestResponse extends Response {
    private List<PathTestData> data;
    private Boolean usedInTest;
    private Timestamp timestamp;
}
