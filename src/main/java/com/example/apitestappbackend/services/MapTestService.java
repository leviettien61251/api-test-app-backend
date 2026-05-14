package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.MapTest.MapTestData;
import com.example.apitestappbackend.DTO.MapTest.MapTestRequest;
import com.example.apitestappbackend.DTO.MapTest.MapTestResponse;
import com.example.apitestappbackend.DTO.StepTest.StepTestData;
import com.example.apitestappbackend.DTO.StepTest.StepTestRequest;
import com.example.apitestappbackend.DTO.StepTest.StepTestResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.MapTest;
import com.example.apitestappbackend.models.NodeTest;
import com.example.apitestappbackend.models.StepTest;
import com.example.apitestappbackend.repository.MapTestRepository;
import com.example.apitestappbackend.repository.NodeTestRepository;
import com.example.apitestappbackend.repository.StepTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MapTestService {
    private final MapTestRepository mapTestRepository;
    private final StepTestRepository stepTestRepository;
    private final NodeTestRepository nodeTestRepository;

    public MapTestService(MapTestRepository mapTestRepository, StepTestRepository stepTestRepository, NodeTestRepository nodeTestRepository) {
        this.mapTestRepository = mapTestRepository;
        this.stepTestRepository = stepTestRepository;
        this.nodeTestRepository = nodeTestRepository;
    }

    public boolean isImageURLValid(String url) {
        String regex = "^(https?:\\/\\/)?([\\w-]+\\.)+[\\w-]+(\\/[\\w- .\\/?%&=]*)?\\.(jpg|jpeg|png|gif|webp|svg|bmp|ico)(\\?.*)?$";
        return url != null && url.matches(regex);
    }

    public MapTestResponse insertMapTest(MapTestRequest request) {
        MapTest savedMt;
        List<MapTestData> mapTests = new ArrayList<>();
        try {
            if (mapTestRepository.existsByBuildingCode(request.getBuildingCode().trim())) {
                return MapTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.BUILDING_EXISTS.getCode())
                        .message(ResponseCode.BUILDING_EXISTS.getMessage())
                        .usedInTest(false)
                        .build();
            }

            if (request.getBuildingCode().isBlank()) {
                return MapTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }

            if (request.getBuildingName().isBlank()) {
                return MapTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }

            if (request.getScaleX().toString().isBlank() || request.getScaleY().toString().isBlank()) {
                return MapTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }

            if (request.getScaleX().isNaN() || request.getScaleY().isNaN()) {
                return MapTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message(ResponseCode.INVALID_VALUE.getMessage())
                        .usedInTest(false)
                        .build();
            }

            if (request.getImageUrl().isBlank()) {
                return MapTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }

            if (!isImageURLValid(request.getImageUrl().trim())) {
                return MapTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message("Hinh anh khong hop le")
                        .usedInTest(false)
                        .build();
            }

            MapTest mt = new MapTest();
            mt.setBuildingCode(request.getBuildingCode().trim());
            mt.setBuildingName(request.getBuildingName().trim());
            mt.setImageUrl(request.getImageUrl().trim());
            mt.setScaleX(Double.parseDouble(request.getScaleX().toString()));
            mt.setScaleY(Double.parseDouble(request.getScaleY().toString()));
            mt.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            mt.setCode(ResponseCode.SUCCESS.getCode());
            mt.setMessage(ResponseCode.SUCCESS.getMessage());
            mt.setUsedInTest(false);

            savedMt = mapTestRepository.save(mt);

            MapTestData mapTestData = new MapTestData();
            mapTestData.setId(savedMt.getId());
            mapTestData.setBuildingCode(savedMt.getBuildingCode());
            mapTestData.setBuildingName(savedMt.getBuildingName());
            mapTestData.setImageUrl(savedMt.getImageUrl());
            mapTestData.setScaleX(savedMt.getScaleX());
            mapTestData.setScaleY(savedMt.getScaleY());

            mapTests.add(mapTestData);

            return MapTestResponse.builder()
                    .timestamp(savedMt.getTimeStamp())
                    .status(savedMt.getStatus())
                    .code(savedMt.getCode())
                    .message(savedMt.getMessage())
                    .usedInTest(savedMt.getUsedInTest())
                    .data(mapTests)
                    .build();
        } catch (Exception e) {
            log.error("Error mapping test: ", e);
            return MapTestResponse
                    .builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("fail")
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .build();
        }

    }

    public StepTestResponse insertStep(StepTestRequest request) {
        StepTest savedSt;
        List<StepTestData> stepTests = new ArrayList<>();
        try {
            if (request.getMapId().toString().isBlank()) {
                return StepTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_BODY.getCode())
                        .message("Thiếu id map")
                        .usedInTest(false)
                        .build();
            }
            if (request.getStartNodeId().toString().isBlank() || request.getEndNodeId().toString().isBlank()) {
                return StepTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_BODY.getCode())
                        .message("Thiếu id start/ end node")
                        .usedInTest(false)
                        .build();
            }
            if (request.getDistance().toString().isBlank()) {
                return StepTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_BODY.getCode())
                        .message("Thiếu số liệu khoảng cách")
                        .usedInTest(false)
                        .build();
            }
            if (request.getDirection().trim().isBlank()) {
                return StepTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_BODY.getCode())
                        .message("Thiếu hướng đi")
                        .usedInTest(false)
                        .build();
            }
            if (request.getInstruction().trim().isBlank()) {
                return StepTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_BODY.getCode())
                        .message("Thiếu chỉ dẫn")
                        .usedInTest(false)
                        .build();
            }

            MapTest mt = mapTestRepository.findById(request.getMapId()).orElseThrow(
                    () -> new IllegalArgumentException("Map not found")
            );
            NodeTest startNt = nodeTestRepository.findById(request.getStartNodeId()).orElseThrow(
                    () -> new IllegalArgumentException("Start node not found")
            );
            NodeTest endNt = nodeTestRepository.findById(request.getEndNodeId()).orElseThrow(
                    () -> new IllegalArgumentException("End node not found")
            );

            StepTest st = new StepTest();
            st.setMapTest(mt);
            st.setStartNodeId(startNt);
            st.setEndNodeId(endNt);
            st.setDistance(request.getDistance());
            st.setDirection(request.getDirection().trim());
            st.setInstruction(request.getInstruction().trim());
            st.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            st.setCode(ResponseCode.SUCCESS.getCode());
            st.setMessage(ResponseCode.SUCCESS.getMessage());
            st.setUsedInTest(false);
            st.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            savedSt = stepTestRepository.save(st);

            StepTestData stepTestData = new StepTestData();
            stepTestData.setMapTest(savedSt.getMapTest());
            stepTestData.setStartNodeId(savedSt.getStartNodeId());
            stepTestData.setEndNodeId(savedSt.getEndNodeId());
            stepTestData.setDistance(savedSt.getDistance());
            stepTestData.setDirection(savedSt.getDirection());
            stepTestData.setInstruction(savedSt.getInstruction());

            stepTests.add(stepTestData);

            return StepTestResponse
                    .builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .data(stepTests)
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            log.error("Error mapping test: ", e);
            return StepTestResponse
                    .builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("fail")
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .build();
        }
    }

    public StepTestResponse getSteps() {
        try {
            return null;
        } catch (Exception e) {
            log.error("Error when get steps ", e);
            return StepTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("fail")
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .build();
        }
    }

    public MapTestResponse getFloors(String buildingCode) {
        try {
            List<MapTest> mapTests;

            // Nếu có building_code thì lọc, không thì lấy tất cả
            if (buildingCode != null && !buildingCode.isEmpty()) {
                mapTests = mapTestRepository.findByBuildingCode(buildingCode);
            } else {
                mapTests = mapTestRepository.findAll();
            }

            // Xử lý dữ liệu trả về
            if (mapTests.isEmpty()) {
                return MapTestResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("success")
                        .code(ResponseCode.SUCCESS.getCode())
                        .message("Không tìm thấy dữ liệu")
                        .usedInTest(false)
                        .build();
            }

            return MapTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message("Lấy danh sách tầng thành công")
                    .data(mapTests)
                    .usedInTest(false)
                    .build();

        } catch (Exception e) {
            log.error("Error when get floor ", e);
            return MapTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("fail")
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .build();
        }
    }
}
