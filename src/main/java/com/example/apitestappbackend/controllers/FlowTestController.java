package com.example.apitestappbackend.controllers;

import com.example.apitestappbackend.DTO.BottleneckDataTest.BottleneckDataTestRequest;
import com.example.apitestappbackend.DTO.BottleneckDataTest.BottleneckDataTestResponse;
import com.example.apitestappbackend.DTO.FlowTest.*;
import com.example.apitestappbackend.DTO.HeatmapDataTest.HeatmapDataTestRequest;
import com.example.apitestappbackend.DTO.HeatmapDataTest.HeatmapDataTestResponse;
import com.example.apitestappbackend.DTO.ObstacleTest.ObstacleTestRequest;
import com.example.apitestappbackend.DTO.ObstacleTest.ObstacleTestResponse;
import com.example.apitestappbackend.DTO.RouteDensityTest.RouteDensityTestRequest;
import com.example.apitestappbackend.DTO.RouteDensityTest.RouteDensityTestResponse;
import com.example.apitestappbackend.DTO.RouteTest.RouteTestRequest;
import com.example.apitestappbackend.DTO.RouteTest.RouteTestResponse;
import com.example.apitestappbackend.services.FlowTestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class FlowTestController {
    private final FlowTestService flowTestService;

    public FlowTestController(FlowTestService flowTestService) {
        this.flowTestService = flowTestService;
    }

    @DeleteMapping("/clean/route")
    public HttpEntity<String> cleanRouteData() {
        return ResponseEntity.ok(flowTestService.cleanRouteTestData());
    }

    @DeleteMapping("/clean/route-density")
    public HttpEntity<String> cleanRouteDensityData() {
        return ResponseEntity.ok(flowTestService.cleanRouteDensityTestData());
    }

    @DeleteMapping("/clean/obstacle")
    public HttpEntity<String> cleanObstacleData() {
        return ResponseEntity.ok(flowTestService.cleanObstacleTestData());
    }

    @DeleteMapping("/clean/heatmap-data")
    public HttpEntity<String> cleanHeatmapData() {
        return ResponseEntity.ok(flowTestService.cleanHeatMapDataTestData());
    }

    @DeleteMapping("/clean/bottleneck-data")
    public HttpEntity<String> cleanBottleneckData() {
        return ResponseEntity.ok(flowTestService.cleanBottleneckDataTestData());
    }

    @PostMapping("/flow/insert-route")
    public HttpEntity<RouteTestResponse> insertRouteTest(@RequestBody(required = false) RouteTestRequest request) {
        RouteTestResponse res = flowTestService.insertRouteTest(request);
        return ResponseEntity.status(res.getStatus().equals("success") ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(res);
    }

    @PostMapping("/flow/insert-route-density")
    public HttpEntity<RouteDensityTestResponse> insertRouteDensityTest(@RequestBody(required = false) RouteDensityTestRequest request) {
        RouteDensityTestResponse res = flowTestService.insertRouteDensityTest(request);
        return ResponseEntity.status(res.getStatus().equals("success") ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(res);
    }

    @PostMapping("/flow/insert-obstacle")
    public HttpEntity<ObstacleTestResponse> insertObstacleTest(@RequestBody(required = false) ObstacleTestRequest request) {
        ObstacleTestResponse res = flowTestService.insertObstacleTest(request);
        return ResponseEntity.status(res.getStatus().equals("success") ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(res);
    }

    @PostMapping("/flow/insert-heatmap-data")
    public HttpEntity<HeatmapDataTestResponse> insertHeatmapDataTest(@RequestBody(required = false) HeatmapDataTestRequest request) {
        HeatmapDataTestResponse res = flowTestService.insertHeatmapDataTest(request);
        return ResponseEntity.status(res.getStatus().equals("success") ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(res);
    }

    @PostMapping("/flow/insert-bottleneck-data")
    public HttpEntity<BottleneckDataTestResponse> insertBottleneckDataTest(@RequestBody(required = false) BottleneckDataTestRequest request) {
        BottleneckDataTestResponse res = flowTestService.insertBottleneckDataTest(request);
        return ResponseEntity.status(res.getStatus().equals("success") ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(res);
    }

    @GetMapping("/flow/get_alerts")
    public HttpEntity<FlowAlertResponse> getFlowGetAlert(@RequestParam MultiValueMap<String, String> queryParams,
                                                         HttpServletRequest request) {
        FlowAlertResponse res = flowTestService.getFlowGetAlert(
                extractToken(request),
                queryParams.get("current_edge")
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @GetMapping("/flow/get_density")
    public HttpEntity<FlowDensityResponse> getFlowGetDensity(@RequestParam MultiValueMap<String, String> queryParams) {
        FlowDensityResponse res = flowTestService.getFlowGetDensity(queryParams.get("route_id"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @GetMapping("/flow/get_heatmap")
    public HttpEntity<FlowHeatmapResponse> getFlowGetHeatmap(@RequestParam MultiValueMap<String, String> queryParams) {
        FlowHeatmapResponse res = flowTestService.getFlowGetHeatmap(queryParams.get("route_id"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @GetMapping("/flow/get_bottlenecks")
    public HttpEntity<FlowBottleneckResponse> getFlowGetBottleneck(@RequestParam MultiValueMap<String, String> queryParams) {
        FlowBottleneckResponse res = flowTestService.getFlowGetBottleneck(queryParams.get("route_id"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @GetMapping("/flow/edge_status")
    public HttpEntity<FlowEdgeStatusResponse> getFlowGetEdgeStatus(@RequestParam MultiValueMap<String, String> queryParams) {
        FlowEdgeStatusResponse res = flowTestService.getFlowGetEdgeStatus(queryParams.get("edge_id"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            return null;
        }

        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7).trim();
        }

        return authHeader.trim();
    }
}
