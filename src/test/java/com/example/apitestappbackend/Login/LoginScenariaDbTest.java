package com.example.apitestappbackend.Login;

import com.example.apitestappbackend.DTO.LoginTest.LoginRequest;
import com.example.apitestappbackend.DTO.LoginTest.LoginResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.LoggedInUsers;
import com.example.apitestappbackend.models.SignupNotYetLogin;
import com.example.apitestappbackend.repository.LoggedInUsersRepository;
import com.example.apitestappbackend.repository.SignupNotYetLoginRepository;
import com.example.apitestappbackend.services.LoggedInUsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Login Scenario Database Tests - PostgreSQL Integration")
public class LoginScenariaDbTest {

    @Autowired
    private LoggedInUsersRepository loggedInUsersRepository;

    @Autowired
    private SignupNotYetLoginRepository signupNotYetLoginRepository;

    @Autowired
    private LoggedInUsersService loggedInUsersService;

    private static final String VALID_PHONE = "0901234567";
    private static final String VALID_PHONE_2 = "0912345678";
    private static final String INVALID_PHONE = "123";
    private static final String VALID_PASSWORD = "111111";
    private static final String WRONG_PASSWORD = "wrongpassword";

    @BeforeEach
    void setUp() {
        // Clear all records before each test
        loggedInUsersRepository.deleteAll();
        signupNotYetLoginRepository.deleteAll();
    }

    private SignupNotYetLogin createAndSaveSignupUser(String phoneNumber, String password) {
        SignupNotYetLogin user = new SignupNotYetLogin();
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        user.setSignupStatus("success");
        user.setCode(ResponseCode.SUCCESS.getCode());
        user.setMessage(ResponseCode.SUCCESS.getMessage());
        user.setUsedInTest(false);
        return signupNotYetLoginRepository.save(user);
    }

    private LoggedInUsers createAndSaveLoggedInUser(String phoneNumber, String password) {
        LoggedInUsers user = new LoggedInUsers();
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        user.setLoginStatus("success");
        user.setToken("token_value");
        user.setRefreshToken("refresh_token_value");
        user.setTokenExpiresAt(Timestamp.from(Instant.now().plusSeconds(3600)));
        user.setCode(ResponseCode.SUCCESS.getCode());
        user.setMessage(ResponseCode.SUCCESS.getMessage());
        user.setUsedInTest(false);
        return loggedInUsersRepository.save(user);
    }

