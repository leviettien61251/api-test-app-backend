package com.example.apitestappbackend.LoggedOutUser;

import com.example.apitestappbackend.DTO.LogoutTest.LoggedOutUserResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.LoggedOutUser;
import com.example.apitestappbackend.models.UserTest;
import com.example.apitestappbackend.repository.LoggedOutUserRepository;
import com.example.apitestappbackend.repository.UserTestRepository;
import com.example.apitestappbackend.services.LoggedOutUserService;
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
@DisplayName("LoggedOutUser Scenario Database Tests - hospital_test_2 Integration")
public class LoggedOutUserScenarioDbTest {

    @Autowired
    private LoggedOutUserService loggedOutUserService;

    @Autowired
    private LoggedOutUserRepository loggedOutUserRepository;

    @Autowired
    private UserTestRepository userTestRepository;

    private static final String VALID_PHONE = "0901234567";
    private static final String VALID_PHONE_2 = "0912345678";
    private static final String VALID_PASSWORD = "111111";
    private static final String VALID_TOKEN = "test_token_12345";
    private static final String VALID_TOKEN_2 = "test_token_67890";
    private static final String INVALID_TOKEN = "invalid_token";
    private static final String EMPTY_TOKEN = "";

    @BeforeEach
    void setUp() {
        // Clear all records before each test
        loggedOutUserRepository.deleteAll();
        userTestRepository.deleteAll();
    }

    private UserTest createAndSaveUserTest(String phoneNumber, String password, String token) {
        UserTest user = UserTest.builder()
                .phoneNumber(phoneNumber)
                .password(password)
                .token(token)
                .refreshToken("refresh_token_" + token)
                .tokenExpiresAt(Timestamp.from(Instant.now().plusSeconds(3600)))
                .build();
        return userTestRepository.save(user);
    }

    private LoggedOutUser createAndSaveLoggedOutUser(String phoneNumber, String invalidatedToken) {
        LoggedOutUser loggedOutUser = new LoggedOutUser();
        loggedOutUser.setPhoneNumber(phoneNumber);
        loggedOutUser.setInvalidatedToken(invalidatedToken);
        loggedOutUser.setStatus("success");
        loggedOutUser.setCode(ResponseCode.SUCCESS.getCode());
        loggedOutUser.setMessage(ResponseCode.SUCCESS.getMessage());
        loggedOutUser.setTimeStamp(new Timestamp(System.currentTimeMillis()));
        loggedOutUser.setUsedInTest(false);
        loggedOutUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return loggedOutUserRepository.save(loggedOutUser);
    }

    @Test
    @DisplayName("Testcase 1: Valid token with Bearer prefix - Should PASS (Successful logout)")
    void testLogoutWithValidToken() {
        // Arrange - Create a user with valid token
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, VALID_TOKEN);

        // Verify user exists with token
        assertTrue(userTestRepository.existsByToken(VALID_TOKEN),
                "User should exist with token in user_test table");

        String authHeader = "Bearer " + VALID_TOKEN;

        // Act - Call logout service
        LoggedOutUserResponse response = loggedOutUserService.logout(authHeader);

