package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.LogoutTest.LoggedOutUserData;
import com.example.apitestappbackend.DTO.LogoutTest.LoggedOutUserResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.LoggedOutUser;
import com.example.apitestappbackend.models.hospitaldb.UserTest;
import com.example.apitestappbackend.repository.LoggedOutUserRepository;
import com.example.apitestappbackend.repository.UserTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LoggedOutUserService {
    private final LoggedOutUserRepository loggedOutUserRepository;
    private final UserTestRepository userTestRepository;

    public LoggedOutUserService(LoggedOutUserRepository loggedOutUserRepository, UserTestRepository userTestRepository) {
        this.loggedOutUserRepository = loggedOutUserRepository;
        this.userTestRepository = userTestRepository;
    }

    public List<LoggedOutUser> findAll() {
        return loggedOutUserRepository.findAll();
    }

    public String cleanLogoutData() {
        loggedOutUserRepository.deleteAllInBatch();
        return "Successfully cleaned logout data";
    }

    private boolean isTokenValid(String token) {
        boolean exists = userTestRepository.existsByToken(token);
        boolean invalidated = loggedOutUserRepository.existsByInvalidatedToken(token);
        return exists && !invalidated;
    }


    private boolean isPhoneNumberEvenAndDivisonBy8(String phoneNumber) {
        return phoneNumber != null && Integer.parseInt(phoneNumber) % 2 == 0 && Integer.parseInt(phoneNumber) % 8 == 0;
    }

    public void testLogoutData() {
        List<UserTest> userTestList = userTestRepository.findAll();
        List<LoggedOutUser> logoutList = new ArrayList<>();

        if (userTestList.isEmpty()) {
            log.info("No user test data found");
            return;
        }

        for (UserTest item : userTestList) {
            System.out.println(item.getPhoneNumber() + " " + item.getPassword());
            if (isPhoneNumberEvenAndDivisonBy8(item.getPhoneNumber().trim())) {

                // Tạo LoggedOutUser record
                LoggedOutUser l = new LoggedOutUser();
                l.setPhoneNumber(item.getPhoneNumber().trim());
                l.setInvalidatedToken(item.getToken());
                l.setTimeStamp(new Timestamp(System.currentTimeMillis()));
                l.setStatus("success");
                l.setCode(ResponseCode.SUCCESS.getCode());
                l.setMessage(ResponseCode.SUCCESS.getMessage());
                l.setUsedInTest(false);
                l.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                logoutList.add(l);

                // Clear token info trong UserTest
                userTestRepository.clearTokenInfo(item.getPhoneNumber().trim());

                // Batch save cho LoggedOutUser
                if (logoutList.size() == 1000) {
                    loggedOutUserRepository.saveAll(logoutList);
                    logoutList.clear();
                }
            }
        }

        // Save remaining records
        if (!logoutList.isEmpty()) {
            loggedOutUserRepository.saveAll(logoutList);
        }

        log.info("Completed logout process");
    }

    public LoggedOutUserResponse logout(String authHeader) {
        LoggedOutUser savedLOU;
        String token = authHeader.substring(7); // remove "Bearer ";


        try {
            if (!authHeader.startsWith("Bearer ")) {
                return LoggedOutUserResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.TOKEN_INVALID.getCode())
                        .message(ResponseCode.TOKEN_INVALID.getMessage())
                        .usedInTest(false)
                        .build();
            }
            if (!isTokenValid(token)) {
                log.error("Invalid token: {}", token);
                return LoggedOutUserResponse
                        .builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status("fail")
                        .code(ResponseCode.TOKEN_INVALID.getCode())
                        .message(ResponseCode.TOKEN_INVALID.getMessage())
                        .usedInTest(false)
                        .build();
            }

            UserTest ut = userTestRepository.findByToken(token.trim())
                    .orElseThrow(
                            () -> new IllegalArgumentException("User with token: " + token + "does not exist!")
                    );
            ut.setToken("");
            userTestRepository.save(ut);

            LoggedOutUser l = new LoggedOutUser();
            l.setPhoneNumber(ut.getPhoneNumber().trim());
            l.setInvalidatedToken(token);
            l.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            l.setStatus("success");
            l.setCode(ResponseCode.SUCCESS.getCode());
            l.setMessage(ResponseCode.SUCCESS.getMessage());
            l.setUsedInTest(false);
            l.setCreatedAt(new Timestamp(System.currentTimeMillis()));


            savedLOU = loggedOutUserRepository.save(l);

            return LoggedOutUserResponse
                    .builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("success")
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .usedInTest(false)
                    .data(new LoggedOutUserData(
                            savedLOU.getId(),
                            savedLOU.getPhoneNumber(),
                            savedLOU.getInvalidatedToken()
                    ))
                    .build();
        } catch (Exception e) {
            log.error("Error logging out user: ", e);
            return LoggedOutUserResponse
                    .builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status("fail")
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .build();
        }
    }
}
