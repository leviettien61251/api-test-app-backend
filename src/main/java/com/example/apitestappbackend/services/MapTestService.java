package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.AreaTest.AreaTestData;
import com.example.apitestappbackend.DTO.AreaTest.AreaTestResponse;
import com.example.apitestappbackend.DTO.HeatmapTest.HeatmapTestData;
import com.example.apitestappbackend.DTO.HeatmapTest.HeatmapTestResponse;
import com.example.apitestappbackend.DTO.MapTest.MapMetaData;
import com.example.apitestappbackend.DTO.MapTest.MapTestData;
import com.example.apitestappbackend.DTO.MapTest.MapTestRequest;
import com.example.apitestappbackend.DTO.MapTest.MapTestResponse;
import com.example.apitestappbackend.DTO.NodeTest.BeaconData;
import com.example.apitestappbackend.DTO.PathTest.PathTestData;
import com.example.apitestappbackend.DTO.PathTest.PathTestResponse;
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
import com.example.apitestappbackend.models.hospitaldb.*;
import com.example.apitestappbackend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final AreaTestRepository areaTestRepository;
    private final HeatmapTestRepository heatmapTestRepository;
    private final PathTestRepository pathTestRepository;

    public MapTestService(MapTestRepository mapTestRepository,
                          StepTestRepository stepTestRepository,
                          NodeTestRepository nodeTestRepository,
                          WardTestRepository wardTestRepository,
                          SavedSearchRepository savedSearchRepository,
                          UserTestRepository userTestRepository,
                          AreaTestRepository areaTestRepository,
                          HeatmapTestRepository heatmapTestRepository,
                          PathTestRepository pathTestRepository) {
        this.mapTestRepository = mapTestRepository;
        this.stepTestRepository = stepTestRepository;
        this.nodeTestRepository = nodeTestRepository;
        this.wardTestRepository = wardTestRepository;
        this.savedSearchRepository = savedSearchRepository;
        this.userTestRepository = userTestRepository;
        this.areaTestRepository = areaTestRepository;
        this.heatmapTestRepository = heatmapTestRepository;
        this.pathTestRepository = pathTestRepository;
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

    public String cleanAreaData(){
        areaTestRepository.deleteAllInBatch();
        return "Dọn dẹp dữ liệu Area thành công";
    }

    public String cleanHeatmapData(){
        heatmapTestRepository.deleteAllInBatch();
        return "Dọn dẹp dữ liệu Heatmap thành công";
    }

    public String cleanPathData(){
        pathTestRepository.deleteAllInBatch();
        return "Dọn dẹp dữ liệu Path thành công";
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

    public WardTestResponse insertWardTest(WardTestRequest request) {
        try {
            if (request == null
                    || request.getMapNodeId() == null || request.getName() == null || request.getWardStatus() == null
                    || request.getName().trim().isBlank() || request.getWardStatus().trim().isBlank()) {
                return WardTestResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }
            if(request.getName().contains(";") || request.getName().contains(",") || request.getName().contains("'")){
                return WardTestResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_TYPE.getCode())
                        .message(ResponseCode.INVALID_TYPE.getMessage())
                        .usedInTest(false)
                        .build();
            }
            if(request.getWardStatus().contains(";") || request.getWardStatus().contains(",") || request.getWardStatus().contains("'")){
                return WardTestResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_TYPE.getCode())
                        .message(ResponseCode.INVALID_TYPE.getMessage())
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

    public WardTestResponse getWard() {
        try {
            List<WardTestData> wards = wardTestRepository.findAll()
                    .stream()
                    .map(ward -> new WardTestData(
                            ward.getId(),
                            ward.getName(),
                            ward.getMapNode().getId(),
                            ward.getMapNode().getMapTest().getId(),
                            ward.getWardStatus()
                    ))
                    .toList();

            return WardTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .data(wards)
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            log.error("Error when get ward test: ", e);
            return WardTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("fail")
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .build();
        }
    }

    public MapTestResponse getLandMark() {
        return buildMapTestFailResponse(ResponseCode.MISSING_PARAM, "Thiếu floor_id");
    }

    public MapTestResponse getLandMark(List<String> floorIds) {
        try {
            if (floorIds == null || floorIds.isEmpty() || floorIds.size() > 1 || floorIds.get(0) == null || floorIds.get(0).isBlank()) {
                return buildMapTestFailResponse(ResponseCode.MISSING_PARAM, "Thiếu floor_id");
            }

            Integer floorId;
            try {
                floorId = Integer.parseInt(floorIds.get(0).trim());
            } catch (NumberFormatException e) {
                return buildMapTestFailResponse(ResponseCode.INVALID_TYPE, "floor_id không hợp lệ");
            }

            if (!mapTestRepository.existsById(floorId)) {
                return buildMapTestFailResponse(ResponseCode.FLOOR_NOT_FOUND, "Tầng không tồn tại");
            }

            List<BeaconData> landmarks = nodeTestRepository.findLandmarksByMapId(floorId);

            return MapTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message("Lấy landmarks thành công")
                    .data(landmarks)
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            log.error("Error when get beacon: ", e);
            return buildMapTestFailResponse(ResponseCode.INTERNAL_SERVER_ERROR, ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    public MapTestResponse insertMapTest(MapTestRequest request) {
        MapTest savedMt;
        List<MapTestData> mapTests = new ArrayList<>();
        try {
            if (request == null) {
                return buildMapTestFailResponse(ResponseCode.MISSING_BODY, ResponseCode.MISSING_BODY.getMessage());
            }

            if (isBlank(request.getBuildingCode())) {
                return buildMapTestFailResponse(ResponseCode.MISSING_PARAM, "Thiếu buildingCode");
            }

            if (isBlank(request.getBuildingName())) {
                return buildMapTestFailResponse(ResponseCode.MISSING_PARAM, "Thiếu buildingName");
            }

            if (isBlank(request.getImageUrl())) {
                return buildMapTestFailResponse(ResponseCode.MISSING_PARAM, "Thiếu imageUrl");
            }

            if (request.getScaleX() == null) {
                return buildMapTestFailResponse(ResponseCode.MISSING_PARAM, "Thiếu scaleX");
            }

            if (request.getScaleY() == null) {
                return buildMapTestFailResponse(ResponseCode.MISSING_PARAM, "Thiếu scaleY");
            }

            Double scaleX = parseNumber(request.getScaleX());
            if (scaleX == null) {
                return buildMapTestFailResponse(ResponseCode.INVALID_TYPE, "scaleX phải là kiểu số");
            }

            Double scaleY = parseNumber(request.getScaleY());
            if (scaleY == null) {
                return buildMapTestFailResponse(ResponseCode.INVALID_TYPE, "scaleY phải là kiểu số");
            }

            String buildingCode = request.getBuildingCode().trim();
            String buildingName = request.getBuildingName().trim();
            String imageUrl = request.getImageUrl().trim();

            if (isInvalidScaleValue(scaleX)) {
                return buildMapTestFailResponse(ResponseCode.INVALID_VALUE, "scaleX phải là số lớn hơn 0");
            }

            if (isInvalidScaleValue(scaleY)) {
                return buildMapTestFailResponse(ResponseCode.INVALID_VALUE, "scaleY phải là số lớn hơn 0");
            }

            if (!isImageURLValid(imageUrl)) {
                return buildMapTestFailResponse(ResponseCode.INVALID_VALUE, "Hinh anh khong hop le");
            }

            if (mapTestRepository.existsByBuildingCode(buildingCode)) {
                return buildMapTestFailResponse(ResponseCode.BUILDING_EXISTS, ResponseCode.BUILDING_EXISTS.getMessage());
            }

            MapTest mt = new MapTest();
            mt.setBuildingCode(buildingCode);
            mt.setBuildingName(buildingName);
            mt.setImageUrl(imageUrl);
            mt.setScaleX(scaleX);
            mt.setScaleY(scaleY);
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

    private boolean isBlank(String value) {
        return value == null || value.trim().isBlank();
    }

    private Double parseNumber(Object value) {
        if (!(value instanceof Number number)) {
            return null;
        }

        return number.doubleValue();
    }

    private Integer parseInteger(Object value) {
        if (!(value instanceof Number number)) {
            return null;
        }

        double doubleValue = number.doubleValue();
        if (Double.isNaN(doubleValue)
                || Double.isInfinite(doubleValue)
                || doubleValue % 1 != 0
                || doubleValue > Integer.MAX_VALUE
                || doubleValue < Integer.MIN_VALUE) {
            return null;
        }

        return number.intValue();
    }

    private boolean isInvalidScaleValue(Double value) {
        return value.isNaN() || value.isInfinite() || value <= 0;
    }

    private MapTestResponse buildMapTestFailResponse(ResponseCode responseCode, String message) {
        return MapTestResponse
                .builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(message)
                .usedInTest(false)
                .build();
    }

    private StepTestResponse buildStepFailResponse(ResponseCode responseCode, String message) {
        return StepTestResponse
                .builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(message)
                .usedInTest(false)
                .build();
    }

    private AreaTestResponse buildAreaFailResponse(ResponseCode responseCode, String message) {
        return AreaTestResponse
                .builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(message)
                .usedInTest(false)
                .build();
    }

    private HeatmapTestResponse buildHeatmapFailResponse(ResponseCode responseCode, String message) {
        return HeatmapTestResponse
                .builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(message)
                .usedInTest(false)
                .build();
    }

    private PathTestResponse buildPathFailResponse(ResponseCode responseCode, String message) {
        return PathTestResponse
                .builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(message)
                .usedInTest(false)
                .build();
    }

    private boolean hasAnyKey(Map<String, Object> request, String... keys) {
        for (String key : keys) {
            if (request.containsKey(key)) {
                return true;
            }
        }

        return false;
    }

    private Object getFirstValue(Map<String, Object> request, String... keys) {
        for (String key : keys) {
            if (request.containsKey(key)) {
                return request.get(key);
            }
        }

        return null;
    }

    private Timestamp parseTimestamp(Object value) {
        if (!(value instanceof String text)) {
            return null;
        }

        text = text.trim();
        if (text.isBlank()) {
            return null;
        }

        try {
            return Timestamp.valueOf(text);
        } catch (IllegalArgumentException ignored) {
        }

        try {
            return Timestamp.valueOf(LocalDateTime.parse(text));
        } catch (DateTimeParseException ignored) {
        }

        try {
            return Timestamp.from(OffsetDateTime.parse(text).toInstant());
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    public AreaTestResponse postAreaTest(Map<String, Object> request) {
        try {
            if (request == null) {
                return buildAreaFailResponse(ResponseCode.MISSING_BODY, ResponseCode.MISSING_BODY.getMessage());
            }

            if (!request.containsKey("areaId")) {
                return buildAreaFailResponse(ResponseCode.MISSING_PARAM, "Thiếu areaId");
            }

            if (!request.containsKey("mapId")) {
                return buildAreaFailResponse(ResponseCode.MISSING_PARAM, "Thiếu mapId");
            }

            Object areaIdValue = request.get("areaId");
            Object mapIdValue = request.get("mapId");

            if (areaIdValue == null) {
                return buildAreaFailResponse(ResponseCode.MISSING_PARAM, "Thiếu areaId");
            }

            if (mapIdValue == null) {
                return buildAreaFailResponse(ResponseCode.MISSING_PARAM, "Thiếu mapId");
            }

            if (!(areaIdValue instanceof String areaId)) {
                return buildAreaFailResponse(ResponseCode.INVALID_TYPE, "areaId phải là kiểu chuỗi");
            }

            Integer mapId = parseInteger(mapIdValue);
            if (mapId == null) {
                return buildAreaFailResponse(ResponseCode.INVALID_TYPE, "mapId phải là kiểu số nguyên");
            }

            areaId = areaId.trim();
            if (areaId.isBlank()) {
                return buildAreaFailResponse(ResponseCode.MISSING_PARAM, "Thiếu areaId");
            }

            if (mapId <= 0) {
                return buildAreaFailResponse(ResponseCode.INVALID_VALUE, "mapId phải lớn hơn 0");
            }

            if (areaTestRepository.existsById(areaId)) {
                return buildAreaFailResponse(ResponseCode.INVALID_VALUE, "areaId đã tồn tại");
            }

            MapTest mapTest = mapTestRepository.findById(mapId).orElse(null);
            if (mapTest == null) {
                return buildAreaFailResponse(ResponseCode.FLOOR_NOT_FOUND, ResponseCode.FLOOR_NOT_FOUND.getMessage());
            }

            AreaTest areaTest = new AreaTest();
            areaTest.setAreaId(areaId);
            areaTest.setMapId(mapTest);
            areaTest.setStatus("success");
            areaTest.setCode(ResponseCode.SUCCESS.getCode());
            areaTest.setMessage(ResponseCode.SUCCESS.getMessage());
            areaTest.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            areaTest.setUsedInTest(false);

            AreaTest savedArea = areaTestRepository.save(areaTest);

            return AreaTestResponse
                    .builder()
                    .timestamp(savedArea.getTimeStamp())
                    .status(savedArea.getStatus())
                    .code(savedArea.getCode())
                    .message(savedArea.getMessage())
                    .data(List.of(new AreaTestData(savedArea.getAreaId(), savedArea.getMapId().getId())))
                    .usedInTest(savedArea.getUsedInTest())
                    .build();
        } catch (Exception e) {
            log.error("Error when post area test: ", e);
            return buildAreaFailResponse(ResponseCode.INTERNAL_SERVER_ERROR, ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    public HeatmapTestResponse postHeatmapTest(Map<String, Object> request) {
        try {
            if (request == null) {
                return buildHeatmapFailResponse(ResponseCode.MISSING_BODY, ResponseCode.MISSING_BODY.getMessage());
            }

            if (!hasAnyKey(request, "nodeId", "node_id")) {
                return buildHeatmapFailResponse(ResponseCode.MISSING_PARAM, "Thiếu nodeId");
            }

            if (!hasAnyKey(request, "densityScore", "density_score")) {
                return buildHeatmapFailResponse(ResponseCode.MISSING_PARAM, "Thiếu densityScore");
            }

            if (!hasAnyKey(request, "recordedAt", "recorded_at")) {
                return buildHeatmapFailResponse(ResponseCode.MISSING_PARAM, "Thiếu recordedAt");
            }

            Object nodeIdValue = getFirstValue(request, "nodeId", "node_id");
            Object densityScoreValue = getFirstValue(request, "densityScore", "density_score");
            Object recordedAtValue = getFirstValue(request, "recordedAt", "recorded_at");

            if (nodeIdValue == null) {
                return buildHeatmapFailResponse(ResponseCode.MISSING_PARAM, "Thiếu nodeId");
            }

            if (densityScoreValue == null) {
                return buildHeatmapFailResponse(ResponseCode.MISSING_PARAM, "Thiếu densityScore");
            }

            if (recordedAtValue == null) {
                return buildHeatmapFailResponse(ResponseCode.MISSING_PARAM, "Thiếu recordedAt");
            }

            if (nodeIdValue instanceof String text && text.trim().isBlank()) {
                return buildHeatmapFailResponse(ResponseCode.MISSING_PARAM, "Thiếu nodeId");
            }

            if (densityScoreValue instanceof String text && text.trim().isBlank()) {
                return buildHeatmapFailResponse(ResponseCode.MISSING_PARAM, "Thiếu densityScore");
            }

            if (recordedAtValue instanceof String text && text.trim().isBlank()) {
                return buildHeatmapFailResponse(ResponseCode.MISSING_PARAM, "Thiếu recordedAt");
            }

            Integer nodeId = parseInteger(nodeIdValue);
            if (nodeId == null) {
                return buildHeatmapFailResponse(ResponseCode.INVALID_TYPE, "nodeId phải là kiểu số nguyên");
            }

            Integer densityScore = parseInteger(densityScoreValue);
            if (densityScore == null) {
                return buildHeatmapFailResponse(ResponseCode.INVALID_TYPE, "densityScore phải là kiểu số nguyên");
            }

            if (!(recordedAtValue instanceof String)) {
                return buildHeatmapFailResponse(ResponseCode.INVALID_TYPE, "recordedAt phải là kiểu chuỗi thời gian");
            }

            Timestamp recordedAt = parseTimestamp(recordedAtValue);
            if (recordedAt == null) {
                return buildHeatmapFailResponse(ResponseCode.INVALID_VALUE, "recordedAt không hợp lệ");
            }

            if (nodeId <= 0) {
                return buildHeatmapFailResponse(ResponseCode.INVALID_VALUE, "nodeId phải lớn hơn 0");
            }

            if (densityScore < 0) {
                return buildHeatmapFailResponse(ResponseCode.INVALID_VALUE, "densityScore phải lớn hơn hoặc bằng 0");
            }

            NodeTest nodeTest = nodeTestRepository.findById(nodeId).orElse(null);
            if (nodeTest == null) {
                return buildHeatmapFailResponse(ResponseCode.NODE_NOT_FOUND, ResponseCode.NODE_NOT_FOUND.getMessage());
            }

            HeatmapTest heatmapTest = new HeatmapTest();
            heatmapTest.setNodeId(nodeTest);
            heatmapTest.setDensityScore(densityScore);
            heatmapTest.setRecordedAt(recordedAt);
            heatmapTest.setStatus("success");
            heatmapTest.setCode(ResponseCode.SUCCESS.getCode());
            heatmapTest.setMessage(ResponseCode.SUCCESS.getMessage());
            heatmapTest.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            heatmapTest.setUsedInTest(false);

            HeatmapTest savedHeatmap = heatmapTestRepository.save(heatmapTest);

            return HeatmapTestResponse
                    .builder()
                    .timestamp(savedHeatmap.getTimeStamp())
                    .status(savedHeatmap.getStatus())
                    .code(savedHeatmap.getCode())
                    .message(savedHeatmap.getMessage())
                    .data(List.of(new HeatmapTestData(
                            savedHeatmap.getId(),
                            savedHeatmap.getNodeId().getId(),
                            savedHeatmap.getDensityScore(),
                            savedHeatmap.getRecordedAt()
                    )))
                    .usedInTest(savedHeatmap.getUsedInTest())
                    .build();
        } catch (Exception e) {
            log.error("Error when post heatmap test: ", e);
            return buildHeatmapFailResponse(ResponseCode.INTERNAL_SERVER_ERROR, ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    public PathTestResponse postPathTest(Map<String, Object> request) {
        try {
            if (request == null) {
                return buildPathFailResponse(ResponseCode.MISSING_BODY, ResponseCode.MISSING_BODY.getMessage());
            }

            if (!hasAnyKey(request, "phoneNumber", "phone_number")) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu phoneNumber");
            }

            if (!hasAnyKey(request, "startNodeId", "start_node_id")) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu startNodeId");
            }

            if (!hasAnyKey(request, "endNodeId", "end_node_id")) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu endNodeId");
            }

            if (!hasAnyKey(request, "totalDistance", "total_distance")) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu totalDistance");
            }

            if (!hasAnyKey(request, "pathStatus", "path_status")) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu pathStatus");
            }

            Object phoneNumberValue = getFirstValue(request, "phoneNumber", "phone_number");
            Object startNodeIdValue = getFirstValue(request, "startNodeId", "start_node_id");
            Object endNodeIdValue = getFirstValue(request, "endNodeId", "end_node_id");
            Object totalDistanceValue = getFirstValue(request, "totalDistance", "total_distance");
            Object pathStatusValue = getFirstValue(request, "pathStatus", "path_status");

            if (phoneNumberValue == null) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu phoneNumber");
            }

            if (startNodeIdValue == null) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu startNodeId");
            }

            if (endNodeIdValue == null) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu endNodeId");
            }

            if (totalDistanceValue == null) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu totalDistance");
            }

            if (pathStatusValue == null) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu pathStatus");
            }

            if (!(phoneNumberValue instanceof String phoneNumber)) {
                return buildPathFailResponse(ResponseCode.INVALID_TYPE, "phoneNumber phải là kiểu chuỗi");
            }

            phoneNumber = phoneNumber.trim();
            if (phoneNumber.isBlank()) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu phoneNumber");
            }

            if (startNodeIdValue instanceof String text && text.trim().isBlank()) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu startNodeId");
            }

            if (endNodeIdValue instanceof String text && text.trim().isBlank()) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu endNodeId");
            }

            if (totalDistanceValue instanceof String text && text.trim().isBlank()) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu totalDistance");
            }

            if (pathStatusValue instanceof String text && text.trim().isBlank()) {
                return buildPathFailResponse(ResponseCode.MISSING_PARAM, "Thiếu pathStatus");
            }

            Integer startNodeId = parseInteger(startNodeIdValue);
            if (startNodeId == null) {
                return buildPathFailResponse(ResponseCode.INVALID_TYPE, "startNodeId phải là kiểu số nguyên");
            }

            Integer endNodeId = parseInteger(endNodeIdValue);
            if (endNodeId == null) {
                return buildPathFailResponse(ResponseCode.INVALID_TYPE, "endNodeId phải là kiểu số nguyên");
            }

            Double totalDistance = parseNumber(totalDistanceValue);
            if (totalDistance == null) {
                return buildPathFailResponse(ResponseCode.INVALID_TYPE, "totalDistance phải là kiểu số");
            }

            Double pathStatus = parseNumber(pathStatusValue);
            if (pathStatus == null) {
                return buildPathFailResponse(ResponseCode.INVALID_TYPE, "pathStatus phải là kiểu số");
            }

            if (startNodeId <= 0) {
                return buildPathFailResponse(ResponseCode.INVALID_VALUE, "startNodeId phải lớn hơn 0");
            }

            if (endNodeId <= 0) {
                return buildPathFailResponse(ResponseCode.INVALID_VALUE, "endNodeId phải lớn hơn 0");
            }

            if (isInvalidScaleValue(totalDistance)) {
                return buildPathFailResponse(ResponseCode.INVALID_VALUE, "totalDistance phải là số lớn hơn 0");
            }

            if (pathStatus.isNaN() || pathStatus.isInfinite() || pathStatus < 0) {
                return buildPathFailResponse(ResponseCode.INVALID_VALUE, "pathStatus phải lớn hơn hoặc bằng 0");
            }

            UserTest userTest = userTestRepository.findByPhoneNumber(phoneNumber).orElse(null);
            if (userTest == null) {
                return buildPathFailResponse(ResponseCode.USER_NOT_FOUND, ResponseCode.USER_NOT_FOUND.getMessage());
            }

            NodeTest startNode = nodeTestRepository.findById(startNodeId).orElse(null);
            if (startNode == null) {
                return buildPathFailResponse(ResponseCode.NODE_NOT_FOUND, "Start node not found");
            }

            NodeTest endNode = nodeTestRepository.findById(endNodeId).orElse(null);
            if (endNode == null) {
                return buildPathFailResponse(ResponseCode.NODE_NOT_FOUND, "End node not found");
            }

            PathTest pathTest = new PathTest();
            pathTest.setUserId(userTest);
            pathTest.setStartNodeId(startNode);
            pathTest.setEndNodeId(endNode);
            pathTest.setTotalDistance(totalDistance);
            pathTest.setPathStatus(pathStatus);
            pathTest.setStatus("success");
            pathTest.setCode(ResponseCode.SUCCESS.getCode());
            pathTest.setMessage(ResponseCode.SUCCESS.getMessage());
            pathTest.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            pathTest.setUsedInTest(false);

            PathTest savedPath = pathTestRepository.save(pathTest);

            return PathTestResponse
                    .builder()
                    .timestamp(savedPath.getTimeStamp())
                    .status(savedPath.getStatus())
                    .code(savedPath.getCode())
                    .message(savedPath.getMessage())
                    .data(List.of(new PathTestData(
                            savedPath.getId(),
                            savedPath.getUserId().getId(),
                            savedPath.getStartNodeId().getId(),
                            savedPath.getEndNodeId().getId(),
                            savedPath.getTotalDistance(),
                            savedPath.getPathStatus()
                    )))
                    .usedInTest(savedPath.getUsedInTest())
                    .build();
        } catch (Exception e) {
            log.error("Error when post path test: ", e);
            return buildPathFailResponse(ResponseCode.INTERNAL_SERVER_ERROR, ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    public StepTestResponse insertStep(StepTestRequest request) {
        StepTest savedSt;
        List<StepTestData> stepTests = new ArrayList<>();
        try {
            if (request == null) {
                return buildStepFailResponse(ResponseCode.MISSING_BODY, ResponseCode.MISSING_BODY.getMessage());
            }

            if (request.getMapId() == null) {
                return buildStepFailResponse(ResponseCode.MISSING_PARAM, "Thiếu mapId");
            }

            if (request.getStartNodeId() == null) {
                return buildStepFailResponse(ResponseCode.MISSING_PARAM, "Thiếu startNodeId");
            }

            if (request.getEndNodeId() == null) {
                return buildStepFailResponse(ResponseCode.MISSING_PARAM, "Thiếu endNodeId");
            }

            if (request.getDistance() == null) {
                return buildStepFailResponse(ResponseCode.MISSING_PARAM, "Thiếu distance");
            }

            if (request.getDirection() == null) {
                return buildStepFailResponse(ResponseCode.MISSING_PARAM, "Thiếu direction");
            }

            if (request.getInstruction() == null) {
                return buildStepFailResponse(ResponseCode.MISSING_PARAM, "Thiếu instruction");
            }

            if (!(request.getDirection() instanceof String direction)) {
                return buildStepFailResponse(ResponseCode.INVALID_TYPE, "direction phải là kiểu chuỗi");
            }

            if (!(request.getInstruction() instanceof String instruction)) {
                return buildStepFailResponse(ResponseCode.INVALID_TYPE, "instruction phải là kiểu chuỗi");
            }

            direction = direction.trim();
            instruction = instruction.trim();

            if (direction.isBlank()) {
                return buildStepFailResponse(ResponseCode.MISSING_PARAM, "Thiếu direction");
            }

            if (instruction.isBlank()) {
                return buildStepFailResponse(ResponseCode.MISSING_PARAM, "Thiếu instruction");
            }

            Integer mapId = parseInteger(request.getMapId());
            if (mapId == null) {
                return buildStepFailResponse(ResponseCode.INVALID_TYPE, "mapId phải là kiểu số nguyên");
            }

            Integer startNodeId = parseInteger(request.getStartNodeId());
            if (startNodeId == null) {
                return buildStepFailResponse(ResponseCode.INVALID_TYPE, "startNodeId phải là kiểu số nguyên");
            }

            Integer endNodeId = parseInteger(request.getEndNodeId());
            if (endNodeId == null) {
                return buildStepFailResponse(ResponseCode.INVALID_TYPE, "endNodeId phải là kiểu số nguyên");
            }

            Double distance = parseNumber(request.getDistance());
            if (distance == null) {
                return buildStepFailResponse(ResponseCode.INVALID_TYPE, "distance phải là kiểu số");
            }

            if (mapId <= 0) {
                return buildStepFailResponse(ResponseCode.INVALID_VALUE, "mapId phải lớn hơn 0");
            }

            if (startNodeId <= 0) {
                return buildStepFailResponse(ResponseCode.INVALID_VALUE, "startNodeId phải lớn hơn 0");
            }

            if (endNodeId <= 0) {
                return buildStepFailResponse(ResponseCode.INVALID_VALUE, "endNodeId phải lớn hơn 0");
            }

            if (isInvalidScaleValue(distance)) {
                return buildStepFailResponse(ResponseCode.INVALID_VALUE, "distance phải là số lớn hơn 0");
            }

            MapTest mt = mapTestRepository.findById(mapId).orElse(null);
            if (mt == null) {
                return buildStepFailResponse(ResponseCode.FLOOR_NOT_FOUND, ResponseCode.FLOOR_NOT_FOUND.getMessage());
            }

            NodeTest startNt = nodeTestRepository.findById(startNodeId).orElse(null);
            if (startNt == null) {
                return buildStepFailResponse(ResponseCode.NODE_NOT_FOUND, "Start node not found");
            }

            NodeTest endNt = nodeTestRepository.findById(endNodeId).orElse(null);
            if (endNt == null) {
                return buildStepFailResponse(ResponseCode.NODE_NOT_FOUND, "End node not found");
            }

            StepTest st = new StepTest();
            st.setMapTest(mt);
            st.setStartNodeId(startNt);
            st.setEndNodeId(endNt);
            st.setDistance(distance);
            st.setDirection(direction);
            st.setInstruction(instruction);
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

    public MapTestResponse getMeta(List<String> floorIds) {
        try {
            if (floorIds == null || floorIds.isEmpty() || floorIds.size() > 1 || floorIds.get(0) == null || floorIds.get(0).isBlank()) {
                return buildMapTestFailResponse(ResponseCode.MISSING_PARAM, "Thiếu floor_id");
            }

            String floorIdValue = floorIds.get(0).trim();
            double floorIdNumber;
            try {
                floorIdNumber = Double.parseDouble(floorIdValue);
            } catch (NumberFormatException e) {
                return buildMapTestFailResponse(ResponseCode.INVALID_TYPE, "floor_id không hợp lệ hoặc quá lớn");
            }

            if (Double.isNaN(floorIdNumber)
                    || Double.isInfinite(floorIdNumber)
                    || floorIdNumber > Integer.MAX_VALUE
                    || floorIdNumber < 0) {
                return buildMapTestFailResponse(ResponseCode.INVALID_TYPE, "floor_id không hợp lệ hoặc quá lớn");
            }

            Integer floorId = (int) floorIdNumber;
            MapTest mapTest = mapTestRepository.findById(floorId).orElse(null);
            if (mapTest == null) {
                return buildMapTestFailResponse(ResponseCode.FLOOR_NOT_FOUND, "Tầng không tồn tại");
            }

            if (mapTest.getScaleX() == null
                    || mapTest.getScaleY() == null
                    || mapTest.getScaleX() <= 0
                    || mapTest.getScaleY() <= 0) {
                return buildMapTestFailResponse(ResponseCode.INVALID_VALUE, "Thông số tỷ lệ không hợp lệ");
            }

            return MapTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message("Lấy meta thành công")
                    .data(new MapMetaData(
                            mapTest.getBuildingCode(),
                            mapTest.getBuildingName(),
                            mapTest.getImageUrl(),
                            mapTest.getScaleX(),
                            mapTest.getScaleY()
                    ))
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            log.error("Error when get map meta: ", e);
            return buildMapTestFailResponse(ResponseCode.INTERNAL_SERVER_ERROR, ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
        }
    }



}
