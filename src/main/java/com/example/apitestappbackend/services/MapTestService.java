package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.MapTest.MapTestData;
import com.example.apitestappbackend.DTO.MapTest.MapTestRequest;
import com.example.apitestappbackend.DTO.MapTest.MapTestResponse;
import com.example.apitestappbackend.DTO.SavedSearch.SavedSearchRequest;
import com.example.apitestappbackend.DTO.SavedSearch.SavedSearchResponse;
import com.example.apitestappbackend.DTO.StepTest.StepEdgeData;
import com.example.apitestappbackend.DTO.StepTest.StepTestData;
import com.example.apitestappbackend.DTO.StepTest.StepTestRequest;
import com.example.apitestappbackend.DTO.StepTest.StepTestResponse;
import com.example.apitestappbackend.DTO.WardTest.WardTestData;
import com.example.apitestappbackend.DTO.WardTest.WardTestRequest;
import com.example.apitestappbackend.DTO.WardTest.WardTestResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.*;
import com.example.apitestappbackend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MapTestService {
    private final MapTestRepository mapTestRepository;
    private final StepTestRepository stepTestRepository;
    private final NodeTestRepository nodeTestRepository;
    private final WardTestRepository wardTestRepository;
    private final SavedSearchRepository savedSearchRepository;
    private final UserTestRepository userTestRepository;

    public MapTestService(MapTestRepository mapTestRepository,
                          StepTestRepository stepTestRepository,
                          NodeTestRepository nodeTestRepository,
                          WardTestRepository wardTestRepository,
                          SavedSearchRepository savedSearchRepository,
                          UserTestRepository userTestRepository) {
        this.mapTestRepository = mapTestRepository;
        this.stepTestRepository = stepTestRepository;
        this.nodeTestRepository = nodeTestRepository;
        this.wardTestRepository = wardTestRepository;
        this.savedSearchRepository = savedSearchRepository;
        this.userTestRepository = userTestRepository;
    }

    public boolean isImageURLValid(String url) {
        String regex = "^(https?:\\/\\/)?([\\w-]+\\.)+[\\w-]+(\\/[\\w- .\\/?%&=]*)?\\.(jpg|jpeg|png|gif|webp|svg|bmp|ico)(\\?.*)?$";
        return url != null && url.matches(regex);
    }

    public void generateMapNodeStepData() {
        try {
            // Create a sample map
            MapTest mt = new MapTest();
            mt.setBuildingCode("B001");
            mt.setBuildingName("Building 1 - Floor 1");
            mt.setImageUrl("https://example.com/map1.png");
            mt.setScaleX(1.0);
            mt.setScaleY(1.0);
            mt.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            mt.setCode(ResponseCode.SUCCESS.getCode());
            mt.setMessage(ResponseCode.SUCCESS.getMessage());
            mt.setUsedInTest(true);

            MapTest savedMap = mapTestRepository.save(mt);

            // Create sample nodes for the map
            NodeTest n1 = new NodeTest();
            n1.setMapTest(savedMap);
            n1.setXCoordinate(10.0);
            n1.setYCoordinate(20.0);
            n1.setType("start");
            n1.setIsPassable(true);
            n1.setCode(ResponseCode.SUCCESS.getCode());
            n1.setMessage(ResponseCode.SUCCESS.getMessage());

            NodeTest n2 = new NodeTest();
            n2.setMapTest(savedMap);
            n2.setXCoordinate(15.0);
            n2.setYCoordinate(25.0);
            n2.setType("end");
            n2.setIsPassable(true);
            n2.setCode(ResponseCode.SUCCESS.getCode());
            n2.setMessage(ResponseCode.SUCCESS.getMessage());

            NodeTest savedN1 = nodeTestRepository.save(n1);
            NodeTest savedN2 = nodeTestRepository.save(n2);

            // Create a sample step connecting the two nodes
            StepTest s1 = new StepTest();
            s1.setMapTest(savedMap);
            s1.setStartNodeId(savedN1);
            s1.setEndNodeId(savedN2);
            s1.setDistance(5.0);
            s1.setDirection("north");
            s1.setInstruction("Walk straight for 5 meters");
            s1.setCode(ResponseCode.SUCCESS.getCode());
            s1.setMessage(ResponseCode.SUCCESS.getMessage());
            s1.setUsedInTest(true);

            stepTestRepository.save(s1);

            log.info("Seeded MapTest (id={}) with 2 nodes and 1 step", savedMap.getId());
        } catch (Exception e) {
            log.error("Error generating map test data: ", e);
        }
    }

    public MapTestResponse generateMapTestDataBuildingAAndB_5() {
        List<MapTest> mapTests = new ArrayList<>();

        try {
            for (int i = 1; i <= 5; i++) {
                MapTest mt = new MapTest();
                mt.setBuildingCode("A");
                mt.setBuildingName("Tòa A - Tầng " + i);
                mt.setImageUrl("https://example.com/a" + i + ".png");
                mt.setScaleX(1.0);
                mt.setScaleY(1.0);
                mt.setTimeStamp(new Timestamp(System.currentTimeMillis()));
                mt.setCode(ResponseCode.SUCCESS.getCode());
                mt.setMessage(ResponseCode.SUCCESS.getMessage());
                mt.setUsedInTest(true);

                mapTests.add(mt);
            }
            mapTestRepository.saveAll(mapTests);
        } catch (Exception e) {
            log.error("Error when generate map test data 5(A: ", e);
        }
        try {
            for (int i = 1; i <= 5; i++) {
                MapTest mt = new MapTest();
                mt.setBuildingCode("B");
                mt.setBuildingName("Tòa B - Tầng " + i);
                mt.setImageUrl("https://example.com/a" + i + ".png");
                mt.setScaleX(1.0);
                mt.setScaleY(1.0);
                mt.setTimeStamp(new Timestamp(System.currentTimeMillis()));
                mt.setCode(ResponseCode.SUCCESS.getCode());
                mt.setMessage(ResponseCode.SUCCESS.getMessage());
                mt.setUsedInTest(true);

                mapTests.add(mt);
            }
            mapTestRepository.saveAll(mapTests);
        } catch (Exception e) {
            log.error("Error when generate map test data 5(B): ", e);
        }

        log.info("Thêm dữ liệu mồi thành công");
        return MapTestResponse
                .builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("success")
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .data(mapTests)
                .usedInTest(true)
                .build();
    }

    public String cleanMapData() {
        mapTestRepository.deleteAllInBatch();
        return "Dọn dẹp dữ liệu Map thành công";
    }

    public String cleanNodeData() {
        nodeTestRepository.deleteAllInBatch();
        return "Dọn dẹp dữ liệu Node thành công";
    }

    public String cleanStepData() {
        stepTestRepository.deleteAllInBatch();
        return "Dọn dẹp dữ liệu Step thành công";
    }

    public String cleanSavedSearchData() {
        savedSearchRepository.deleteAllInBatch();
        return "Dọn dẹp dữ liệu Search thành công";
    }

    public String cleanWardData() {
        wardTestRepository.deleteAllInBatch();
        return "Dọn dẹp dữ liệu Ward thành công";
    }

    public SavedSearchResponse postSavedSearch(SavedSearchRequest request) {
        try {
            if (request == null || request.getKeyword() == null || request.getUserId() == null) {
                return SavedSearchResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message("Thiếu từ khóa tìm kiếm hoặc user_id")
                        .usedInTest(false)
                        .build();
            }

            if (!(request.getKeyword() instanceof String) || !(request.getUserId() instanceof String)) {
                return SavedSearchResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_TYPE.getCode())
                        .message("Sai kiểu dữ liệu")
                        .usedInTest(false)
                        .build();
            }

            String keyword = ((String) request.getKeyword()).trim();
            if (keyword.isBlank()) {
                return SavedSearchResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message("Thiếu từ khóa tìm kiếm hoặc user_id")
                        .usedInTest(false)
                        .build();
            }

            UserTest user = userTestRepository.findById(request.getUserId()).orElse(
                    null
            );
            List<WardTest> wards = wardTestRepository.findByNameContainingIgnoreCase(keyword);
            List<WardTestData> searchData = wards.stream()
                    .map(ward -> new WardTestData(
                            ward.getId(),
                            ward.getName(),
                            ward.getMapNode().getId(),
                            ward.getMapNode().getMapTest().getId(),
                            ward.getWardStatus()
                    ))
                    .toList();

            if (!wards.isEmpty()) {
                SavedSearch savedSearch = new SavedSearch();
                savedSearch.setUserId(user);
                savedSearch.setTargetNode(wards.get(0).getMapNode());
                savedSearch.setKeyword(keyword);
                savedSearch.setSearchedAt(new Timestamp(System.currentTimeMillis()));
                savedSearch.setStatus("success");
                savedSearch.setCode(ResponseCode.SUCCESS.getCode());
                savedSearch.setMessage(ResponseCode.SUCCESS.getMessage());
                savedSearch.setUsedInTest(false);
                savedSearchRepository.save(savedSearch);
            }

            return SavedSearchResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message("Tìm kiếm hoàn tất")
                    .data(searchData)
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            log.error("Error when post saved search: ", e);
            return SavedSearchResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("fail")
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .build();
        }
    }

    public WardTestResponse postWardTest(WardTestRequest request) {
        try {
            if (request == null || request.getMapNodeId() == null || request.getName() == null || request.getName().trim().isBlank()) {
                return WardTestResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }

            NodeTest nodeTest = nodeTestRepository.findById(request.getMapNodeId()).orElse(null);
            if (nodeTest == null) {
                return WardTestResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.NODE_NOT_FOUND.getCode())
                        .message(ResponseCode.NODE_NOT_FOUND.getMessage())
                        .usedInTest(false)
                        .build();
            }

            WardTest wardTest = new WardTest();
            wardTest.setMapNode(nodeTest);
            wardTest.setName(request.getName().trim());
            wardTest.setWardStatus(
                    request.getWardStatus() == null || request.getWardStatus().trim().isBlank()
                            ? "open"
                            : request.getWardStatus().trim()
            );
            wardTest.setStatus("success");
            wardTest.setCode(ResponseCode.SUCCESS.getCode());
            wardTest.setMessage(ResponseCode.SUCCESS.getMessage());
            wardTest.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            wardTest.setUsedInTest(false);

            WardTest savedWard = wardTestRepository.save(wardTest);
            WardTestData wardTestData = new WardTestData(
                    savedWard.getId(),
                    savedWard.getName(),
                    savedWard.getMapNode().getId(),
                    savedWard.getMapNode().getMapTest().getId(),
                    savedWard.getWardStatus()
            );

            return WardTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .data(List.of(wardTestData))
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            log.error("Error when post ward test: ", e);
            return WardTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("fail")
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .build();
        }
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
            if (request.getMapId() == null) {
                return StepTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_BODY.getCode())
                        .message("Map id null")
                        .usedInTest(false)
                        .build();
            }

            if (request.getStartNodeId() == null) {
                return StepTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_BODY.getCode())
                        .message("Start Node id null")
                        .usedInTest(false)
                        .build();
            }

            if (request.getEndNodeId() == null) {
                return StepTestResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_BODY.getCode())
                        .message("End Node id null")
                        .usedInTest(false)
                        .build();
            }

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

    public StepTestResponse getEdges() {
        return StepTestResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(ResponseCode.MISSING_PARAM.getCode())
                .message("Thiếu floor_id")
                .usedInTest(false)
                .build();
    }

    public StepTestResponse getEdges(List<String> floorIds) {
        try {
            if (floorIds == null || floorIds.isEmpty() || floorIds.size() > 1 || floorIds.get(0) == null) {
                return StepTestResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message("Thiếu floor_id")
                        .usedInTest(false)
                        .build();
            }

            String floorIdStr = floorIds.get(0);
            if (!floorIdStr.matches("^\\d+$") || floorIdStr.contains("'") || floorIdStr.contains(";")) {
                return StepTestResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_TYPE.getCode())
                        .message("floor_id phải là kiểu số nguyên")
                        .usedInTest(false)
                        .build();
            }

            if (floorIdStr.length() > 10) {
                return StepTestResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message("floor_id không hợp lệ")
                        .usedInTest(false)
                        .build();
            }

            long floorIdLong = Long.parseLong(floorIdStr);
            if (floorIdLong <= 0 || floorIdLong > Integer.MAX_VALUE) {
                return StepTestResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message("floor_id không hợp lệ")
                        .usedInTest(false)
                        .build();
            }

            Integer floorId = (int) floorIdLong;
            if (!mapTestRepository.existsById(floorId)) {
                return StepTestResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.FLOOR_NOT_FOUND.getCode())
                        .message("Tầng không tồn tại")
                        .usedInTest(false)
                        .build();
            }

            List<StepEdgeData> edges = stepTestRepository.findByMapTest_Id(floorId)
                    .stream()
                    .map(step -> new StepEdgeData(
                            step.getId(),
                            step.getStartNodeId().getId(),
                            step.getEndNodeId().getId(),
                            step.getDistance(),
                            step.getDirection(),
                            step.getInstruction()
                    ))
                    .toList();

            return StepTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message("Lấy danh sách edge thành công")
                    .data(edges)
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            log.error("Error when get edges: ", e);
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

            if (Objects.equals(buildingCode, "")) {
                mapTests = mapTestRepository.findAll();
                return MapTestResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("success")
                        .code(ResponseCode.SUCCESS.getCode())
                        .message(ResponseCode.SUCCESS.getMessage())
                        .data(mapTests)
                        .usedInTest(false)
                        .build();
            }
            // Xử lý dữ liệu trả về
            if (mapTests.isEmpty()) {
                return MapTestResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("success")
                        .code(ResponseCode.SUCCESS.getCode())
                        .message(ResponseCode.SUCCESS.getMessage())
                        .data(mapTests)
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
