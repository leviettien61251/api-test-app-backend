package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.PasswordChanged.PasswordChangedData;
import com.example.apitestappbackend.DTO.PasswordChanged.PasswordChangedRequest;
import com.example.apitestappbackend.DTO.PasswordChanged.PasswordChangedResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.LoggedInUsers;
import com.example.apitestappbackend.models.PasswordChanged;
import com.example.apitestappbackend.repository.LoggedInUsersRepository;
import com.example.apitestappbackend.repository.PasswordChangedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
public class PasswordChangedService {
    private final PasswordChangedRepository passwordChangedRepository;
    private final LoggedInUsersRepository loggedInUsersRepository;
    private final LoggedInUsersService loggedInUsersService;

    public PasswordChangedService(PasswordChangedRepository passwordChangedRepository, LoggedInUsersRepository loggedInUsersRepository, LoggedInUsersService loggedInUsersService) {
        this.passwordChangedRepository = passwordChangedRepository;
        this.loggedInUsersRepository = loggedInUsersRepository;
        this.loggedInUsersService = loggedInUsersService;
    }

    public List<PasswordChanged> findAll() {
        return passwordChangedRepository.findAll();
    }

    private boolean isPhoneNumberLoggedIn(String phoneNumber) {
        return phoneNumber != null && loggedInUsersRepository.existsByPhoneNumber(phoneNumber);
        // nếu phoneNumber này đã login thì sẽ có dữ liệu => true
    }

    private boolean isOldPasswordCorrect(String phoneNumber, String oldPassword) {
        return phoneNumber != null && oldPassword != null && loggedInUsersRepository.existsByPhoneNumberAndPassword(phoneNumber, oldPassword);
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        String regexPhoneNumber = "^(0|\\+84)(3[2-9]|5[6-9]|7[0-9]|8[1-9]|9[0-9])\\d{7}$";
        return phoneNumber != null && phoneNumber.matches(regexPhoneNumber);
    }

    private boolean isPasswordValid(String password) {
        String regexPassword = "^[^\\s]{6,50}$";
        return password != null && password.matches(regexPassword);
    }

    public PasswordChangedResponse changePassword(PasswordChangedRequest request) {
        PasswordChanged savedP;
        LoggedInUsers needChangeP;

        try {
            //nếu phoneNumber này chưa login => return user not logged in
            if (!isPhoneNumberLoggedIn(request.getPhoneNumber().trim())) {
                return PasswordChangedResponse.builder()
                        .status("fail")
                        .passwordChangedTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.USER_NOT_LOGGED_IN.getCode())
                        .message(ResponseCode.USER_NOT_LOGGED_IN.getMessage())
                        .usedInTest(false)
                        .build();
            }
            if(!isOldPasswordCorrect(request.getPhoneNumber().trim(), request.getOldPassword().trim())){
                return PasswordChangedResponse.builder()
                        .status("fail")
                        .passwordChangedTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.PASSWORD_INCORRECT.getCode())
                        .message(ResponseCode.PASSWORD_INCORRECT.getMessage())
                        .usedInTest(false)
                        .build();
            }
            if(request.getOldPassword().isBlank()){
                return PasswordChangedResponse.builder()
                        .status("fail")
                        .passwordChangedTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message("Old password bị để trống")
                        .usedInTest(false)
                        .build();
            }
            if(request.getNewPassword().isBlank()){
                return PasswordChangedResponse.builder()
                        .status("fail")
                        .passwordChangedTimestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message("New password bị để trống")
                        .usedInTest(false)
                        .build();
            }


            PasswordChanged p = new PasswordChanged();
            p.setPhoneNumber(request.getPhoneNumber().trim());
            p.setOldPassword(request.getOldPassword().trim());
            p.setNewPassword(request.getNewPassword().trim());
            p.setStatus("success");
            p.setOldPasswordStatus(PasswordStatus.PROVIDED.getStatus());
            p.setNewPasswordStatus(PasswordStatus.PROVIDED.getStatus());
            p.setUsedInTest(false);
            p.setCode(ResponseCode.SUCCESS.getCode());
            p.setMessage(ResponseCode.SUCCESS.getMessage());


            try{
                //update new password vào loggedInUser
                LoggedInUsers l = loggedInUsersService.findUserByPhoneNumber(request.getPhoneNumber().trim());
                l.setPassword(p.getNewPassword());

                needChangeP = loggedInUsersRepository.save(l);

            } catch (Exception e) {
                log.error("Logged User");
                return PasswordChangedResponse.builder()
                        .status("fail")
                        .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                        .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                        .usedInTest(false)
                        .build();
            }

            savedP = passwordChangedRepository.save(p);

            return PasswordChangedResponse.builder()
                    .status(savedP.getStatus())
                    .passwordChangedTimestamp(new Timestamp(System.currentTimeMillis()))
                    .oldPasswordStatus(savedP.getOldPassword())
                    .newPasswordStatus(savedP.getNewPassword())
                    .code(savedP.getCode())
                    .message(savedP.getMessage())
                    .usedInTest(savedP.getUsedInTest())
                    .data(new PasswordChangedData(
                            savedP.getId(),
                            savedP.getPhoneNumber(),
                            savedP.getOldPassword(),
                            savedP.getNewPassword()
                    ))
                    .build();
        } catch (Exception e) {
            log.error("Change password");
            return PasswordChangedResponse.builder()
                    .status("fail")
                    .passwordChangedTimestamp(new Timestamp(System.currentTimeMillis()))
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .build();
        }
    }

    public enum PasswordStatus {
        PROVIDED("PROVIDED"),
        EMPTY("EMPTY"),
        TOO_SHORT("TOO SHORT"),
        SAME_AS_OLD("SAME AS OLD");

        private final String status;

        PasswordStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
}
