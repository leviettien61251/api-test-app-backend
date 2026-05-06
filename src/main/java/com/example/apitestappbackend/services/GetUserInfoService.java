package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.GetUserInfo.GetUserInfoRequest;
import com.example.apitestappbackend.DTO.GetUserInfo.GetUserInfoResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.GetUserInfo;
import com.example.apitestappbackend.repository.GetUserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
public class GetUserInfoService {
    private final GetUserInfoRepository getUserInfoRepository;
//    private final LoggedInUsersService loggedInUsersService;

    public GetUserInfoService(GetUserInfoRepository getUserInfoRepository) {
        this.getUserInfoRepository = getUserInfoRepository;
    }

    public List<GetUserInfo> findAll() {
        return getUserInfoRepository.findAll();
    }

    private boolean isUserExists(String phoneNumber) {
        return false;
    }

    public GetUserInfoResponse getUserInfo(GetUserInfoRequest request) {
        try {
//            if

            return GetUserInfoResponse.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("fail")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .usedInTest(false)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
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

}
