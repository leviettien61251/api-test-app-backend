package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.BottleneckDataTest.BottleneckDataTestData;
import com.example.apitestappbackend.DTO.BottleneckDataTest.BottleneckDataTestRequest;
import com.example.apitestappbackend.DTO.BottleneckDataTest.BottleneckDataTestResponse;
import com.example.apitestappbackend.DTO.FlowTest.FlowAlertData;
import com.example.apitestappbackend.DTO.FlowTest.FlowAlertResponse;
import com.example.apitestappbackend.DTO.FlowTest.FlowBottleneckData;
import com.example.apitestappbackend.DTO.FlowTest.FlowBottleneckResponse;
import com.example.apitestappbackend.DTO.FlowTest.FlowDensityData;
import com.example.apitestappbackend.DTO.FlowTest.FlowDensityResponse;
import com.example.apitestappbackend.DTO.FlowTest.FlowEdgeStatusData;
import com.example.apitestappbackend.DTO.FlowTest.FlowEdgeStatusResponse;
import com.example.apitestappbackend.DTO.FlowTest.FlowHeatmapData;
import com.example.apitestappbackend.DTO.FlowTest.FlowHeatmapResponse;
import com.example.apitestappbackend.DTO.HeatmapDataTest.HeatmapDataTestData;
import com.example.apitestappbackend.DTO.HeatmapDataTest.HeatmapDataTestRequest;
import com.example.apitestappbackend.DTO.HeatmapDataTest.HeatmapDataTestResponse;
import com.example.apitestappbackend.DTO.ObstacleTest.ObstacleTestData;
import com.example.apitestappbackend.DTO.ObstacleTest.ObstacleTestRequest;
import com.example.apitestappbackend.DTO.ObstacleTest.ObstacleTestResponse;
import com.example.apitestappbackend.DTO.RouteDensityTest.RouteDensityTestData;
import com.example.apitestappbackend.DTO.RouteDensityTest.RouteDensityTestRequest;
import com.example.apitestappbackend.DTO.RouteDensityTest.RouteDensityTestResponse;
import com.example.apitestappbackend.DTO.RouteTest.RouteTestData;
import com.example.apitestappbackend.DTO.RouteTest.RouteTestRequest;
import com.example.apitestappbackend.DTO.RouteTest.RouteTestResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.hospitaldb.BottlenecksDataTest;
import com.example.apitestappbackend.models.hospitaldb.EdgeDensityTest;
import com.example.apitestappbackend.models.hospitaldb.HeatMapDataTest;
import com.example.apitestappbackend.models.hospitaldb.ObstacleTest;
import com.example.apitestappbackend.models.hospitaldb.RouteDensityTest;
import com.example.apitestappbackend.models.hospitaldb.RouteTest;
import com.example.apitestappbackend.repository.BottlenecksDataTestRepository;
import com.example.apitestappbackend.repository.EdgeDensityTestRepository;
import com.example.apitestappbackend.repository.HeatMapDataTestRepository;
import com.example.apitestappbackend.repository.ObstacleTestRepository;
import com.example.apitestappbackend.repository.RouteDensityTestRepository;
import com.example.apitestappbackend.repository.RouteTestRepository;
import com.example.apitestappbackend.repository.EdgeStatusTestRepository;
import com.example.apitestappbackend.repository.EdgeTestRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlowTestService {
    private final EdgeTestRepository edgeTestRepository;
    private final EdgeStatusTestRepository edgeStatusTestRepository;
    private final RouteTestRepository routeTestRepository;
    private final RouteDensityTestRepository routeDensityTestRepository;
    private final HeatMapDataTestRepository heatMapDataTestRepository;
    private final BottlenecksDataTestRepository bottlenecksDataTestRepository;
    private final EdgeDensityTestRepository edgeDensityTestRepository;
    private final ObstacleTestRepository obstacleTestRepository;

    public FlowTestService(EdgeTestRepository edgeTestRepository,
                           EdgeStatusTestRepository edgeStatusTestRepository,
                           RouteTestRepository routeTestRepository,
                           RouteDensityTestRepository routeDensityTestRepository,
                           HeatMapDataTestRepository heatMapDataTestRepository,
                           BottlenecksDataTestRepository bottlenecksDataTestRepository,
                           EdgeDensityTestRepository edgeDensityTestRepository,
                           ObstacleTestRepository obstacleTestRepository) {
        this.edgeTestRepository = edgeTestRepository;
        this.edgeStatusTestRepository = edgeStatusTestRepository;
        this.routeTestRepository = routeTestRepository;
        this.routeDensityTestRepository = routeDensityTestRepository;
        this.heatMapDataTestRepository = heatMapDataTestRepository;
        this.bottlenecksDataTestRepository = bottlenecksDataTestRepository;
        this.edgeDensityTestRepository = edgeDensityTestRepository;
        this.obstacleTestRepository = obstacleTestRepository;
    }

    public RouteTestResponse insertRouteTest(RouteTestRequest request) {
        try {
            if (request == null) {
                return buildRouteFailResponse(ResponseCode.MISSING_BODY);
            }

            String routeId = parseRequiredString(request.getRouteId());
            if (routeId == null) {
                return request.getRouteId() == null
                        ? buildRouteFailResponse(ResponseCode.MISSING_PARAM)
                        : buildRouteFailResponse(ResponseCode.INVALID_TYPE);
            }

            RouteTest routeTest = new RouteTest();
            routeTest.setRouteId(routeId);
            routeTest.setStatus("success");
            routeTest.setCode(ResponseCode.SUCCESS.getCode());
            routeTest.setMessage(ResponseCode.SUCCESS.getMessage());
            routeTest.setUsedInTest(false);

            RouteTest savedRoute = routeTestRepository.save(routeTest);

            return RouteTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .data(new RouteTestData(savedRoute.getId(), savedRoute.getRouteId()))
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            return buildRouteFailResponse(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    public RouteDensityTestResponse insertRouteDensityTest(RouteDensityTestRequest request) {
        try {
            if (request == null) {
                return buildRouteDensityFailResponse(ResponseCode.MISSING_BODY);
            }

            String routeId = parseRequiredString(request.getRouteId());
            Integer currentPeople = parseInteger(request.getCurrentPeople());
            if (request.getRouteId() == null || request.getCurrentPeople() == null) {
                return buildRouteDensityFailResponse(ResponseCode.MISSING_PARAM);
            }
            if (routeId == null || currentPeople == null) {
                return buildRouteDensityFailResponse(ResponseCode.INVALID_TYPE);
            }
            if (currentPeople < 0) {
                return buildRouteDensityFailResponse(ResponseCode.INVALID_VALUE);
            }

            RouteTest routeTest = findRouteOrNull(routeId);
            if (routeTest == null) {
                return buildRouteDensityFailResponse(ResponseCode.PATH_NOT_FOUND);
            }

            RouteDensityTest routeDensityTest = new RouteDensityTest();
            routeDensityTest.setRouteId(routeTest);
            routeDensityTest.setType(currentPeople);
            setSuccessAudit(routeDensityTest);

            RouteDensityTest saved = routeDensityTestRepository.save(routeDensityTest);
            return RouteDensityTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .data(new RouteDensityTestData(saved.getId(), saved.getRouteId().getRouteId(), saved.getType()))
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            return buildRouteDensityFailResponse(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    public ObstacleTestResponse insertObstacleTest(ObstacleTestRequest request) {
        try {
            if (request == null) {
                return buildObstacleFailResponse(ResponseCode.MISSING_BODY);
            }

            String routeId = parseRequiredString(request.getRouteId());
            String type = parseRequiredString(request.getType());
            Double xCoordinate = parseDouble(request.getXCoordinate());
            Double yCoordinate = parseDouble(request.getYCoordinate());
            String description = parseRequiredString(request.getDescription());
            String obstacleStatus = parseRequiredString(request.getObstacleStatus());
            if (request.getRouteId() == null || request.getType() == null || request.getXCoordinate() == null
                    || request.getYCoordinate() == null || request.getDescription() == null || request.getObstacleStatus() == null) {
                return buildObstacleFailResponse(ResponseCode.MISSING_PARAM);
            }
            if (routeId == null || type == null || xCoordinate == null || yCoordinate == null || description == null || obstacleStatus == null) {
                return buildObstacleFailResponse(ResponseCode.INVALID_TYPE);
            }

            RouteTest routeTest = findRouteOrNull(routeId);
            if (routeTest == null) {
                return buildObstacleFailResponse(ResponseCode.PATH_NOT_FOUND);
            }

            ObstacleTest obstacleTest = new ObstacleTest();
            obstacleTest.setRouteId(routeTest);
            obstacleTest.setType(type);
            obstacleTest.setXCoordinate(xCoordinate);
            obstacleTest.setYCoordinate(yCoordinate);
            obstacleTest.setDescription(description);
            obstacleTest.setObstacleStatus(obstacleStatus);
            setSuccessAudit(obstacleTest);

            ObstacleTest saved = obstacleTestRepository.save(obstacleTest);
            return ObstacleTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .data(new ObstacleTestData(saved.getId(), saved.getRouteId().getRouteId(), saved.getType(),
                            saved.getXCoordinate(), saved.getYCoordinate(), saved.getDescription(), saved.getObstacleStatus()))
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            return buildObstacleFailResponse(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    public HeatmapDataTestResponse insertHeatmapDataTest(HeatmapDataTestRequest request) {
        try {
            if (request == null) {
                return buildHeatmapDataFailResponse(ResponseCode.MISSING_BODY);
            }

            String routeId = parseRequiredString(request.getRouteId());
            Double x = parseDouble(request.getX());
            Double y = parseDouble(request.getY());
            Double densityValue = parseDouble(request.getDensityValue());
            String statusMessage = parseRequiredString(request.getStatusMessage());
            Double radius = parseDouble(request.getRadius());
            if (request.getRouteId() == null || request.getX() == null || request.getY() == null
                    || request.getDensityValue() == null || request.getStatusMessage() == null || request.getRadius() == null) {
                return buildHeatmapDataFailResponse(ResponseCode.MISSING_PARAM);
            }
            if (routeId == null || x == null || y == null || densityValue == null || statusMessage == null || radius == null) {
                return buildHeatmapDataFailResponse(ResponseCode.INVALID_TYPE);
            }
            if (densityValue < 0 || radius < 0) {
                return buildHeatmapDataFailResponse(ResponseCode.INVALID_VALUE);
            }

            RouteTest routeTest = findRouteOrNull(routeId);
            if (routeTest == null) {
                return buildHeatmapDataFailResponse(ResponseCode.PATH_NOT_FOUND);
            }

            HeatMapDataTest heatMapDataTest = new HeatMapDataTest();
            heatMapDataTest.setRouteId(routeTest);
            heatMapDataTest.setX(x);
            heatMapDataTest.setY(y);
            heatMapDataTest.setDensityValue(densityValue);
            heatMapDataTest.setStatusMessage(statusMessage);
            heatMapDataTest.setRadius(radius);
            setSuccessAudit(heatMapDataTest);

            HeatMapDataTest saved = heatMapDataTestRepository.save(heatMapDataTest);
            return HeatmapDataTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .data(new HeatmapDataTestData(saved.getId(), saved.getRouteId().getRouteId(), saved.getX(),
                            saved.getY(), saved.getDensityValue(), saved.getStatusMessage(), saved.getRadius()))
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            return buildHeatmapDataFailResponse(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    public BottleneckDataTestResponse insertBottleneckDataTest(BottleneckDataTestRequest request) {
        try {
            if (request == null) {
                return buildBottleneckDataFailResponse(ResponseCode.MISSING_BODY);
            }

            String routeId = parseRequiredString(request.getRouteId());
            String edgeName = parseRequiredString(request.getEdgeName());
            Double x = parseDouble(request.getX());
            Double y = parseDouble(request.getY());
            Double occupancyRate = parseDouble(request.getOccupancyRate());
            if (request.getRouteId() == null || request.getEdgeName() == null || request.getX() == null
                    || request.getY() == null || request.getOccupancyRate() == null) {
                return buildBottleneckDataFailResponse(ResponseCode.MISSING_PARAM);
            }
            if (routeId == null || edgeName == null || x == null || y == null || occupancyRate == null) {
                return buildBottleneckDataFailResponse(ResponseCode.INVALID_TYPE);
            }
            if (occupancyRate < 0 || occupancyRate > 1) {
                return buildBottleneckDataFailResponse(ResponseCode.INVALID_VALUE);
            }

            RouteTest routeTest = findRouteOrNull(routeId);
            if (routeTest == null) {
                return buildBottleneckDataFailResponse(ResponseCode.PATH_NOT_FOUND);
            }

            BottlenecksDataTest bottleneck = new BottlenecksDataTest();
            bottleneck.setRouteId(routeTest);
            bottleneck.setEdgeName(edgeName);
            bottleneck.setX(x);
            bottleneck.setY(y);
            bottleneck.setOccupancyRate(occupancyRate);
            setSuccessAudit(bottleneck);

            BottlenecksDataTest saved = bottlenecksDataTestRepository.save(bottleneck);
            return BottleneckDataTestResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .data(new BottleneckDataTestData(saved.getId(), saved.getRouteId().getRouteId(), saved.getEdgeName(),
                            saved.getX(), saved.getY(), saved.getOccupancyRate()))
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            return buildBottleneckDataFailResponse(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    public FlowAlertResponse getFlowGetAlert(String token, List<String> currentEdges) {
        try {
            if (token == null
                    || token.trim().isBlank()
                    || currentEdges == null
                    || currentEdges.isEmpty()
                    || currentEdges.get(0) == null
                    || currentEdges.get(0).isBlank()) {
                return buildFlowAlertFailResponse(ResponseCode.MISSING_PARAM);
            }

            if (currentEdges.size() > 1) {
                return buildFlowAlertFailResponse(ResponseCode.INVALID_TYPE);
            }

            String edgeId = currentEdges.get(0);
            if (edgeId.contains("'")) {
                return buildFlowAlertFailResponse(ResponseCode.INVALID_TYPE);
            }

            String trimmedEdgeId = edgeId.trim();
            if (!edgeTestRepository.existsById(trimmedEdgeId)) {
                return buildFlowAlertFailResponse(ResponseCode.EDGE_NOT_FOUND);
            }

            List<String> blockedEdges = edgeStatusTestRepository.findBlockedEdgeIds(trimmedEdgeId);
            long now = System.currentTimeMillis();
            List<FlowAlertData> data = new ArrayList<>();
            for (int i = 0; i < blockedEdges.size(); i++) {
                data.add(new FlowAlertData("ALT_" + now + "_" + i, blockedEdges.get(i)));
            }

            return FlowAlertResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .data(data)
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            return buildFlowAlertFailResponse(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    private FlowAlertResponse buildFlowAlertFailResponse(ResponseCode responseCode) {
        return FlowAlertResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .usedInTest(false)
                .build();
    }

    public FlowDensityResponse getFlowGetDensity(List<String> routeIds) {
        try {
            if (routeIds == null || routeIds.isEmpty() || routeIds.get(0) == null || routeIds.get(0).isBlank()) {
                return buildFlowDensityFailResponse(ResponseCode.MISSING_PARAM);
            }

            if (routeIds.size() > 1) {
                return buildFlowDensityFailResponse(ResponseCode.INVALID_TYPE);
            }

            String routeId = routeIds.get(0);
            if (routeId.contains("'")) {
                return buildFlowDensityFailResponse(ResponseCode.INVALID_TYPE);
            }

            String trimmedRouteId = routeId.trim();
            if (!routeTestRepository.existsByRouteId(trimmedRouteId)) {
                return buildFlowDensityFailResponse(ResponseCode.PATH_NOT_FOUND);
            }

            Integer currentPeople = routeDensityTestRepository.findCurrentPeopleByRouteId(trimmedRouteId)
                    .orElse(0);

            return FlowDensityResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .data(new FlowDensityData(currentPeople))
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            return buildFlowDensityFailResponse(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    private FlowDensityResponse buildFlowDensityFailResponse(ResponseCode responseCode) {
        return FlowDensityResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .usedInTest(false)
                .build();
    }

    public FlowHeatmapResponse getFlowGetHeatmap(List<String> routeIds) {
        try {
            if (routeIds == null || routeIds.isEmpty() || routeIds.get(0) == null || routeIds.get(0).isBlank()) {
                return buildFlowHeatmapFailResponse(ResponseCode.MISSING_PARAM);
            }

            if (routeIds.size() > 1) {
                return buildFlowHeatmapFailResponse(ResponseCode.INVALID_TYPE);
            }

            String routeId = routeIds.get(0);
            if (routeId.contains("'")) {
                return buildFlowHeatmapFailResponse(ResponseCode.INVALID_TYPE);
            }

            String trimmedRouteId = routeId.trim();
            if (routeTestRepository.findByRouteId(trimmedRouteId).isEmpty()) {
                return buildFlowHeatmapFailResponse(ResponseCode.PATH_NOT_FOUND);
            }

            List<FlowHeatmapData> data = heatMapDataTestRepository.findByRouteId_RouteId(trimmedRouteId)
                    .stream()
                    .map(this::toFlowHeatmapData)
                    .toList();

            return FlowHeatmapResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .data(data)
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            return buildFlowHeatmapFailResponse(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    private FlowHeatmapData toFlowHeatmapData(HeatMapDataTest heatMapDataTest) {
        return new FlowHeatmapData(
                heatMapDataTest.getX(),
                heatMapDataTest.getY(),
                heatMapDataTest.getDensityValue(),
                heatMapDataTest.getStatusMessage(),
                heatMapDataTest.getRadius()
        );
    }

    private FlowHeatmapResponse buildFlowHeatmapFailResponse(ResponseCode responseCode) {
        return FlowHeatmapResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .usedInTest(false)
                .build();
    }

    public FlowBottleneckResponse getFlowGetBottleneck(List<String> routeIds) {
        try {
            if (routeIds == null || routeIds.isEmpty() || routeIds.get(0) == null || routeIds.get(0).isBlank()) {
                return buildFlowBottleneckFailResponse(ResponseCode.MISSING_PARAM);
            }

            if (routeIds.size() > 1) {
                return buildFlowBottleneckFailResponse(ResponseCode.INVALID_TYPE);
            }

            String routeId = routeIds.get(0);
            if (routeId.contains("'")) {
                return buildFlowBottleneckFailResponse(ResponseCode.INVALID_TYPE);
            }

            String trimmedRouteId = routeId.trim();
            if (routeTestRepository.findByRouteId(trimmedRouteId).isEmpty()) {
                return buildFlowBottleneckFailResponse(ResponseCode.PATH_NOT_FOUND);
            }

            List<FlowBottleneckData> data = bottlenecksDataTestRepository
                    .findByRouteId_RouteIdAndOccupancyRateGreaterThan(trimmedRouteId, 0.8)
                    .stream()
                    .map(this::toFlowBottleneckData)
                    .toList();

            return FlowBottleneckResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .data(data)
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            return buildFlowBottleneckFailResponse(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    private FlowBottleneckData toFlowBottleneckData(BottlenecksDataTest bottlenecksDataTest) {
        String severity = bottlenecksDataTest.getOccupancyRate() != null && bottlenecksDataTest.getOccupancyRate() > 0.9
                ? "CRITICAL"
                : "WARNING";

        return new FlowBottleneckData(
                bottlenecksDataTest.getEdgeName(),
                bottlenecksDataTest.getX(),
                bottlenecksDataTest.getY(),
                severity
        );
    }

    private FlowBottleneckResponse buildFlowBottleneckFailResponse(ResponseCode responseCode) {
        return FlowBottleneckResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .usedInTest(false)
                .build();
    }

    public FlowEdgeStatusResponse getFlowGetEdgeStatus(List<String> edgeIds) {
        try {
            if (edgeIds == null || edgeIds.isEmpty() || edgeIds.get(0) == null || edgeIds.get(0).trim().isBlank()) {
                return buildFlowEdgeStatusFailResponse(ResponseCode.MISSING_PARAM);
            }

            if (edgeIds.size() > 1) {
                return buildFlowEdgeStatusFailResponse(ResponseCode.INVALID_TYPE);
            }

            String edgeId = edgeIds.get(0);
            if (edgeId.contains("'")) {
                return buildFlowEdgeStatusFailResponse(ResponseCode.INVALID_TYPE);
            }

            if (!edgeTestRepository.existsById(edgeId)) {
                return buildFlowEdgeStatusFailResponse(ResponseCode.EDGE_NOT_FOUND);
            }

            EdgeDensityTest edgeDensityTest = edgeDensityTestRepository.findByEdgeId_EdgeId(edgeId)
                    .orElse(null);
            if (edgeDensityTest == null) {
                return buildFlowEdgeStatusFailResponse(ResponseCode.DENSITY_UNAVAILABLE);
            }

            return FlowEdgeStatusResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .data(new FlowEdgeStatusData(
                            edgeDensityTest.getEdgeId().getEdgeId(),
                            edgeDensityTest.getCurrentCount(),
                            edgeDensityTest.getFillPercentage()
                    ))
                    .usedInTest(false)
                    .build();
        } catch (Exception e) {
            return buildFlowEdgeStatusFailResponse(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    private FlowEdgeStatusResponse buildFlowEdgeStatusFailResponse(ResponseCode responseCode) {
        return FlowEdgeStatusResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .usedInTest(false)
                .build();
    }

    private RouteTest findRouteOrNull(String routeId) {
        return routeTestRepository.findByRouteId(routeId).orElse(null);
    }

    private String parseRequiredString(Object value) {
        if (!(value instanceof String text)) {
            return null;
        }

        String trimmed = text.trim();
        if (trimmed.isBlank() || trimmed.contains("'")) {
            return null;
        }

        return trimmed;
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

    private Double parseDouble(Object value) {
        if (!(value instanceof Number number)) {
            return null;
        }

        double doubleValue = number.doubleValue();
        if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
            return null;
        }

        return doubleValue;
    }

    private void setSuccessAudit(RouteDensityTest model) {
        model.setStatus("success");
        model.setCode(ResponseCode.SUCCESS.getCode());
        model.setMessage(ResponseCode.SUCCESS.getMessage());
        model.setUsedInTest(false);
    }

    private void setSuccessAudit(ObstacleTest model) {
        model.setStatus("success");
        model.setCode(ResponseCode.SUCCESS.getCode());
        model.setMessage(ResponseCode.SUCCESS.getMessage());
        model.setUsedInTest(false);
    }

    private void setSuccessAudit(HeatMapDataTest model) {
        model.setStatus("success");
        model.setCode(ResponseCode.SUCCESS.getCode());
        model.setMessage(ResponseCode.SUCCESS.getMessage());
        model.setUsedInTest(false);
    }

    private void setSuccessAudit(BottlenecksDataTest model) {
        model.setStatus("success");
        model.setCode(ResponseCode.SUCCESS.getCode());
        model.setMessage(ResponseCode.SUCCESS.getMessage());
        model.setUsedInTest(false);
    }

    private RouteTestResponse buildRouteFailResponse(ResponseCode responseCode) {
        return RouteTestResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .usedInTest(false)
                .build();
    }

    private RouteDensityTestResponse buildRouteDensityFailResponse(ResponseCode responseCode) {
        return RouteDensityTestResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .usedInTest(false)
                .build();
    }

    private ObstacleTestResponse buildObstacleFailResponse(ResponseCode responseCode) {
        return ObstacleTestResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .usedInTest(false)
                .build();
    }

    private HeatmapDataTestResponse buildHeatmapDataFailResponse(ResponseCode responseCode) {
        return HeatmapDataTestResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .usedInTest(false)
                .build();
    }

    private BottleneckDataTestResponse buildBottleneckDataFailResponse(ResponseCode responseCode) {
        return BottleneckDataTestResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status("fail")
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .usedInTest(false)
                .build();
    }

    public String cleanRouteTestData(){
        routeTestRepository.deleteAllInBatch();
        return "Route Test Data Cleared";
    }
    public String cleanRouteDensityTestData(){
        routeDensityTestRepository.deleteAllInBatch();
        return "Route Density Test Data Cleared";
    }
    public String cleanObstacleTestData(){
        obstacleTestRepository.deleteAllInBatch();
        return "Obstacle Test Data Cleared";
    }
    public String cleanHeatMapDataTestData(){
        heatMapDataTestRepository.deleteAllInBatch();
        return "Heat Map Data Test Data Cleared";
    }
    public String cleanBottleneckDataTestData(){
        bottlenecksDataTestRepository.deleteAllInBatch();
        return "Bottleneck Data Test Data Cleared";
    }
}
