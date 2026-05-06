package com.example.apitestappbackend.controllers;

import com.example.apitestappbackend.DTO.SetUserInfo.SetUserInfoRequest;
import com.example.apitestappbackend.DTO.SetUserInfo.SetUserInfoResponse;
import com.example.apitestappbackend.models.SetUserInfo;
import com.example.apitestappbackend.services.SetUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class SetUserInfoController {
    private final SetUserInfoService setUserInfoService;

    public SetUserInfoController(SetUserInfoService setUserInfoService) {
        this.setUserInfoService = setUserInfoService;
    }

    @GetMapping("/set-user-info")
    public List<SetUserInfo> findAll() {
        return setUserInfoService.findAll();
    }

    @GetMapping("/set-user-info-by-id")
    public SetUserInfo findById(@RequestParam("id") String id) {
            return setUserInfoService.findById(id);
    }

    @GetMapping("/set-user-info-by-phone-number")
    public SetUserInfo findByPhoneNumber(@RequestParam("phone_number") String phone_number) {
        return setUserInfoService.findByPhoneNumber(phone_number);
    }

    @PostMapping("/set-user-info")
    public ResponseEntity<SetUserInfoResponse> setUserInfo(@RequestBody SetUserInfoRequest request) {
        log.info("Set user info request for phone: {}", request.getPhoneNumber());
        SetUserInfoResponse res = setUserInfoService.setUserInfo(request);
        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(res);
    }

}
