package com.example.apitestappbackend.controllers;

import com.example.apitestappbackend.DTO.MapTest.MapTestRequest;
import com.example.apitestappbackend.DTO.MapTest.MapTestResponse;
import com.example.apitestappbackend.DTO.StepTest.StepTestRequest;
import com.example.apitestappbackend.DTO.StepTest.StepTestResponse;
import com.example.apitestappbackend.services.MapTestService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class MapTestController {
    private final MapTestService mapTestService;

    public MapTestController(MapTestService mapTestService) {
        this.mapTestService = mapTestService;
    }

    @PostMapping("/insert-map-test")
    public HttpEntity<MapTestResponse> insertMapTest(@RequestBody MapTestRequest request) {
        MapTestResponse res = mapTestService.insertMapTest(request);

        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(res);
    }

    @PostMapping("/map/insert-step")
    public HttpEntity<StepTestResponse> insertStep(@RequestBody StepTestRequest request) {
        StepTestResponse res = mapTestService.insertStep(request);

        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(res);
    }

    @GetMapping("/map/floors")
    public HttpEntity<MapTestResponse> getFloors(@RequestParam(value = "building_code", required = false) String buildingCode) {
        MapTestResponse res = mapTestService.getFloors(buildingCode);

        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(res);
    }

    @DeleteMapping("/map/clean")
    public HttpEntity<String> cleanMapData() {
        return ResponseEntity.ok(mapTestService.cleanMapData());
    }

    @DeleteMapping("/map/node/clean")
    public HttpEntity<String> cleanNodeData() {
        return ResponseEntity.ok(mapTestService.cleanNodeData());
    }

    @DeleteMapping("/map/step/clean")
    public HttpEntity<String> cleanStepData() {
        return ResponseEntity.ok(mapTestService.cleanStepData());
    }
}
