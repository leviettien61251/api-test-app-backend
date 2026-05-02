package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.Login.LoginData;
import com.example.apitestappbackend.DTO.Login.LoginRequest;
import com.example.apitestappbackend.DTO.Login.LoginResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.LoggedInUsers;
import com.example.apitestappbackend.repository.LoggedInUsersRepository;
import com.example.apitestappbackend.repository.SignupNotYetLoginRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class LoggedInUsersService {
    private final LoggedInUsersRepository loggedInUsersRepository;
    private final SignupNotYetLoginRepository signupNotYetLoginRepository;

    public LoggedInUsersService(LoggedInUsersRepository loggedInUsersRepository, SignupNotYetLoginRepository signupNotYetLoginRepository) {
        this.loggedInUsersRepository = loggedInUsersRepository;
        this.signupNotYetLoginRepository = signupNotYetLoginRepository;
    }

    public List<LoggedInUsers> findAll() {
        return loggedInUsersRepository.findAll();
    }

    public LoggedInUsers findById(String id) {
        return loggedInUsersRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User with id: " + id + "does not exist!")
        );
    }

    public void insert(LoggedInUsers u) {
        loggedInUsersRepository.save(u);
    }

    public LoggedInUsers updateById(String id, LoggedInUsers u) {
        LoggedInUsers savedU = loggedInUsersRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User with id: " + id + "does not exist!")
        );
        savedU.setPhoneNumber(u.getPhoneNumber());
        return loggedInUsersRepository.save(savedU);
    }

    public LoggedInUsers findUserByPhoneNumber(String phoneNumber) {
        return loggedInUsersRepository.findLoggedInUsersByPhoneNumber(phoneNumber);
    }

    private boolean isPhoneNumberExists(String phoneNumber) {
        return loggedInUsersRepository.existsByPhoneNumber(phoneNumber);
    }

    private boolean isUserSignUp(String phoneNumber) {
        return signupNotYetLoginRepository.existsByPhoneNumber(phoneNumber);
    }

    private boolean isPasswordCorrect(String password) {
        return signupNotYetLoginRepository.existsByPassword(password);
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        String regexPhoneNumber = "^(0|\\+84)(3[2-9]|5[6-9]|7[0-9]|8[1-9]|9[0-9])\\d{7}$";
        return phoneNumber != null && phoneNumber.matches(regexPhoneNumber);
    }

    private boolean isPasswordValid(String password) {
        String regexPassword = "^[^\\s]{6,50}$";
        return password != null && password.matches(regexPassword);
    }

    public LoginResponse login(LoginRequest request) {
        LoggedInUsers savedL;
        try {
            if (request.getPhoneNumber() == null) {
                return LoginResponse.builder()
                        .loginStatus("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message("Thiếu trường phone_number")
                        .usedInTest(false)
                        .build();
            }
            if (request.getPassword() == null) {
                return LoginResponse.builder()
                        .loginStatus("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message("Thiếu trường password")
                        .usedInTest(false)
                        .build();
            }

            if (request.getPhoneNumber().isBlank()) {
                return LoginResponse.builder()
                        .loginStatus("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message("Thiếu trường phone_number")
                        .usedInTest(false)
                        .build();
            }
            if (request.getPassword().isBlank()) {
                return LoginResponse.builder()
                        .loginStatus("fail")
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message("Thiếu trường password")
                        .usedInTest(false)
                        .build();
            }
            if (!isPhoneNumberValid(request.getPhoneNumber().trim())) {
                return LoginResponse.builder()
                        .loginStatus("fail")
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message("Số điện thoại không hợp lệ")
                        .usedInTest(false)
                        .build();
            }

            if (!isPasswordValid(request.getPassword().trim())) {
                return LoginResponse.builder()
                        .loginStatus("fail")
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message("Password không hợp lệ")
                        .usedInTest(false)
                        .build();
            }
            if (isPhoneNumberExists(request.getPhoneNumber().trim())) {
                return LoginResponse.builder()
                        .loginStatus("fail")
                        .loginTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.USER_EXISTS.getCode())
                        .message(ResponseCode.USER_EXISTS.getMessage())
                        .usedInTest(false)
                        .build();
            }
            if (!isUserSignUp(request.getPhoneNumber().trim())) {
                return LoginResponse.builder()
                        .loginStatus("fail")
                        .loginTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.USER_NOT_FOUND.getCode())
                        .message(ResponseCode.USER_NOT_FOUND.getMessage())
                        .usedInTest(false)
                        .build();
            }


            if (!isPasswordCorrect(request.getPassword().trim())) {
                return LoginResponse.builder()
                        .loginStatus("fail")
                        .loginTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.PASSWORD_INCORRECT.getCode())
                        .message(ResponseCode.PASSWORD_INCORRECT.getMessage())
                        .usedInTest(false)
                        .build();
            }


            LoggedInUsers l = new LoggedInUsers();
            l.setPhoneNumber(request.getPhoneNumber().trim());
            l.setPassword(request.getPassword().trim());
            l.setLoginStatus("success");
            l.setToken("token was set");
            l.setRefreshToken("refresh token");
            l.setTokenExpiresAt(Timestamp.from(Instant.now().plusSeconds(3600)));
            l.setUsedInTest(false);
            l.setCode(ResponseCode.SUCCESS.getCode());
            l.setMessage(ResponseCode.SUCCESS.getMessage());

            savedL = loggedInUsersRepository.save(l);

            return LoginResponse.builder()
                    .loginStatus(savedL.getLoginStatus())
                    .loginTimestamp(new Timestamp(System.currentTimeMillis()))
                    .token(savedL.getToken())
                    .refreshToken(savedL.getRefreshToken())
                    .tokenExpiresAt(savedL.getTokenExpiresAt())
                    .code(savedL.getCode())
                    .message(savedL.getMessage())
                    .usedInTest(savedL.getUsedInTest())
                    .data(new LoginData(
                            savedL.getId(),
                            savedL.getPhoneNumber(),
                            savedL.getCreatedAt()
                    ))
                    .build();

        } catch (Exception e) {
            log.error("Login error: {}", String.valueOf(e));
            return LoginResponse.builder()
                    .loginStatus("fail")
                    .loginTimestamp(new Timestamp(System.currentTimeMillis()))
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .build();
        }


    }
}
