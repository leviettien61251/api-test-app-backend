package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.SignUpTest.SignUpData;
import com.example.apitestappbackend.DTO.SignUpTest.SignUpRequest;
import com.example.apitestappbackend.DTO.SignUpTest.SignUpResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.SignupNotYetLogin;
import com.example.apitestappbackend.models.hospitaldb.UserTest;
import com.example.apitestappbackend.repository.SignupNotYetLoginRepository;
import com.example.apitestappbackend.repository.UserTestRepository;
import com.example.apitestappbackend.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SignupNotYetLoginService {
    @Autowired
    private JwtUtil jwtUtil;

    private final SignupNotYetLoginRepository signupNotYetLoginRepository;
    private final UserTestRepository userTestRepository;

    public SignupNotYetLoginService(SignupNotYetLoginRepository signupNotYetLoginRepository, UserTestRepository userTestRepository) {
        this.signupNotYetLoginRepository = signupNotYetLoginRepository;
        this.userTestRepository = userTestRepository;
    }

    public List<SignupNotYetLogin> findAll_() {
        return signupNotYetLoginRepository.findAll_();
    }

    public void insert(SignupNotYetLogin s) {
        signupNotYetLoginRepository.save(s);
    }

    public SignupNotYetLogin update(String id, SignupNotYetLogin s) {
        SignupNotYetLogin newS = signupNotYetLoginRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Acc with id: " + id + "does not exist!")
        );
        newS.setPhoneNumber(s.getPhoneNumber());
        return signupNotYetLoginRepository.save(newS);
    }

    private boolean isNewUser(String phoneNumber) {
        return phoneNumber != null && userTestRepository.existsByPhoneNumber(phoneNumber);
    }

    public void deleteById(String id) {
        signupNotYetLoginRepository.deleteById(id);
    }

    public String cleanSignUpData() {
        signupNotYetLoginRepository.deleteAllInBatch();
        return "Successfully cleaned signup data";
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        String regexPhoneNumber = "^(0|\\+84)(3[2-9]|5[6-9]|7[0-9]|8[1-9]|9[0-9])\\d{7}$";
        return phoneNumber != null && phoneNumber.matches(regexPhoneNumber);
    }

    private boolean isPasswordValid(String password) {
        String regexPassword = "^\\S{6,50}$";
        return password != null && password.matches(regexPassword);
    }

    private List<String> generateSignUpData() {
        int baseNumber = 1;

        int totalBulkTests = 10000;
        List<String> phones = new ArrayList<>();

        for (int i = 0; i < totalBulkTests; i++) {
            int phoneNumber = baseNumber + i;
            String viettelPhone = "0980" + String.format("%06d", phoneNumber);
            phones.add(viettelPhone);
            System.out.println(viettelPhone);
        }

        return phones;

    }

    public void save10000() {
        String password = "111111";
        List<String> phones = generateSignUpData();
        List<SignupNotYetLogin> list = new ArrayList<>();
        List<UserTest> newUsers = new ArrayList<>();

        for (String p : phones) {
            SignupNotYetLogin s = new SignupNotYetLogin();
            s.setPhoneNumber(p);
            s.setPassword(password);
            s.setSignupStatus("success");
            s.setUsedInTest(false);
            s.setCode(ResponseCode.SUCCESS.getCode());
            s.setMessage(ResponseCode.SUCCESS.getMessage());
            list.add(s);

            String token = jwtUtil.generateToken(p);
            String refreshToken = jwtUtil.generateRefreshToken(p);
            Timestamp tokenExpiresAt = jwtUtil.getExpiration(token);

            // Check and update/create UserTest
            Optional<UserTest> existingUser = userTestRepository.findByPhoneNumber(p);
            if (existingUser.isPresent()) {
                // Update only token info for existing user
                userTestRepository.updateTokenInfo(p, token, refreshToken, tokenExpiresAt);
            } else {
                // Create new user
                UserTest ut = new UserTest();
                ut.setPhoneNumber(p);
                ut.setPassword(password);
                ut.setFullname(p);
                ut.setAddress("");
                ut.setAvatar("");
                ut.setToken(token);
                ut.setRefreshToken(refreshToken);
                ut.setTokenExpiresAt(tokenExpiresAt);
                newUsers.add(ut);
            }

            // Save in batches of 1000 to avoid memory issues
            if (list.size() == 1000) {
                signupNotYetLoginRepository.saveAll(list);
                userTestRepository.saveAll(newUsers);
                log.info("Saved {} records", list.size());
                list.clear();
                newUsers.clear();
            }
        }

        // Save remaining records
        if (!list.isEmpty()) {
            signupNotYetLoginRepository.saveAll(list);
            log.info("Saved final {} records", list.size());
        }
        log.info("Completed saving 10000 records");
    }

    public void generate10000SignUpData() {
        save10000();
    }

    public SignUpResponse signUp(SignUpRequest request) {
        SignupNotYetLogin savedS;
        try {
            if (request.getPhoneNumber().isBlank()) {
                return SignUpResponse.builder()
                        .signupStatus("fail")
                        .signupTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message("Thiếu trường phone_number")
                        .usedInTest(false)
                        .build();
            }
            if (request.getPassword().isBlank()) {
                return SignUpResponse.builder()
                        .signupStatus("fail")
                        .signupTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message("Thiếu trường password")
                        .usedInTest(false)
                        .build();
            }
            if (!isPhoneNumberValid(request.getPhoneNumber().trim())) {
                return SignUpResponse.builder()
                        .signupStatus("fail")
                        .signupTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message("Số điện thoại không hợp lệ")
                        .usedInTest(false)
                        .build();
            }
            if (!isPasswordValid(request.getPassword().trim())) {
                return SignUpResponse.builder()
                        .signupStatus("fail")
                        .signupTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message("Password không hợp lệ")
                        .usedInTest(false)
                        .build();
            }
            if (signupNotYetLoginRepository.existsByPhoneNumber(request.getPhoneNumber().trim())) {

                return SignUpResponse.builder()
                        .signupStatus("fail")
                        .signupTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.USER_EXISTS.getCode())
                        .message(ResponseCode.USER_EXISTS.getMessage())
                        .usedInTest(false)
                        .build();
            }
            SignupNotYetLogin s = new SignupNotYetLogin();
            s.setPhoneNumber(request.getPhoneNumber().trim());
            s.setPassword(request.getPassword().trim());
            s.setSignupStatus("success");
            s.setUsedInTest(false);
            s.setCode(ResponseCode.SUCCESS.getCode());
            s.setMessage(ResponseCode.SUCCESS.getMessage());

            savedS = signupNotYetLoginRepository.save(s);

            return SignUpResponse.builder()
                    .signupStatus(savedS.getSignupStatus())
                    .signupTimestamp(new Timestamp(System.currentTimeMillis()))
                    .code(savedS.getCode())
                    .message(savedS.getMessage())
                    .usedInTest(false)
                    .data(new SignUpData(
                            savedS.getId(),
                            savedS.getPhoneNumber(),
                            savedS.getCreatedAt()))
                    .build();
        } catch (Exception e) {

            log.error("Sign up error: ", e);
            return SignUpResponse.builder()
                    .signupStatus("fail")
                    .signupTimestamp(new Timestamp(System.currentTimeMillis()))
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .build();
        }


    }

    public SignUpResponse signUp_(SignUpRequest request) {
        String token = jwtUtil.generateToken(request.getPhoneNumber().trim());
        String refreshToken = jwtUtil.generateRefreshToken(request.getPhoneNumber().trim());
        Timestamp tokenExpiresAt = jwtUtil.getExpiration(token);
        SignupNotYetLogin savedS;
        UserTest savedUt;
        try {
            if (request.getPhoneNumber().isBlank()) {
                return SignUpResponse.builder()
                        .signupStatus("fail")
                        .signupTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message("Thiếu trường phone_number")
                        .usedInTest(false)
                        .build();
            }
            if (request.getPassword().isBlank()) {
                return SignUpResponse.builder()
                        .signupStatus("fail")
                        .signupTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message("Thiếu trường password")
                        .usedInTest(false)
                        .build();
            }
            if (!isPhoneNumberValid(request.getPhoneNumber().trim())) {
                return SignUpResponse.builder()
                        .signupStatus("fail")
                        .signupTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message("Số điện thoại không hợp lệ")
                        .usedInTest(false)
                        .build();
            }
            if (!isPasswordValid(request.getPassword().trim())) {
                return SignUpResponse.builder()
                        .signupStatus("fail")
                        .signupTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message("Password không hợp lệ")
                        .usedInTest(false)
                        .build();
            }
            if (userTestRepository.existsByPhoneNumber(request.getPhoneNumber().trim())) {

                return SignUpResponse.builder()
                        .signupStatus("fail")
                        .signupTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.USER_EXISTS.getCode())
                        .message(ResponseCode.USER_EXISTS.getMessage())
                        .usedInTest(false)
                        .build();
            }
            SignupNotYetLogin s = new SignupNotYetLogin();
            s.setPhoneNumber(request.getPhoneNumber().trim());
            s.setPassword(request.getPassword().trim());
            s.setSignupStatus("success");
            s.setUsedInTest(false);
            s.setCode(ResponseCode.SUCCESS.getCode());
            s.setMessage(ResponseCode.SUCCESS.getMessage());

            savedS = signupNotYetLoginRepository.save(s);
            //kiểm tra xem user cũ hay mới
            if (!isNewUser(savedS.getPhoneNumber().trim())) {
                //nếu mới thì lưu userTest mới
                UserTest ut = new UserTest();
                ut.setPhoneNumber(s.getPhoneNumber());
                ut.setPassword(s.getPassword());
                ut.setFullname(s.getPhoneNumber());// user mới mặc định fullName là sđt
                ut.setAddress("");
                ut.setAvatar("");
                ut.setToken(token);
                ut.setRefreshToken(refreshToken);
                ut.setTokenExpiresAt(tokenExpiresAt);

                userTestRepository.save(ut);
            } else {
                //nếu cũ thì tìm userTest theo số điện thoại
                UserTest utOld = userTestRepository.findByPhoneNumber(savedS.getPhoneNumber())
                        .orElseThrow(
                                () -> new IllegalArgumentException("User with phone number: " + savedS.getPhoneNumber() + "does not exist!"));
                //gán token mới
                utOld.setPhoneNumber(s.getPhoneNumber());
                utOld.setToken(token);
                utOld.setRefreshToken(refreshToken);
                utOld.setTokenExpiresAt(tokenExpiresAt);
                //lưu lại
                userTestRepository.save(utOld);

            }

            return SignUpResponse.builder()
                    .signupStatus(savedS.getSignupStatus())
                    .signupTimestamp(new Timestamp(System.currentTimeMillis()))
                    .code(savedS.getCode())
                    .message(savedS.getMessage())
                    .usedInTest(false)
                    .data(new SignUpData(
                            savedS.getId(),
                            savedS.getPhoneNumber(),
                            savedS.getCreatedAt()))
                    .build();
        } catch (Exception e) {

            log.error("Sign up error: ", e);
            return SignUpResponse.builder()
                    .signupStatus("fail")
                    .signupTimestamp(new Timestamp(System.currentTimeMillis()))
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .build();
        }


    }
}
