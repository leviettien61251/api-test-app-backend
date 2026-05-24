package com.example.apitestappbackend.services;

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
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.hospitaldb.BottlenecksDataTest;
import com.example.apitestappbackend.models.hospitaldb.EdgeDensityTest;
import com.example.apitestappbackend.models.hospitaldb.HeatMapDataTest;
import com.example.apitestappbackend.repository.BottlenecksDataTestRepository;
import com.example.apitestappbackend.repository.EdgeDensityTestRepository;
import com.example.apitestappbackend.repository.HeatMapDataTestRepository;
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

    public FlowTestService(EdgeTestRepository edgeTestRepository,
                           EdgeStatusTestRepository edgeStatusTestRepository,
                           RouteTestRepository routeTestRepository,
                           RouteDensityTestRepository routeDensityTestRepository,
                           HeatMapDataTestRepository heatMapDataTestRepository,
                           BottlenecksDataTestRepository bottlenecksDataTestRepository,
                           EdgeDensityTestRepository edgeDensityTestRepository) {
        this.edgeTestRepository = edgeTestRepository;
        this.edgeStatusTestRepository = edgeStatusTestRepository;
        this.routeTestRepository = routeTestRepository;
        this.routeDensityTestRepository = routeDensityTestRepository;
        this.heatMapDataTestRepository = heatMapDataTestRepository;
        this.bottlenecksDataTestRepository = bottlenecksDataTestRepository;
        this.edgeDensityTestRepository = edgeDensityTestRepository;
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
            if (routeTestRepository.findByRouteId(trimmedRouteId).isEmpty()) {
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
                            edgeDensityTest.getCurrentCoutn(),
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
}
