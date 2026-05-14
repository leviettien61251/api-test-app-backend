package com.example.apitestappbackend.DTO.StepTest;

import com.example.apitestappbackend.DTO.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StepTestResponse extends Response {
    private List<?> data;
    private Boolean usedInTest;
    private Timestamp timestamp;

}
