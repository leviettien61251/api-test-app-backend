package com.example.apitestappbackend.controllers;

import com.example.apitestappbackend.DTO.GetUserInfo.GetUserInfoRequest;
import com.example.apitestappbackend.DTO.GetUserInfo.GetUserInfoResponse;
import com.example.apitestappbackend.services.GetUserInfoService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class GetUserInfoController {
    private final GetUserInfoService getUserInfoService;

    public GetUserInfoController(GetUserInfoService getUserInfoService) {
        this.getUserInfoService = getUserInfoService;
    }

    @PostMapping("/get-user-info")
    public HttpEntity<GetUserInfoResponse> getUserInfo(@RequestBody GetUserInfoRequest request) {

        GetUserInfoResponse res = getUserInfoService.getUserInfo(request);

        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(res);
    }

}
