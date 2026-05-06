package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.SetUserInfo.SetUserInfoData;
import com.example.apitestappbackend.DTO.SetUserInfo.SetUserInfoRequest;
import com.example.apitestappbackend.DTO.SetUserInfo.SetUserInfoResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.SetUserInfo;
import com.example.apitestappbackend.repository.LoggedInUsersRepository;
import com.example.apitestappbackend.repository.SetUserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
public class SetUserInfoService {
    private final SetUserInfoRepository setUserInfoRepository;
    private final LoggedInUsersRepository loggedInUsersRepository;

    public SetUserInfoService(SetUserInfoRepository setUserInfoRepository, LoggedInUsersRepository loggedInUsersRepository) {
        this.setUserInfoRepository = setUserInfoRepository;
        this.loggedInUsersRepository = loggedInUsersRepository;
    }

    public List<SetUserInfo> findAll() {
        return setUserInfoRepository.findAll();
    }

    public SetUserInfo findById(String id) {
        return setUserInfoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User with id: " + id + "does not exist!")
        );
    }

    public SetUserInfo findByPhoneNumber(String phoneNumber) {
        return setUserInfoRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new IllegalArgumentException("User with phone number: " + phoneNumber + "does not exist!")
        );
    }

    public boolean isUserLoggedInWithPhoneNumber(String phoneNumber) {
        return loggedInUsersRepository.existsByPhoneNumber(phoneNumber);
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        String regexPhoneNumber = "^(0|\\+84)(3[2-9]|5[6-9]|7[0-9]|8[1-9]|9[0-9])\\d{7}$";
        return phoneNumber != null && phoneNumber.matches(regexPhoneNumber);
    }
    private boolean isNameValid(String name) {
        String regexName = "^[a-zA-Z\\s]+$";
        return name != null && name.matches(regexName);

    }
    public SetUserInfoResponse setUserInfo(SetUserInfoRequest request) {
        SetUserInfo savedS;

        try {
            if (request.getFullName().isBlank()) {
                return SetUserInfoResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message("Không được để trống tên")
                        .usedInTest(false)
                        .createdAt(new Timestamp(System.currentTimeMillis()))
                        .build();
            }
            if (request.getPhoneNumber().isBlank()) {
                return SetUserInfoResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message("Không được để trống mật khẩu")
                        .usedInTest(false)
                        .createdAt(new Timestamp(System.currentTimeMillis()))
                        .build();
            }
            if (request.getAddress().isBlank()) {
                return SetUserInfoResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message("Không được để trống địa chỉ")
                        .usedInTest(false)
                        .createdAt(new Timestamp(System.currentTimeMillis()))
                        .build();
            }
            if (!isPhoneNumberValid(request.getPhoneNumber().trim())) {
                return SetUserInfoResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message("Số điện thoại không hợp lệ")
                        .usedInTest(false)
                        .createdAt(new Timestamp(System.currentTimeMillis()))
                        .build();

            }
            if (!isUserLoggedInWithPhoneNumber(request.getPhoneNumber().trim())) {
                return SetUserInfoResponse.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.USER_NOT_LOGGED_IN.getCode())
                        .message(ResponseCode.USER_NOT_LOGGED_IN.getMessage())
                        .usedInTest(false)
                        .createdAt(new Timestamp(System.currentTimeMillis()))
                        .build();
            }


            SetUserInfo newS = new SetUserInfo();
            newS.setFullName(request.getFullName().trim());
            newS.setPhoneNumber(request.getPhoneNumber().trim());
            newS.setAddress(request.getAddress().trim());
            newS.setStatus("success");
            newS.setCode(ResponseCode.SUCCESS.getCode());
            newS.setMessage(ResponseCode.SUCCESS.getMessage());
            newS.setUsedInTest(false);
            savedS = setUserInfoRepository.save(newS);

            return SetUserInfoResponse.builder()
                    .timestamp(savedS.getTimeStamp())
                    .status(savedS.getStatus())
                    .code(savedS.getCode())
                    .message(savedS.getMessage())
                    .usedInTest(savedS.getUsedInTest())
                    .createdAt(savedS.getCreatedAt())
                    .data(new SetUserInfoData(
                                    savedS.getId(),
                                    savedS.getFullName(),
                                    savedS.getPhoneNumber(),
                                    savedS.getAddress()
                            )
                    )
                    .build();
        } catch (Exception e) {
            log.error("Set user info error: {}", String.valueOf(e));
            return SetUserInfoResponse.builder()
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
