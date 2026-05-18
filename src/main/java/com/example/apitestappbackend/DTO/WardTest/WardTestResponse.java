package com.example.apitestappbackend.DTO.WardTest;

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
public class WardTestResponse extends Response {
    private List<WardTestData> data;
    private Boolean usedInTest;
    private Timestamp timestamp;
}
