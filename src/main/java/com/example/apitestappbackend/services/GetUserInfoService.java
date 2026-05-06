package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.GetUserInfo.GetUserInfoData;
import com.example.apitestappbackend.DTO.GetUserInfo.GetUserInfoRequest;
import com.example.apitestappbackend.DTO.GetUserInfo.GetUserInfoResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.GetUserInfo;
import com.example.apitestappbackend.models.UserTest;
import com.example.apitestappbackend.repository.GetUserInfoRepository;
import com.example.apitestappbackend.repository.UserTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
public class GetUserInfoService {
    private final GetUserInfoRepository getUserInfoRepository;
    private final UserTestRepository userTestRepository;

    public GetUserInfoService(GetUserInfoRepository getUserInfoRepository, UserTestRepository userTestRepository) {
        this.getUserInfoRepository = getUserInfoRepository;
        this.userTestRepository = userTestRepository;
    }

    public List<GetUserInfo> findAll() {
        return getUserInfoRepository.findAll();
    }

    public GetUserInfoResponse getUserInfo(GetUserInfoRequest request) {
        GetUserInfo savedGUI;
        UserTest userTest;
        try {
            if (request.getPhoneNumber().trim().isBlank()) {
                return GetUserInfoResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .createdAt(new Timestamp(System.currentTimeMillis()))
                        .build();
            }
            if (!isPhoneNumberValid(request.getPhoneNumber().trim())) {
                return GetUserInfoResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message(ResponseCode.INVALID_VALUE.getMessage())
                        .usedInTest(false)
                        .createdAt(new Timestamp(System.currentTimeMillis()))
                        .build();
            }
            if (!isUserExists(request.getPhoneNumber().trim())) {
                return GetUserInfoResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.USER_NOT_FOUND.getCode())
                        .message(ResponseCode.USER_NOT_FOUND.getMessage())
                        .usedInTest(false)
                        .createdAt(new Timestamp(System.currentTimeMillis()))
                        .build();
            }

            userTest = userTestRepository.findByPhoneNumber(request.getPhoneNumber().trim())
                    .orElseThrow(
                            () -> new IllegalArgumentException("User with phone number: " + request.getPhoneNumber().trim() + "does not exist!")
                    );

            GetUserInfo newGUI = new GetUserInfo();
            newGUI.setPhoneNumber(request.getPhoneNumber().trim());
            newGUI.setFullName(userTest.getFullname());
            newGUI.setAvatar(userTest.getAvatar());
            newGUI.setAddress(userTest.getAddress());
            newGUI.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            newGUI.setStatus("success");
            newGUI.setCode(ResponseCode.SUCCESS.getCode());
            newGUI.setMessage(ResponseCode.SUCCESS.getMessage());
            newGUI.setUsedInTest(false);
            newGUI.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            savedGUI = getUserInfoRepository.save(newGUI);

            return GetUserInfoResponse.builder()
                    .timestamp(savedGUI.getTimeStamp())
                    .status(savedGUI.getStatus())
                    .code(savedGUI.getCode())
                    .message(savedGUI.getMessage())
                    .usedInTest(savedGUI.getUsedInTest())
                    .data(new GetUserInfoData(
                            savedGUI.getId(),
                            savedGUI.getFullName(),
                            savedGUI.getPhoneNumber(),
                            savedGUI.getAvatar(),
                            savedGUI.getAddress()
                    ))
                    .createdAt(savedGUI.getCreatedAt())
                    .build();


        } catch (Exception e) {
            log.error("Error getting user info: ", e);
            return GetUserInfoResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("fail")
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();
        }
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        String regexPhoneNumber = "^(0|\\+84)(3[2-9]|5[6-9]|7[0-9]|8[1-9]|9[0-9])\\d{7}$";
        return phoneNumber != null && phoneNumber.matches(regexPhoneNumber);
    }

    private boolean isUserExists(String phoneNumber) {
        return userTestRepository.existsByPhoneNumber(phoneNumber);
    }

}
