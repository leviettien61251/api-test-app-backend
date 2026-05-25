package com.example.apitestappbackend.controllers;

import com.example.apitestappbackend.DTO.SetAvatar.SetAvatarRequest;
import com.example.apitestappbackend.DTO.SetAvatar.SetAvatarResponse;
import com.example.apitestappbackend.services.SetAvatarService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class SetAvatarController {
    private final SetAvatarService setAvatarService;

    public SetAvatarController(SetAvatarService setAvatarService) {
        this.setAvatarService = setAvatarService;
    }


    @PostMapping("/set-avatar")
    public ResponseEntity<SetAvatarResponse> setAvatar(@RequestBody SetAvatarRequest request) {
        SetAvatarResponse res = setAvatarService.setAvatar(request);

        HttpStatus status = res.getStatus().equals("success")
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(res);
    }

    @DeleteMapping("/clean/set-avatar")
    public HttpEntity<String> cleanDataSetAvatar() {
        return ResponseEntity.ok(setAvatarService.cleanDataSetAvatar());
    }
}