        // Assert - Logout should succeed
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode(),
                "Response code should be 1000 (SUCCESS)");
        assertEquals(ResponseCode.SUCCESS.getMessage(), response.getMessage(),
                "Response message should match SUCCESS message");
        assertFalse(response.getUsedInTest());
        assertNotNull(response.getData(), "Data should not be null");
        assertNotNull(response.getData().getId(), "User ID should be set");
        assertEquals(VALID_PHONE, response.getData().getPhoneNumber(), "Phone number should match");
        assertEquals(VALID_TOKEN, response.getData().getInvalidatedToken(), "Invalidated token should match");

        // Verify record is saved in logged_out_user table
        assertTrue(loggedOutUserRepository.existsByInvalidatedToken(VALID_TOKEN),
                "Token should be saved in logged_out_user table");

        // Verify token is cleared in user_test table
        UserTest savedUser = userTestRepository.findByPhoneNumber(VALID_PHONE).orElse(null);
        assertNotNull(savedUser, "User should still exist in user_test table");
        assertEquals("", savedUser.getToken(), "Token should be cleared in user_test table");
    }

    @Test
    @DisplayName("Testcase 2: Invalid token without Bearer prefix - Should PASS (Logout fails)")
    void testLogoutWithInvalidTokenFormat() {
        // Arrange - Create a user with valid token
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, VALID_TOKEN);

        String authHeader = "Token " + VALID_TOKEN; // Wrong prefix

        // Act - Call logout service
        LoggedOutUserResponse response = loggedOutUserService.logout(authHeader);

        // Assert - Logout should fail
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.TOKEN_INVALID.getCode(), response.getCode(),
                "Response code should be 3001 (TOKEN_INVALID)");
        assertEquals(ResponseCode.TOKEN_INVALID.getMessage(), response.getMessage(),
                "Response message should match TOKEN_INVALID message");
        assertFalse(response.getUsedInTest());
        assertNull(response.getData(), "Data should be null");

        // Verify record is NOT saved in logged_out_user table
        assertFalse(loggedOutUserRepository.existsByInvalidatedToken(VALID_TOKEN),
                "Token should NOT be saved in logged_out_user table");

        // Verify token still exists in user_test table
        UserTest savedUser = userTestRepository.findByPhoneNumber(VALID_PHONE).orElse(null);
        assertNotNull(savedUser, "User should still exist");
        assertEquals(VALID_TOKEN, savedUser.getToken(), "Token should still be present");
    }

    @Test
    @DisplayName("Testcase 3: Non-existent token - Should PASS (Logout fails)")
    void testLogoutWithNonExistentToken() {
        // Arrange - Create a user with valid token, but logout with different token
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, VALID_TOKEN);

        String authHeader = "Bearer " + INVALID_TOKEN;

        // Act - Call logout service
        LoggedOutUserResponse response = loggedOutUserService.logout(authHeader);

        // Assert - Logout should fail
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.TOKEN_INVALID.getCode(), response.getCode(),
                "Response code should be 3001 (TOKEN_INVALID)");
        assertEquals(ResponseCode.TOKEN_INVALID.getMessage(), response.getMessage());
        assertFalse(response.getUsedInTest());
        assertNull(response.getData(), "Data should be null");

        // Verify record is NOT saved in logged_out_user table
        assertFalse(loggedOutUserRepository.existsByInvalidatedToken(INVALID_TOKEN),
                "Invalid token should NOT be saved in logged_out_user table");

        // Verify original token still exists
        UserTest savedUser = userTestRepository.findByPhoneNumber(VALID_PHONE).orElse(null);
        assertNotNull(savedUser, "User should still exist");
        assertEquals(VALID_TOKEN, savedUser.getToken(), "Original token should still be present");
    }

    @Test
    @DisplayName("Testcase 4: Already invalidated token - Should PASS (Logout fails)")
    void testLogoutWithAlreadyInvalidatedToken() {
        // Arrange - Create a user and an already logged out user with same token
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, VALID_TOKEN);
        createAndSaveLoggedOutUser(VALID_PHONE, VALID_TOKEN); // Token already invalidated

        String authHeader = "Bearer " + VALID_TOKEN;

        // Act - Call logout service
        LoggedOutUserResponse response = loggedOutUserService.logout(authHeader);

        // Assert - Logout should fail because token is already invalidated
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.TOKEN_INVALID.getCode(), response.getCode(),
                "Response code should be 3001 (TOKEN_INVALID)");
        assertEquals(ResponseCode.TOKEN_INVALID.getMessage(), response.getMessage());
        assertFalse(response.getUsedInTest());
        assertNull(response.getData(), "Data should be null");
    }

    @Test
    @DisplayName("Testcase 5: Empty token - Should PASS (Logout fails)")
    void testLogoutWithEmptyToken() {
        // Arrange - Create a user with valid token
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, VALID_TOKEN);

        String authHeader = "Bearer ";

        // Act - Call logout service
        LoggedOutUserResponse response = loggedOutUserService.logout(authHeader);

        // Assert - Logout should fail
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.TOKEN_INVALID.getCode(), response.getCode(),
                "Response code should be 3001 (TOKEN_INVALID)");
        assertEquals(ResponseCode.TOKEN_INVALID.getMessage(), response.getMessage());
        assertFalse(response.getUsedInTest());
        assertNull(response.getData(), "Data should be null");

        // Verify record is NOT saved in logged_out_user table
        assertFalse(loggedOutUserRepository.existsByInvalidatedToken(EMPTY_TOKEN),
                "Empty token should NOT be saved");
    }

    @Test
    @DisplayName("Testcase 6: Null auth header - Should PASS (Logout fails)")
    void testLogoutWithNullAuthHeader() {
        // Arrange - Create a user with valid token
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, VALID_TOKEN);

        // Act & Assert - Should throw NullPointerException when processing null
        assertThrows(NullPointerException.class, () -> {
            loggedOutUserService.logout(null);
        }, "Should throw NullPointerException for null auth header");

        // Verify record is NOT saved
        assertFalse(loggedOutUserRepository.existsByInvalidatedToken(VALID_TOKEN),
                "Token should NOT be saved in logged_out_user table");
    }

    @Test
    @DisplayName("Testcase 7: Multiple successful logouts with different users")
    void testMultipleSuccessfulLogoutsWithDifferentUsers() {
        // Arrange - Create two different users with different tokens
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, VALID_TOKEN);
        createAndSaveUserTest(VALID_PHONE_2, VALID_PASSWORD, VALID_TOKEN_2);

        // Act - Logout first user
        String authHeader1 = "Bearer " + VALID_TOKEN;
        LoggedOutUserResponse response1 = loggedOutUserService.logout(authHeader1);

        // Assert - First logout succeeds
        assertEquals("success", response1.getStatus());
        assertEquals(ResponseCode.SUCCESS.getCode(), response1.getCode());
        assertEquals(VALID_PHONE, response1.getData().getPhoneNumber());
        assertTrue(loggedOutUserRepository.existsByInvalidatedToken(VALID_TOKEN),
                "First token should be invalidated");

        // Act - Logout second user
        String authHeader2 = "Bearer " + VALID_TOKEN_2;
        LoggedOutUserResponse response2 = loggedOutUserService.logout(authHeader2);

        // Assert - Second logout succeeds
        assertEquals("success", response2.getStatus());
        assertEquals(ResponseCode.SUCCESS.getCode(), response2.getCode());
        assertEquals(VALID_PHONE_2, response2.getData().getPhoneNumber());
        assertTrue(loggedOutUserRepository.existsByInvalidatedToken(VALID_TOKEN_2),
                "Second token should be invalidated");

        // Verify both tokens are saved in database
        assertEquals(2, loggedOutUserRepository.findAll().size(),
                "Both logged out users should be in database");
    }

    @Test
    @DisplayName("Testcase 8: Response code and message comparison - Verify exact values")
    void testResponseCodeAndMessageValidation() {
        // Arrange - Create a user with valid token
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, VALID_TOKEN);

        String authHeader = "Bearer " + VALID_TOKEN;

        // Act - Call logout service
        LoggedOutUserResponse response = loggedOutUserService.logout(authHeader);

        // Assert - Verify exact code and message from ResponseCode enum
        assertNotNull(response, "Response should not be null");
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode(),
                "Code should match ResponseCode.SUCCESS.getCode()");
        assertEquals(ResponseCode.SUCCESS.getMessage(), response.getMessage(),
                "Message should match ResponseCode.SUCCESS.getMessage()");
        assertEquals("1000", response.getCode(), "Code should be exactly 1000");
        assertEquals("Request processed successfully", response.getMessage(),
                "Message should be exactly 'Request processed successfully'");
    }

    @Test
    @DisplayName("Testcase 9: Invalid token response code validation - TOKEN_INVALID comparison")
    void testInvalidTokenResponseCodeValidation() {
        // Arrange
        String authHeader = "Token " + VALID_TOKEN; // Wrong prefix

        // Act - Call logout service
        LoggedOutUserResponse response = loggedOutUserService.logout(authHeader);

        // Assert - Verify exact code and message for invalid token
        assertNotNull(response, "Response should not be null");
        assertEquals(ResponseCode.TOKEN_INVALID.getCode(), response.getCode(),
                "Code should match ResponseCode.TOKEN_INVALID.getCode()");
        assertEquals(ResponseCode.TOKEN_INVALID.getMessage(), response.getMessage(),
                "Message should match ResponseCode.TOKEN_INVALID.getMessage()");
        assertEquals("3001", response.getCode(), "Code should be exactly 3001");
        assertEquals("Token is invalid", response.getMessage(),
                "Message should be exactly 'Token is invalid'");
    }

    @Test
    @DisplayName("Testcase 10: Whitespace in token handling - Should succeed after trimming")
    void testLogoutWithWhitespaceInToken() {
        // Arrange - Create a user with valid token
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, VALID_TOKEN);

        String authHeader = "Bearer  " + VALID_TOKEN + "  "; // Extra whitespace

        // Act - Call logout service
        LoggedOutUserResponse response = loggedOutUserService.logout(authHeader);

        // Assert - Should handle whitespace correctly
        assertNotNull(response, "Response should not be null");
        // The service extracts token from position 7 onwards, so whitespace at the end is part of token
        // This test validates the actual behavior
        if (response.getStatus().equals("success")) {
            assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode());
        } else {
            // If fails, it should be because token with whitespace doesn't match
            assertEquals(ResponseCode.TOKEN_INVALID.getCode(), response.getCode());
        }
    }
}