    @Test
    @DisplayName("Testcase 1: Valid phone, registered account, correct password - Should PASS (Success login)")
    void testLoginWithValidPhoneAndCorrectPassword() {
        // Arrange - Create a registered user in signup_not_yet_login table
        createAndSaveSignupUser(VALID_PHONE, VALID_PASSWORD);

        // Verify user is in signup table
        assertTrue(signupNotYetLoginRepository.existsByPhoneNumber(VALID_PHONE),
                "User should exist in signup_not_yet_login table");

        LoginRequest request = new LoginRequest(VALID_PHONE, VALID_PASSWORD);

        // Act - Call login service
        LoginResponse response = loggedInUsersService.login(request);

        // Assert - Login should succeed
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getLoginStatus(), "Login status should be success");
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode(), "Response code should be 1000");
        assertEquals(ResponseCode.SUCCESS.getMessage(), response.getMessage());
        assertFalse(response.getUsedInTest());
        assertNotNull(response.getToken(), "Token should be generated");
        assertNotNull(response.getRefreshToken(), "RefreshToken should be generated");
        assertNotNull(response.getTokenExpiresAt(), "TokenExpiresAt should be set");
        assertNotNull(response.getData(), "Data should not be null");
        assertNotNull(response.getData().getId(), "User ID should be set");
        assertEquals(VALID_PHONE, response.getData().getPhoneNumber(), "Phone number should match");

        // Verify record is saved in logged_in_users table
        assertTrue(loggedInUsersRepository.existsByPhoneNumber(VALID_PHONE),
                "User should be saved in logged_in_users table");
        LoggedInUsers savedUser = loggedInUsersRepository.findLoggedInUsersByPhoneNumber(VALID_PHONE);
        assertNotNull(savedUser, "Saved user should not be null");
        assertEquals(VALID_PHONE, savedUser.getPhoneNumber());
    }

    @Test
    @DisplayName("Testcase 2: Valid phone, registered account, incorrect password - Should PASS (Login fails)")
    void testLoginWithValidPhoneButWrongPassword() {
        // Arrange - Create a registered user with different password
        createAndSaveSignupUser(VALID_PHONE, VALID_PASSWORD);

        LoginRequest request = new LoginRequest(VALID_PHONE, WRONG_PASSWORD);

        // Act - Call login service with wrong password
        LoginResponse response = loggedInUsersService.login(request);

        // Assert - Login should fail
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getLoginStatus(), "Login status should be fail");
        assertEquals(ResponseCode.PASSWORD_INCORRECT.getCode(), response.getCode(),
                "Response code should be 3008 (PASSWORD_INCORRECT)");
        assertEquals(ResponseCode.PASSWORD_INCORRECT.getMessage(), response.getMessage());
        assertFalse(response.getUsedInTest());
        assertNull(response.getToken(), "Token should not be generated");

        // Verify record is NOT saved in logged_in_users table
        assertFalse(loggedInUsersRepository.existsByPhoneNumber(VALID_PHONE),
                "User should NOT be saved in logged_in_users table on failed login");
    }

    @Test
    @DisplayName("Testcase 3: Valid phone, unregistered account - Should PASS (Login fails)")
    void testLoginWithValidPhoneButUnregisteredAccount() {
        // Arrange - Do NOT create any user in signup_not_yet_login table
        LoginRequest request = new LoginRequest(VALID_PHONE, VALID_PASSWORD);

        // Act - Call login service
        LoginResponse response = loggedInUsersService.login(request);

        // Assert - Login should fail (user not found)
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getLoginStatus(), "Login status should be fail");
        assertEquals(ResponseCode.USER_NOT_FOUND.getCode(), response.getCode(),
                "Response code should be 3007 (USER_NOT_FOUND)");
        assertEquals(ResponseCode.USER_NOT_FOUND.getMessage(), response.getMessage());
        assertFalse(response.getUsedInTest());
        assertNull(response.getToken(), "Token should not be generated");

        // Verify record is NOT saved in logged_in_users table
        assertFalse(loggedInUsersRepository.existsByPhoneNumber(VALID_PHONE),
                "User should NOT be saved in logged_in_users table");
    }

    @Test
    @DisplayName("Testcase 4: Valid phone, empty password - Should PASS (Login fails)")
    void testLoginWithValidPhoneButEmptyPassword() {
        // Arrange - Create registered user
        createAndSaveSignupUser(VALID_PHONE, VALID_PASSWORD);

        LoginRequest request = new LoginRequest(VALID_PHONE, "");

        // Act - Call login service with empty password
        LoginResponse response = loggedInUsersService.login(request);

        // Assert - Login should fail
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getLoginStatus(), "Login status should be fail");
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode(),
                "Response code should be 2001 (MISSING_PARAM)");
        assertTrue(response.getMessage().contains("password"), "Message should mention password");

        // Verify record is NOT saved
        assertFalse(loggedInUsersRepository.existsByPhoneNumber(VALID_PHONE),
                "User should NOT be saved in logged_in_users table");
    }

    @Test
    @DisplayName("Testcase 5: Invalid phone format, valid password - Should PASS (Login fails)")
    void testLoginWithInvalidPhoneButValidPassword() {
        // Arrange - Create user with valid phone for comparison
        createAndSaveSignupUser(VALID_PHONE, VALID_PASSWORD);

        LoginRequest request = new LoginRequest(INVALID_PHONE, VALID_PASSWORD);

        // Act - Call login service with invalid phone
        LoginResponse response = loggedInUsersService.login(request);

        // Assert - Login should fail due to invalid phone format
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getLoginStatus(), "Login status should be fail");
        assertEquals(ResponseCode.INVALID_VALUE.getCode(), response.getCode(),
                "Response code should be 2003 (INVALID_VALUE)");
        assertTrue(response.getMessage().contains("Số điện thoại") || response.getMessage().contains("phone"),
                "Message should mention phone");

        // Verify record is NOT saved
        assertFalse(loggedInUsersRepository.existsByPhoneNumber(INVALID_PHONE),
                "User should NOT be saved in logged_in_users table");
    }

    @Test
    @DisplayName("Testcase 6: Empty phone, valid password - Should PASS (Login fails)")
    void testLoginWithEmptyPhoneButValidPassword() {
        // Arrange
        LoginRequest request = new LoginRequest("", VALID_PASSWORD);

        // Act - Call login service with empty phone
        LoginResponse response = loggedInUsersService.login(request);

        // Assert - Login should fail
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getLoginStatus(), "Login status should be fail");
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode(),
                "Response code should be 2001 (MISSING_PARAM)");

        // Verify record is NOT saved
        assertFalse(loggedInUsersRepository.existsByPhoneNumber(""),
                "User should NOT be saved in logged_in_users table");
    }

    @Test
    @DisplayName("Testcase 7: Empty phone and empty password - Should PASS (Login fails)")
    void testLoginWithEmptyPhoneAndEmptyPassword() {
        // Arrange
        LoginRequest request = new LoginRequest("", "");

        // Act - Call login service
        LoginResponse response = loggedInUsersService.login(request);

        // Assert - Login should fail
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getLoginStatus(), "Login status should be fail");
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode(),
                "Response code should be 2001 (MISSING_PARAM)");

        // Verify record is NOT saved
        assertFalse(loggedInUsersRepository.existsByPhoneNumber(""),
                "User should NOT be saved in logged_in_users table");
    }

    @Test
    @DisplayName("Testcase 8: Null phone and null password - Should PASS (Logic fails)")
    void testLoginWithNullPhoneAndNullEmptyPassword() {
        // Arrange
        LoginRequest request = new LoginRequest(null, null);

        // Act - Call login service
        LoginResponse response = loggedInUsersService.login(request);

        // Assert - Login should fail
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getLoginStatus(), "Login status should be fail");
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode(),
                "Response code should be 2001 (MISSING_PARAM)");

        // Verify record is NOT saved
        assertFalse(loggedInUsersRepository.existsByPhoneNumber(""),
                "User should NOT be saved in logged_in_users table");
    }

    @Test
    @DisplayName("Testcase 9: Invalid phone and invalid password - Should PASS (Login fails)")
    void testLoginWithInvalidPhoneAndInvalidPassword() {
        // Arrange
        LoginRequest request = new LoginRequest("123", "12345");

        // Act - Call login service
        LoginResponse response = loggedInUsersService.login(request);

        // Assert - Login should fail
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getLoginStatus(), "Login status should be fail");

        // Verify record is NOT saved
        assertFalse(loggedInUsersRepository.existsByPhoneNumber("123"),
                "User should NOT be saved in logged_in_users table");
    }

    @Test
    @DisplayName("Testcase 10: Phone with whitespace and valid password - Should PASS (Login success)")
    void testLoginPhoneWithWhitespaceAndValidPassword() {
        // Arrange - Create a registered user in signup_not_yet_login table
        createAndSaveSignupUser(VALID_PHONE, VALID_PASSWORD);

        // Verify user is in signup table
        assertTrue(signupNotYetLoginRepository.existsByPhoneNumber(VALID_PHONE),
                "User should exist in signup_not_yet_login table");

        //Arrange
        LoginRequest request = new LoginRequest(" " + VALID_PHONE + " ", VALID_PASSWORD);

        //Act - Call login service
        LoginResponse response = loggedInUsersService.login(request);

        // Assert - Login should succeed
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getLoginStatus(), "Login status should be success");
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode(), "Response code should be 1000");
        assertEquals(ResponseCode.SUCCESS.getMessage(), response.getMessage());
        assertFalse(response.getUsedInTest());
        assertNotNull(response.getToken(), "Token should be generated");
        assertNotNull(response.getRefreshToken(), "RefreshToken should be generated");
        assertNotNull(response.getTokenExpiresAt(), "TokenExpiresAt should be set");
        assertNotNull(response.getData(), "Data should not be null");
        assertNotNull(response.getData().getId(), "User ID should be set");
        assertEquals(VALID_PHONE, response.getData().getPhoneNumber(), "Phone number should match");

        // Verify record is saved in logged_in_users table
        assertTrue(loggedInUsersRepository.existsByPhoneNumber(VALID_PHONE),
                "User should be saved in logged_in_users table");
        LoggedInUsers savedUser = loggedInUsersRepository.findLoggedInUsersByPhoneNumber(VALID_PHONE);
        assertNotNull(savedUser, "Saved user should not be null");
        assertEquals(VALID_PHONE, savedUser.getPhoneNumber());

    }

    @Test
    @DisplayName("Edge case: User already logged in (phone exists in logged_in_users) - Should PASS (Login fails)")
    //
    void testLoginWithAlreadyLoggedInUser() {
        // Arrange - Create user in logged_in_users table (simulate already logged in)
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);

        LoginRequest request = new LoginRequest(VALID_PHONE, VALID_PASSWORD);

        // Act - Call login service
        LoginResponse response = loggedInUsersService.login(request);

        // Assert - Login should fail (user already exists in logged_in_users)
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getLoginStatus(), "Login status should be fail");
        assertEquals(ResponseCode.USER_EXISTS.getCode(), response.getCode(),
                "Response code should be 3006 (USER_EXISTS)");
        assertEquals(ResponseCode.USER_EXISTS.getMessage(), response.getMessage());
        assertNull(response.getToken(), "Token should not be generated");
    }

    @Test
    @DisplayName("Testcase 1 - Duplicate: Multiple successful logins with different users")
    void testMultipleSuccessfulLoginsWithDifferentUsers() {
        // Arrange - Create two different users
        createAndSaveSignupUser(VALID_PHONE, VALID_PASSWORD);
        createAndSaveSignupUser(VALID_PHONE_2, VALID_PASSWORD);

        // Act - Login first user
        LoginRequest request1 = new LoginRequest(VALID_PHONE, VALID_PASSWORD);
        LoginResponse response1 = loggedInUsersService.login(request1);

        // Assert - First login succeeds
        assertEquals("success", response1.getLoginStatus());
        assertEquals(VALID_PHONE, response1.getData().getPhoneNumber());

        // Act - Login second user
        LoginRequest request2 = new LoginRequest(VALID_PHONE_2, VALID_PASSWORD);
        LoginResponse response2 = loggedInUsersService.login(request2);

        // Assert - Second login succeeds
        assertEquals("success", response2.getLoginStatus());
        assertEquals(VALID_PHONE_2, response2.getData().getPhoneNumber());

        // Verify both users are saved in database
        assertTrue(loggedInUsersRepository.existsByPhoneNumber(VALID_PHONE),
                "First user should be in logged_in_users");
        assertTrue(loggedInUsersRepository.existsByPhoneNumber(VALID_PHONE_2),
                "Second user should be in logged_in_users");
    }

}
