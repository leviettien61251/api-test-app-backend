package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.NodeTest.NodeData;
import com.example.apitestappbackend.DTO.NodeTest.NodeRequest;
import com.example.apitestappbackend.DTO.NodeTest.NodeResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.MapTest;
import com.example.apitestappbackend.models.NodeTest;
import com.example.apitestappbackend.repository.MapTestRepository;
import com.example.apitestappbackend.repository.NodeTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Slf4j
@Service
public class NodeTestService {
    private final NodeTestRepository nodeTestRepository;
    private final MapTestRepository mapTestRepository;

    public NodeTestService(NodeTestRepository nodeTestRepository, MapTestRepository mapTestRepository) {
        this.nodeTestRepository = nodeTestRepository;
        this.mapTestRepository = mapTestRepository;
    }

    public NodeResponse insertNodeTest(NodeRequest request) {
        NodeTest savedNt;

        try {
            if (request.getMapId() == null) {
                return NodeResponse.builder()
                        .status("fail")
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }
            if (request.getXCoordinate().toString().isBlank() || request.getYCoordinate().toString().isBlank()) {
                return NodeResponse.builder()
                        .status("fail")
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }
            if (request.getType().isBlank()) {
                return NodeResponse.builder()
                        .status("fail")
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }
            if (request.getIsPassable().toString().isBlank()) {
                return NodeResponse.builder()
                        .status("fail")
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }
            if (request.getXCoordinate().isNaN() || request.getYCoordinate().isNaN()) {
                return NodeResponse.builder()
                        .status("fail")
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message(ResponseCode.INVALID_VALUE.getMessage())
                        .usedInTest(false)
                        .build();
            }

            MapTest mt = mapTestRepository.findById(request.getMapId()).orElse(null);
            if (mt == null) {
                return NodeResponse.builder()
                        .status("fail")
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.FLOOR_NOT_FOUND.getCode())
                        .message(ResponseCode.FLOOR_NOT_FOUND.getMessage())
                        .usedInTest(false)
                        .build();
            }

            NodeTest nt = new NodeTest();
            nt.setMapTest(mt);
            nt.setXCoordinate(Double.parseDouble(request.getXCoordinate().toString()));
            nt.setYCoordinate(Double.parseDouble(request.getYCoordinate().toString()));
            nt.setType(request.getType().trim());
            nt.setIsPassable(Boolean.parseBoolean(request.getIsPassable().toString()));
            nt.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            nt.setStatus("success");
            nt.setCode(ResponseCode.SUCCESS.getCode());
            nt.setMessage(ResponseCode.SUCCESS.getMessage());
            nt.setUsedInTest(false);
            savedNt = nodeTestRepository.save(nt);


            return NodeResponse.builder()
                    .status(savedNt.getStatus())
                    .timestamp(savedNt.getTimeStamp())
                    .code(savedNt.getCode())
                    .message(savedNt.getMessage())
                    .usedInTest(savedNt.getUsedInTest())
                    .data(new NodeData(
                            savedNt.getId(),
                            savedNt.getMapTest(),
                            savedNt.getXCoordinate(),
                            savedNt.getYCoordinate(),
                            savedNt.getType(),
                            savedNt.getIsPassable()
                    ))
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();
        } catch (Exception e) {
            log.error("Error when insert node test: {}", e.getMessage());
            return NodeResponse.builder()
                    .status("fail")
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();
        }

    }
}
