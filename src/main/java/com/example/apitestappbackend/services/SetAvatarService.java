package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.SetAvatar.SetAvatarData;
import com.example.apitestappbackend.DTO.SetAvatar.SetAvatarRequest;
import com.example.apitestappbackend.DTO.SetAvatar.SetAvatarResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.SetAvatar;
import com.example.apitestappbackend.models.hospitaldb.UserTest;
import com.example.apitestappbackend.repository.LoggedInUsersRepository;
import com.example.apitestappbackend.repository.SetAvatarRepository;
import com.example.apitestappbackend.repository.UserTestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class SetAvatarService {
    private static final Logger log = LoggerFactory.getLogger(SetAvatarService.class);
    private final SetAvatarRepository setAvatarRepository;
    private final LoggedInUsersRepository loggedInUsersRepository;
    private final UserTestRepository userTestRepository;

    public SetAvatarService(SetAvatarRepository setAvatarRepository, LoggedInUsersRepository loggedInUsersRepository, UserTestRepository userTestRepository) {
        this.setAvatarRepository = setAvatarRepository;
        this.loggedInUsersRepository = loggedInUsersRepository;
        this.userTestRepository = userTestRepository;
    }

    public List<SetAvatar> findAll() {
        return setAvatarRepository.findAll();
    }

    public boolean isUserLoggedInWithPhoneNumber(String phoneNumber) {
        return loggedInUsersRepository.existsByPhoneNumber(phoneNumber);
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        String regexPhoneNumber = "^(0|\\+84)(3[2-9]|5[6-9]|7[0-9]|8[1-9]|9[0-9])\\d{7}$";
        return phoneNumber != null && phoneNumber.matches(regexPhoneNumber);
    }

    private boolean isAvatarUrlValid(String avatarUrl) {
        try {
            URI uri = new URI(avatarUrl);
            String scheme = uri.getScheme();
            return ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))
                    && uri.getHost() != null
                    && !uri.getHost().isBlank();
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public SetAvatarResponse setAvatar(SetAvatarRequest request) {
        SetAvatar savedS;

        try {

            if (request == null
                    || request.getPhoneNumber() == null
                    || request.getPhoneNumber().isBlank()
                    || request.getAvatarUrl() == null
                    || request.getAvatarUrl().isBlank()) {
                return SetAvatarResponse.builder()
                        .status("fail")
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.MISSING_PARAM.getCode())
                        .message(ResponseCode.MISSING_PARAM.getMessage())
                        .usedInTest(false)
                        .build();
            }

            String phoneNumber = request.getPhoneNumber().trim();
            String avatarUrl = request.getAvatarUrl().trim();

            if (!isPhoneNumberValid(phoneNumber) || !isAvatarUrlValid(avatarUrl)) {
                return SetAvatarResponse.builder()
                        .status("fail")
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.INVALID_VALUE.getCode())
                        .message(ResponseCode.INVALID_VALUE.getMessage())
                        .usedInTest(false)
                        .build();
            }

            UserTest newUT = userTestRepository.findByPhoneNumber(phoneNumber).orElse(null);
            if (newUT == null) {
                return SetAvatarResponse.builder()
                        .status("fail")
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.USER_NOT_FOUND.getCode())
                        .message(ResponseCode.USER_NOT_FOUND.getMessage())
                        .usedInTest(false)
                        .build();
            }

            if (!isUserLoggedInWithPhoneNumber(phoneNumber)) {
                return SetAvatarResponse.builder()
                        .status("fail")
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .code(ResponseCode.USER_NOT_LOGGED_IN.getCode())
                        .message(ResponseCode.USER_NOT_LOGGED_IN.getMessage())
                        .usedInTest(false)
                        .build();
            }
            log.error("Set avatar " + phoneNumber + " " + avatarUrl);
            SetAvatar s = new SetAvatar();
            s.setPhoneNumber(phoneNumber);
            s.setAvatarUrlInput(avatarUrl);
            s.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            s.setStatus("success");
            s.setCode(ResponseCode.SUCCESS.getCode());
            s.setMessage(ResponseCode.SUCCESS.getMessage());
            s.setUsedInTest(false);
            s.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            newUT.setAvatar(s.getAvatarUrlInput());

            savedS = setAvatarRepository.save(s);
            userTestRepository.save(newUT);

            return SetAvatarResponse.builder()
                    .status(savedS.getStatus())
                    .timestamp(savedS.getTimeStamp())
                    .code(savedS.getCode())
                    .message(savedS.getMessage())
                    .usedInTest(false)
                    .data(new SetAvatarData(
                            savedS.getId(),
                            savedS.getPhoneNumber(),
                            savedS.getAvatarUrlInput()
                    ))
                    .createdAt(savedS.getCreatedAt())
                    .build();

        } catch (Exception e) {
            return SetAvatarResponse.builder()
                    .status("fail")
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .usedInTest(false)
                    .build();
        }

    }
}
