package com.example.apitestappbackend.controllers;

import com.example.apitestappbackend.DTO.FlowTest.FlowAlertResponse;
import com.example.apitestappbackend.DTO.FlowTest.FlowBottleneckResponse;
import com.example.apitestappbackend.DTO.FlowTest.FlowDensityResponse;
import com.example.apitestappbackend.DTO.FlowTest.FlowEdgeStatusResponse;
import com.example.apitestappbackend.DTO.FlowTest.FlowHeatmapResponse;
import com.example.apitestappbackend.services.FlowTestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class FlowTestController {
    private final FlowTestService flowTestService;

    public FlowTestController(FlowTestService flowTestService) {
        this.flowTestService = flowTestService;
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
