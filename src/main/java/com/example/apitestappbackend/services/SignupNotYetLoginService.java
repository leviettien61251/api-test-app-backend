package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.SignUp.SignUpData;
import com.example.apitestappbackend.DTO.SignUp.SignUpRequest;
import com.example.apitestappbackend.DTO.SignUp.SignUpResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.SignupNotYetLogin;
import com.example.apitestappbackend.repository.SignupNotYetLoginRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SignupNotYetLoginService {
    private SignupNotYetLoginRepository signupNotYetLoginRepository;

    public SignupNotYetLoginService(SignupNotYetLoginRepository signupNotYetLoginRepository) {
        this.signupNotYetLoginRepository = signupNotYetLoginRepository;
    }

    public List<SignupNotYetLogin> findAll_() {
        List<SignupNotYetLogin> list = signupNotYetLoginRepository.findAll_();
        return list;
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

    public void deleteById(String id) {
        signupNotYetLoginRepository.deleteById(id);
    }

    public boolean isPhoneNumberExists(String phone_number) {
        List<SignupNotYetLogin> list =
                signupNotYetLoginRepository.existsSignupNotYetLoginByPhone_number(phone_number);
        return list.size() > 0;
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        String regexPhoneNumber = "^(0|\\+84)(3[2-9]|5[6-9]|7[0-9]|8[1-9]|9[0-9])\\d{7}$";
        return phoneNumber != null && phoneNumber.matches(regexPhoneNumber);
    }

    private boolean isPasswordValid(String password) {
        String regexPassword = "^[^\\s]{6,50}$";
        return password != null && password.matches(regexPassword);
    }

    private List<String> generateSignUpData() {
        int baseNumber = 1;

        int totalBulkTests = 10000;
        List<String> phones = new ArrayList<>();
        List<SignUpRequest> requests = new ArrayList<>();

        for (int i = 0; i < totalBulkTests; i++) {
            // Generate odd numbers: 1111111, 1111113, 1111115, ..., 1111309
            // (increment by 2 to ensure all digits are odd)
            //int phoneNumber = baseNumber + (i * 2);
            int phoneNumber = baseNumber + i;
            String viettelPhone = "098" + String.format("%06d", phoneNumber);
            phones.add(viettelPhone);
            System.out.println(viettelPhone);
        }

        return phones;

    }

    public void save10000() {
        String password = "111111";
        List<String> phones = generateSignUpData();
        List<SignupNotYetLogin> list = new ArrayList<>();
        
        for (String p : phones) {
            SignupNotYetLogin s = new SignupNotYetLogin();
            s.setPhoneNumber(p);
            s.setPassword(password);
            s.setSignupStatus("success");
            s.setUsedInTest(false);
            s.setCode(ResponseCode.SUCCESS.getCode());
            s.setMessage(ResponseCode.SUCCESS.getMessage());
            list.add(s);
            
            // Save in batches of 1000 to avoid memory issues
            if (list.size() == 1000) {
                signupNotYetLoginRepository.saveAll(list);
                log.info("Saved {} records", list.size());
                list.clear();
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
}
