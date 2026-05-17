package com.example.apitestappbackend.controllers;

import com.example.apitestappbackend.DTO.NodeTest.NodeRequest;
import com.example.apitestappbackend.DTO.NodeTest.NodeResponse;
import com.example.apitestappbackend.services.NodeTestService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class NodeTestController {
    private final NodeTestService nodeTestService;

    public NodeTestController(NodeTestService nodeTestService) {
        this.nodeTestService = nodeTestService;
    }

    @PostMapping("/insert-node-test")
    public HttpEntity<NodeResponse> getNodeTest(@RequestBody NodeRequest request) {
        NodeResponse res = nodeTestService.insertNodeTest(request);

        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(res);
    }

    @GetMapping("/map/nodes")
    public HttpEntity<NodeResponse> getNodeTest(@RequestParam MultiValueMap<String, String> queryParams) {
        NodeResponse res = nodeTestService.getNodeTest(queryParams.get("floor_id"));
        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(res);
    }
}
