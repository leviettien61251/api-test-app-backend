package com.example.apitestappbackend.services;

import com.example.apitestappbackend.DTO.LoginTest.LoginData;
import com.example.apitestappbackend.DTO.LoginTest.LoginRequest;
import com.example.apitestappbackend.DTO.LoginTest.LoginResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.LoggedInUsers;
import com.example.apitestappbackend.models.SignupNotYetLogin;
import com.example.apitestappbackend.models.hospitaldb.UserTest;
import com.example.apitestappbackend.repository.LoggedInUsersRepository;
import com.example.apitestappbackend.repository.LoggedOutUserRepository;
import com.example.apitestappbackend.repository.SignupNotYetLoginRepository;
import com.example.apitestappbackend.repository.UserTestRepository;
import com.example.apitestappbackend.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class LoggedInUsersService {
    @Autowired
    private JwtUtil jwtUtil;

    private final LoggedInUsersRepository loggedInUsersRepository;
    private final LoggedOutUserRepository loggedOutUserRepository;
    private final SignupNotYetLoginRepository signupNotYetLoginRepository;
    private final UserTestRepository userTestRepository;

    public LoggedInUsersService(LoggedInUsersRepository loggedInUsersRepository,
                                SignupNotYetLoginRepository signupNotYetLoginRepository,
                                UserTestRepository userTestRepository,
                                LoggedOutUserRepository loggedOutUserRepository) {
        this.loggedInUsersRepository = loggedInUsersRepository;
        this.signupNotYetLoginRepository = signupNotYetLoginRepository;
        this.userTestRepository = userTestRepository;
        this.loggedOutUserRepository = loggedOutUserRepository;
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

    @Transactional
    public String cleanLoginData() {
        List<LoggedInUsers> loggedInUsers = loggedInUsersRepository.findAll();
        if (loggedInUsers.isEmpty()) {
            return "No login data to clean";
        }

        Set<String> phoneNumbers = new LinkedHashSet<>();
        for (LoggedInUsers loggedInUser : loggedInUsers) {
            if (loggedInUser.getPhoneNumber() != null && !loggedInUser.getPhoneNumber().isBlank()) {
                phoneNumbers.add(loggedInUser.getPhoneNumber().trim());
            }
        }

        if (!phoneNumbers.isEmpty()) {
            userTestRepository.deleteByPhoneNumberIn(phoneNumbers);
        }

        loggedInUsersRepository.deleteAllInBatch();
        return "Successfully cleaned login data for " + phoneNumbers.size() + " user(s)";
    }

    public LoggedInUsers findUserByPhoneNumber(String phoneNumber) {
        return loggedInUsersRepository.findLoggedInUsersByPhoneNumber(phoneNumber);
    }

    private boolean isPhoneNumberExists(String phoneNumber) {
        return loggedInUsersRepository.existsByPhoneNumber(phoneNumber);
    }

    private boolean isUserSignUp(String phoneNumber) {
        return userTestRepository.existsByPhoneNumber(phoneNumber);
    }

    private boolean isPasswordCorrect(String password) {
        return userTestRepository.existsByPassword(password);
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        String regexPhoneNumber = "^(0|\\+84)(3[2-9]|5[6-9]|7[0-9]|8[1-9]|9[0-9])\\d{7}$";
        return phoneNumber != null && phoneNumber.matches(regexPhoneNumber);
    }

    private boolean isPasswordValid(String password) {
        String regexPassword = "^[^\\s]{6,50}$";
        return password != null && password.matches(regexPassword);
    }

    private boolean isNewUser(String phoneNumber) {
        return phoneNumber != null && userTestRepository.existsByPhoneNumber(phoneNumber);
    }

    private boolean isPhoneNumberEven(String phoneNumber) {
        return phoneNumber != null && Integer.parseInt(phoneNumber) % 2 == 0;
    }

    private boolean isPhoneNumberEvenAndDivisionBy16(String phoneNumber) {
        return phoneNumber != null && Integer.parseInt(phoneNumber) % 16 == 0;
    }

    public void generateLoginData() {
        List<SignupNotYetLogin> signUpList = signupNotYetLoginRepository.findAll();
        List<String> phones = new ArrayList<>();
        List<LoggedInUsers> loggedList = new ArrayList<>();
        List<UserTest> newUsers = new ArrayList<>();
        int batchSize = 1000;

        if (signUpList.isEmpty()) {
            log.info("No signup data found");
            return;
        }

        for (SignupNotYetLogin s : signUpList) {
            if (isPhoneNumberEven(s.getPhoneNumber().trim())) {
                log.info("Phone number is even: {}", s.getPhoneNumber());
                phones.add(s.getPhoneNumber());

                String phone = s.getPhoneNumber().trim();
                String password = s.getPassword().trim();
                String token = jwtUtil.generateToken(phone);
                String refreshToken = jwtUtil.generateRefreshToken(phone);
                Timestamp tokenExpiresAt = jwtUtil.getExpiration(token);

                // Save to LoggedInUsers
                LoggedInUsers l = new LoggedInUsers();
                l.setPhoneNumber(phone);
                l.setPassword(password);
                l.setLoginStatus("success");
                l.setToken(token);
                l.setRefreshToken(refreshToken);
                l.setTokenExpiresAt(tokenExpiresAt);
                l.setUsedInTest(false);
                l.setCode(ResponseCode.SUCCESS.getCode());
                l.setMessage(ResponseCode.SUCCESS.getMessage());
                loggedList.add(l);

                // Check and update/create UserTest
                Optional<UserTest> existingUser = userTestRepository.findByPhoneNumber(phone);
                if (existingUser.isPresent()) {
                    // Update only token info for existing user
                    userTestRepository.updateTokenInfo(phone, token, refreshToken, tokenExpiresAt);
                } else {
                    // Create new user
                    UserTest ut = new UserTest();
                    ut.setPhoneNumber(phone);
                    ut.setPassword(password);
                    ut.setFullname(phone);
                    ut.setAddress("");
                    ut.setAvatar("");
                    ut.setToken(token);
                    ut.setRefreshToken(refreshToken);
                    ut.setTokenExpiresAt(tokenExpiresAt);
                    newUsers.add(ut);
                }

                // Batch save
                if (phones.size() >= batchSize) {
                    saveBatch(loggedList, newUsers);
                    phones.clear();
                }
            }
        }

        // Save remaining records
        if (!phones.isEmpty()) {
            saveBatch(loggedList, newUsers);
        }

        log.info("Completed saving records");
    }

    private void saveBatch(List<LoggedInUsers> loggedList, List<UserTest> newUsers) {
        log.info("Saving {} records", loggedList.size());
        loggedInUsersRepository.saveAll(loggedList);
        if (!newUsers.isEmpty()) {
            userTestRepository.saveAll(newUsers);
        }
        loggedList.clear();
        newUsers.clear();
    }

    private void saveBatchRelogin(List<LoggedInUsers> loggedList, List<UserTest> updateList) {
        log.info("Saving {} logged users and updating {} users", loggedList.size(), updateList.size());

        if (!loggedList.isEmpty()) {
            loggedInUsersRepository.saveAll(loggedList);
            loggedList.clear();
        }

        if (!updateList.isEmpty()) {
            userTestRepository.saveAll(updateList); // saveAll sẽ tự động update nếu có ID
            updateList.clear();
        }
    }

    public void reloginEvenPhoneNumbersAndDivisionBy16() {
        // Lấy thông tin user đã logout (token trống hoặc null)
        List<UserTest> loggedOutList = userTestRepository.findAllWhereTokenIsEmpty();
        List<LoggedInUsers> loggedList = new ArrayList<>();
        List<UserTest> updateList = new ArrayList<>();
        int batchSize = 1000;
        int processedCount = 0;

        if (loggedOutList.isEmpty()) {
            log.info("No logged out users found");
            return;
        }

        log.info("Found {} logged out users to process", loggedOutList.size());

        for (UserTest user : loggedOutList) {
            String phoneNumber = user.getPhoneNumber();

            if (phoneNumber != null && isPhoneNumberEvenAndDivisionBy16(phoneNumber.trim())) {
                String phone = phoneNumber.trim();
                String token = jwtUtil.generateToken(phone);
                String refreshToken = jwtUtil.generateRefreshToken(phone);
                Timestamp tokenExpiresAt = jwtUtil.getExpiration(token);

                // 2. Lưu vào LoggedInUsers
                LoggedInUsers loggedInUser = new LoggedInUsers();
                loggedInUser.setPhoneNumber(phone);
                loggedInUser.setPassword("111111");
                loggedInUser.setLoginStatus("success");
                loggedInUser.setToken(token);
                loggedInUser.setRefreshToken(refreshToken);
                loggedInUser.setTokenExpiresAt(tokenExpiresAt);
                loggedInUser.setUsedInTest(false);
                loggedInUser.setCode(ResponseCode.SUCCESS.getCode());
                loggedInUser.setMessage(ResponseCode.SUCCESS.getMessage());
                loggedList.add(loggedInUser);

                // 3. Cập nhật token cho UserTest hiện tại
                user.setToken(token);
                user.setRefreshToken(refreshToken);
                user.setTokenExpiresAt(tokenExpiresAt);
                updateList.add(user);

                processedCount++;

                if (processedCount % batchSize == 0) {
                    saveBatch(loggedList, updateList);
                    log.info("Processed {} records", processedCount);
                }
            }
        }

        if (!loggedList.isEmpty() || !updateList.isEmpty()) {
            saveBatchRelogin(loggedList, updateList);
        }

        log.info("Completed saving records. Total processed: {}", processedCount);
    }

    public LoginResponse login(LoginRequest request) {

        String token = jwtUtil.generateToken(request.getPhoneNumber().trim());
        String refreshToken = jwtUtil.generateRefreshToken(request.getPhoneNumber().trim());
        Timestamp tokenExpiresAt = jwtUtil.getExpiration(token);

        LoggedInUsers savedL;
        UserTest savedUT;
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
            l.setToken(token);
            l.setRefreshToken(refreshToken);
            l.setTokenExpiresAt(tokenExpiresAt);
            l.setUsedInTest(false);
            l.setCode(ResponseCode.SUCCESS.getCode());
            l.setMessage(ResponseCode.SUCCESS.getMessage());

            //lưu data vào logged_in_users
            savedL = loggedInUsersRepository.save(l);

            //kiểm tra xem user cũ hay mới
            if (!isNewUser(savedL.getPhoneNumber().trim())) {
                //nếu mới thì lưu userTest mới
                UserTest ut = new UserTest();
                ut.setPhoneNumber(l.getPhoneNumber());
                ut.setPassword(l.getPassword());
                ut.setFullname(l.getPhoneNumber());// user mới mặc định fullName là sđt
                ut.setAddress("");
                ut.setAvatar("");
                ut.setToken(l.getToken());
                ut.setRefreshToken(l.getRefreshToken());
                ut.setTokenExpiresAt(l.getTokenExpiresAt());

                userTestRepository.save(ut);
            } else {
                //nếu cũ thì tìm userTest theo số điện thoại
                UserTest utOld = userTestRepository.findByPhoneNumber(savedL.getPhoneNumber())
                        .orElseThrow(
                                () -> new IllegalArgumentException("User with phone number: " + savedL.getPhoneNumber() + "does not exist!"));
                //gán token mới
                utOld.setPhoneNumber(l.getPhoneNumber());
                utOld.setToken(l.getToken());
                utOld.setRefreshToken(l.getRefreshToken());
                utOld.setTokenExpiresAt(l.getTokenExpiresAt());
                //lưu lại
                userTestRepository.save(utOld);

            }


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
