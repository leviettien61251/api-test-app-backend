package com.example.apitestappbackend.controllers;

import com.example.apitestappbackend.DTO.MapTest.MapTestRequest;
import com.example.apitestappbackend.DTO.MapTest.MapTestResponse;
import com.example.apitestappbackend.services.MapTestService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/map-test")
public class MapTestController {
    private final MapTestService mapTestService;

    public MapTestController(MapTestService mapTestService) {
        this.mapTestService = mapTestService;
    }

    @PostMapping("/map-test")
    public HttpEntity<MapTestResponse> mapTest(@RequestBody MapTestRequest request) {
        MapTestResponse res = mapTestService.mapTest(request);

        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(res);
    }
}
