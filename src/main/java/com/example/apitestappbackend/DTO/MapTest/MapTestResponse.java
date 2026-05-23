package com.example.apitestappbackend.DTO.MapTest;


import com.example.apitestappbackend.DTO.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MapTestResponse extends Response {
    private Object data;
    private Timestamp timestamp;
    private Boolean usedInTest;
}
