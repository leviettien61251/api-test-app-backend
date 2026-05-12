package com.example.apitestappbackend.DTO.NodeTest;

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
public class NodeResponse extends Response {
    private NodeData data;
    private Boolean usedInTest;
    private Timestamp timestamp;

}
