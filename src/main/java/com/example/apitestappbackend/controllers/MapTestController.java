package com.example.apitestappbackend.controllers;

import com.example.apitestappbackend.DTO.MapTest.MapTestRequest;
import com.example.apitestappbackend.DTO.MapTest.MapTestResponse;
import com.example.apitestappbackend.DTO.SavedSearch.SavedSearchRequest;
import com.example.apitestappbackend.DTO.SavedSearch.SavedSearchResponse;
import com.example.apitestappbackend.DTO.StepTest.StepTestRequest;
import com.example.apitestappbackend.DTO.StepTest.StepTestResponse;
import com.example.apitestappbackend.DTO.WardTest.WardTestRequest;
import com.example.apitestappbackend.DTO.WardTest.WardTestResponse;
import com.example.apitestappbackend.services.MapTestService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class MapTestController {
    private final MapTestService mapTestService;

    public MapTestController(MapTestService mapTestService) {
        this.mapTestService = mapTestService;
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

    @PostMapping("/generateMapDataBuildingAB_5")
    public HttpEntity<MapTestResponse> generateMapDataBuildingAB_5() {
        MapTestResponse res = mapTestService.generateMapTestDataBuildingAAndB_5();

        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(res);
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

    @GetMapping("/map/edges")
    public HttpEntity<StepTestResponse> getEdges(@RequestParam MultiValueMap<String, String> queryParams) {
        StepTestResponse res = mapTestService.getEdges(queryParams.get("floor_id"));
        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(res);
    }

    @GetMapping("/map/meta")
    public HttpEntity<MapTestResponse> getMeta(@RequestParam MultiValueMap<String, String> queryParams) {
        MapTestResponse res = mapTestService.getMeta(queryParams.get("floor_id"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @PostMapping("/map/search")
    public HttpEntity<SavedSearchResponse> postSavedSearch(@RequestBody SavedSearchRequest request) {
        SavedSearchResponse res = mapTestService.postSavedSearch(request);
        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(res);
    }

    @PostMapping("/map/insert-ward")
    public HttpEntity<WardTestResponse> postWardTest(@RequestBody WardTestRequest request) {
        WardTestResponse res = mapTestService.insertWardTest(request);
        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(res);
    }

    @DeleteMapping("/clean/map")
    public HttpEntity<String> cleanMapData() {
        return ResponseEntity.ok(mapTestService.cleanMapData());
    }

    @DeleteMapping("/clean/map/node")
    public HttpEntity<String> cleanNodeData() {
        return ResponseEntity.ok(mapTestService.cleanNodeData());
    }

    @DeleteMapping("/clean/map/step")
    public HttpEntity<String> cleanStepData() {
        return ResponseEntity.ok(mapTestService.cleanStepData());
    }

    @DeleteMapping("/clean/map/search")
    public HttpEntity<String> cleanSearchData() {
        return ResponseEntity.ok(mapTestService.cleanSavedSearchData());
    }

    @DeleteMapping("/clean/map/ward")
    public HttpEntity<String> cleanWardData() {
        return ResponseEntity.ok(mapTestService.cleanWardData());
    }


}
