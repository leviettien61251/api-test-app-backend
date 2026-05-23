package com.example.apitestappbackend.SetAvatar;

import com.example.apitestappbackend.DTO.SetAvatar.SetAvatarRequest;
import com.example.apitestappbackend.DTO.SetAvatar.SetAvatarResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.LoggedInUsers;
import com.example.apitestappbackend.models.SetAvatar;
import com.example.apitestappbackend.models.hospitaldb.UserTest;
import com.example.apitestappbackend.repository.LoggedInUsersRepository;
import com.example.apitestappbackend.repository.SetAvatarRepository;
import com.example.apitestappbackend.repository.UserTestRepository;
import com.example.apitestappbackend.services.SetAvatarService;
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
@DisplayName("SetAvatar Scenario Database Tests - PostgreSQL Integration (that_hospital_test_2)")
public class SetAvatarScenarioDbTest {

    @Autowired
    private SetAvatarRepository setAvatarRepository;

    @Autowired
    private LoggedInUsersRepository loggedInUsersRepository;

    @Autowired
    private UserTestRepository userTestRepository;

    @Autowired
    private SetAvatarService setAvatarService;

    private static final String VALID_PHONE = "0981234567";
    private static final String VALID_PHONE_2 = "0982345678";
    private static final String INVALID_PHONE = "123";
    private static final String VALID_AVATAR_URL = "https://example.com/avatar.png";
    private static final String VALID_AVATAR_URL_2 = "https://example.com/avatar2.png";
    private static final String VALID_PASSWORD = "111111";

    @BeforeEach
    void setUp() {
        setAvatarRepository.deleteAll();
        loggedInUsersRepository.deleteAll();
        userTestRepository.deleteAll();
    }

    private UserTest createAndSaveUser(String phoneNumber, String password, String fullName, String address, String avatar) {
        UserTest user = new UserTest();
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        user.setFullname(fullName);
        user.setAddress(address);
        user.setAvatar(avatar);
        user.setToken("token_value");
        user.setRefreshToken("refresh_token_value");
        user.setTokenExpiresAt(Timestamp.from(Instant.now().plusSeconds(3600)));
        return userTestRepository.save(user);
    }

    private LoggedInUsers createAndSaveLoggedInUser(String phoneNumber, String password, String token) {
        LoggedInUsers loggedInUser = new LoggedInUsers();
        loggedInUser.setPhoneNumber(phoneNumber);
        loggedInUser.setPassword(password);
        loggedInUser.setToken(token);
        loggedInUser.setRefreshToken("refresh_token_" + phoneNumber);
        loggedInUser.setTokenExpiresAt(Timestamp.from(Instant.now().plusSeconds(3600)));
        loggedInUser.setLoginStatus("success");
        loggedInUser.setCode(ResponseCode.SUCCESS.getCode());
        loggedInUser.setMessage(ResponseCode.SUCCESS.getMessage());
        loggedInUser.setUsedInTest(false);
        return loggedInUsersRepository.save(loggedInUser);
    }

    @Test
    @DisplayName("Testcase 1: Valid phone and logged-in user - Should SUCCESS")
    void testSetAvatarWithValidPhoneAndLoggedInUser() {
        // Arrange - Create user in user_test table
        UserTest savedUser = createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "John Doe", "123 Main St", "old_avatar.png");

        // Create logged-in user record
        LoggedInUsers loggedInUser = createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD, "token_" + VALID_PHONE);

        SetAvatarRequest request = new SetAvatarRequest(VALID_AVATAR_URL, VALID_PHONE);

        // Act - Call SetAvatar service
        SetAvatarResponse response = setAvatarService.setAvatar(request);

        // Assert - SetAvatar should succeed
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode(), "Response code should be 1000");
        assertEquals(ResponseCode.SUCCESS.getMessage(), response.getMessage(), "Message should match SUCCESS message");
        assertFalse(response.getUsedInTest());
        assertNotNull(response.getData(), "Data should not be null");
        assertNotNull(response.getData().getId(), "Avatar ID should be set");
        assertEquals(VALID_PHONE, response.getData().getPhoneNumber(), "Phone number should match");
        assertEquals(VALID_AVATAR_URL, response.getData().getAvatarUrl(), "Avatar URL should match");

        // Verify record is saved in set_avatar table
        assertTrue(setAvatarRepository.findAll().size() > 0, "Record should be saved in set_avatar table");
        SetAvatar savedSetAvatar = setAvatarRepository.findAll().get(0);
        assertNotNull(savedSetAvatar);
        assertEquals(VALID_PHONE, savedSetAvatar.getPhoneNumber());
        assertEquals("success", savedSetAvatar.getStatus());
        assertEquals(ResponseCode.SUCCESS.getCode(), savedSetAvatar.getCode());
        assertEquals(ResponseCode.SUCCESS.getMessage(), savedSetAvatar.getMessage());
    }

    @Test
    @DisplayName("Testcase 2: Valid phone but user NOT logged-in - Should FAIL (USER_NOT_LOGGED_IN)")
    void testSetAvatarWithValidPhoneButUserNotLoggedIn() {
        // Arrange - Create user in user_test but NO logged-in user record
        UserTest savedUser = createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "John Doe", "123 Main St", "old_avatar.png");

        SetAvatarRequest request = new SetAvatarRequest(VALID_AVATAR_URL, VALID_PHONE);

        // Act - Call SetAvatar service
        SetAvatarResponse response = setAvatarService.setAvatar(request);

        // Assert - SetAvatar should fail with USER_NOT_LOGGED_IN
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.USER_NOT_LOGGED_IN.getCode(), response.getCode(),
                "Response code should be 3009 (USER_NOT_LOGGED_IN)");
        assertEquals(ResponseCode.USER_NOT_LOGGED_IN.getMessage(), response.getMessage(),
                "Message should match USER_NOT_LOGGED_IN message");
        assertFalse(response.getUsedInTest());
        assertNull(response.getData(), "Data should be null on failure");

        // Verify record is NOT saved in set_avatar table
        assertEquals(0, setAvatarRepository.findAll().size(),
                "No record should be saved in set_avatar table when user not logged in");
    }

    @Test
    @DisplayName("Testcase 3: Empty phone number - Should FAIL (MISSING_PARAM)")
    void testSetAvatarWithEmptyPhoneNumber() {
        // Arrange - Create a logged-in user for comparison
        createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "John Doe", "123 Main St", "old_avatar.png");
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD, "token_" + VALID_PHONE);

        SetAvatarRequest request = new SetAvatarRequest(VALID_AVATAR_URL, "");

        // Act - Call SetAvatar service with empty phone
        SetAvatarResponse response = setAvatarService.setAvatar(request);

        // Assert - SetAvatar should fail with MISSING_PARAM
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode(),
                "Response code should be 2001 (MISSING_PARAM)");
        assertEquals(ResponseCode.MISSING_PARAM.getMessage(), response.getMessage(),
                "Message should match MISSING_PARAM message");
        assertFalse(response.getUsedInTest());

        // Verify record is NOT saved
        assertEquals(0, setAvatarRepository.findAll().size(),
                "No record should be saved in set_avatar table");
    }

    @Test
    @DisplayName("Testcase 4: Invalid phone format - Should FAIL (INVALID_VALUE)")
    void testSetAvatarWithInvalidPhoneFormat() {
        // Arrange - Create a logged-in user with valid phone for comparison
        createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "John Doe", "123 Main St", "old_avatar.png");
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD, "token_" + VALID_PHONE);

        SetAvatarRequest request = new SetAvatarRequest(VALID_AVATAR_URL, INVALID_PHONE);

        // Act - Call SetAvatar service with invalid phone
        SetAvatarResponse response = setAvatarService.setAvatar(request);

        // Assert - SetAvatar should fail due to invalid phone format
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.INVALID_VALUE.getCode(), response.getCode(),
                "Response code should be 2003 (INVALID_VALUE)");
        assertEquals(ResponseCode.INVALID_VALUE.getMessage(), response.getMessage(),
                "Message should match INVALID_VALUE message");
        assertFalse(response.getUsedInTest());

        // Verify record is NOT saved
        assertEquals(0, setAvatarRepository.findAll().size(),
                "No record should be saved in set_avatar table");
    }

    @Test
    @DisplayName("Testcase 5: Phone with whitespace and logged-in user - Should SUCCESS (trim and process)")
    void testSetAvatarWithPhoneWhitespaceAndLoggedInUser() {
        // Arrange - Create a logged-in user
        UserTest savedUser = createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "Jane Doe", "456 Oak Ave", "old_avatar2.png");
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD, "token_" + VALID_PHONE);

        SetAvatarRequest request = new SetAvatarRequest(VALID_AVATAR_URL_2, " " + VALID_PHONE + " ");

        // Act - Call SetAvatar service
        SetAvatarResponse response = setAvatarService.setAvatar(request);

        // Assert - SetAvatar should succeed (phone should be trimmed)
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode(),
                "Response code should be 1000 (SUCCESS)");
        assertEquals(ResponseCode.SUCCESS.getMessage(), response.getMessage(),
                "Message should match SUCCESS message");
        assertFalse(response.getUsedInTest());
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(VALID_PHONE, response.getData().getPhoneNumber(), "Phone number should be trimmed");
        assertEquals(VALID_AVATAR_URL_2, response.getData().getAvatarUrl());

        // Verify record is saved
        assertEquals(1, setAvatarRepository.findAll().size(),
                "Record should be saved in set_avatar table");
    }

    @Test
    @DisplayName("Testcase 6: Null phone number - Should FAIL (MISSING_PARAM)")
    void testSetAvatarWithNullPhoneNumber() {
        // Arrange
        SetAvatarRequest request = new SetAvatarRequest(VALID_AVATAR_URL, null);

        // Act - Call SetAvatar service
        SetAvatarResponse response = setAvatarService.setAvatar(request);

        // Assert - SetAvatar should fail
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");

        // Verify record is NOT saved
        assertEquals(0, setAvatarRepository.findAll().size(),
                "No record should be saved in set_avatar table");
    }

    @Test
    @DisplayName("Testcase 6.1: Missing avatar URL - Should FAIL (MISSING_PARAM)")
    void testSetAvatarWithMissingAvatarUrl() {
        SetAvatarRequest request = new SetAvatarRequest(" ", VALID_PHONE);

        SetAvatarResponse response = setAvatarService.setAvatar(request);

        assertNotNull(response);
        assertEquals("fail", response.getStatus());
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode());
        assertEquals(0, setAvatarRepository.findAll().size());
    }

    @Test
    @DisplayName("Testcase 6.1.1: Null avatar URL - Should FAIL (MISSING_PARAM)")
    void testSetAvatarWithNullAvatarUrl() {
        SetAvatarRequest request = new SetAvatarRequest(null, VALID_PHONE);

        SetAvatarResponse response = setAvatarService.setAvatar(request);

        assertNotNull(response);
        assertEquals("fail", response.getStatus());
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode());
        assertEquals(0, setAvatarRepository.findAll().size());
    }

    @Test
    @DisplayName("Testcase 6.2: Invalid avatar URL - Should FAIL (INVALID_VALUE)")
    void testSetAvatarWithInvalidAvatarUrl() {
        SetAvatarRequest request = new SetAvatarRequest("avatar.png", VALID_PHONE);

        SetAvatarResponse response = setAvatarService.setAvatar(request);

        assertNotNull(response);
        assertEquals("fail", response.getStatus());
        assertEquals(ResponseCode.INVALID_VALUE.getCode(), response.getCode());
        assertEquals(0, setAvatarRepository.findAll().size());
    }

    @Test
    @DisplayName("Testcase 6.3: Valid phone but user does not exist - Should FAIL (USER_NOT_FOUND)")
    void testSetAvatarWithMissingUser() {
        SetAvatarRequest request = new SetAvatarRequest(VALID_AVATAR_URL, VALID_PHONE);

        SetAvatarResponse response = setAvatarService.setAvatar(request);

        assertNotNull(response);
        assertEquals("fail", response.getStatus());
        assertEquals(ResponseCode.USER_NOT_FOUND.getCode(), response.getCode());
        assertEquals(ResponseCode.USER_NOT_FOUND.getMessage(), response.getMessage());
        assertEquals(0, setAvatarRepository.findAll().size());
    }

    @Test
    @DisplayName("Testcase 7: Multiple SetAvatar calls for different logged-in users")
    void testMultipleSetAvatarCallsForDifferentLoggedInUsers() {
        // Arrange - Create two different users with logged-in records
        UserTest user1 = createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "User One", "Address 1", "avatar1.png");
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD, "token_" + VALID_PHONE);

        UserTest user2 = createAndSaveUser(VALID_PHONE_2, VALID_PASSWORD, "User Two", "Address 2", "avatar2.png");
        createAndSaveLoggedInUser(VALID_PHONE_2, VALID_PASSWORD, "token_" + VALID_PHONE_2);

        // Act - Set avatar for first user
        SetAvatarRequest request1 = new SetAvatarRequest(VALID_AVATAR_URL, VALID_PHONE);
        SetAvatarResponse response1 = setAvatarService.setAvatar(request1);

        // Assert - First avatar set succeeds
        assertEquals("success", response1.getStatus());
        assertEquals(ResponseCode.SUCCESS.getCode(), response1.getCode());
        assertEquals(VALID_PHONE, response1.getData().getPhoneNumber());
        assertEquals(VALID_AVATAR_URL, response1.getData().getAvatarUrl());

        // Act - Set avatar for second user
        SetAvatarRequest request2 = new SetAvatarRequest(VALID_AVATAR_URL_2, VALID_PHONE_2);
        SetAvatarResponse response2 = setAvatarService.setAvatar(request2);

        // Assert - Second avatar set succeeds
        assertEquals("success", response2.getStatus());
        assertEquals(ResponseCode.SUCCESS.getCode(), response2.getCode());
        assertEquals(VALID_PHONE_2, response2.getData().getPhoneNumber());
        assertEquals(VALID_AVATAR_URL_2, response2.getData().getAvatarUrl());

        // Verify both records are saved in database
        assertEquals(2, setAvatarRepository.findAll().size(),
                "Two records should be saved in set_avatar table");
    }

    @Test
    @DisplayName("Testcase 8: Various invalid phone formats - Should FAIL (INVALID_VALUE)")
    void testSetAvatarWithVariousInvalidPhoneFormats() {
        String[] invalidPhones = {
                "123",                  // Too short
                "ABC1234567",           // Contains letters
                "09012345",             // Too short
                "09012345678901",       // Too long
                "012345678",            // Invalid prefix
                "+1234567890"           // Non-Vietnamese format
        };

        for (String invalidPhone : invalidPhones) {
            SetAvatarRequest request = new SetAvatarRequest(VALID_AVATAR_URL, invalidPhone);
            SetAvatarResponse response = setAvatarService.setAvatar(request);

            assertNotNull(response, "Response should not be null for phone: " + invalidPhone);
            assertEquals("fail", response.getStatus(), "Status should be fail for phone: " + invalidPhone);
            assertEquals(ResponseCode.INVALID_VALUE.getCode(), response.getCode(),
                    "Code should be 2003 for phone: " + invalidPhone);
        }

        // Verify no records were saved
        assertEquals(0, setAvatarRepository.findAll().size(),
                "No records should be saved for any invalid phone");
    }

    @Test
    @DisplayName("Testcase 9: Success - code and message validation in response and database")
    void testSetAvatarSuccessCodeAndMessageValidation() {
        // Arrange - Create a logged-in user
        UserTest savedUser = createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "Test User", "Test Address", "test.png");
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD, "token_" + VALID_PHONE);

        SetAvatarRequest request = new SetAvatarRequest(VALID_AVATAR_URL, VALID_PHONE);

        // Act
        SetAvatarResponse response = setAvatarService.setAvatar(request);

        // Assert - Validate code and message structure
        assertNotNull(response.getCode(), "Code should not be null");
        assertNotNull(response.getMessage(), "Message should not be null");
        assertTrue(response.getCode().matches("\\d+"), "Code should be numeric");
        assertTrue(!response.getMessage().isEmpty(), "Message should not be empty");

        // Verify code and message match SUCCESS
        assertEquals("1000", response.getCode(), "Code should be 1000");
        assertEquals("Request processed successfully", response.getMessage(), "Message should be correct");

        // Verify in database
        SetAvatar savedSetAvatar = setAvatarRepository.findAll().get(0);
        assertEquals(response.getCode(), savedSetAvatar.getCode(), "Saved code should match response code");
        assertEquals(response.getMessage(), savedSetAvatar.getMessage(), "Saved message should match response message");
    }

    @Test
    @DisplayName("Testcase 10: Failure - code and message validation for USER_NOT_LOGGED_IN error")
    void testSetAvatarErrorCodeAndMessageValidation() {
        // Arrange - Create user but NO logged-in record
        UserTest savedUser = createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "Test User", "Test Address", "test.png");

        SetAvatarRequest request = new SetAvatarRequest(VALID_AVATAR_URL, VALID_PHONE);

        // Act
        SetAvatarResponse response = setAvatarService.setAvatar(request);

        // Assert - Validate error code and message
        assertEquals("fail", response.getStatus());
        assertEquals(ResponseCode.USER_NOT_LOGGED_IN.getCode(), response.getCode(),
                "Error code should match USER_NOT_LOGGED_IN");
        assertEquals(ResponseCode.USER_NOT_LOGGED_IN.getMessage(), response.getMessage(),
                "Error message should match USER_NOT_LOGGED_IN");
        assertEquals("3009", response.getCode(), "Code should be 3009");
        assertEquals("User not logged in", response.getMessage(), "Message should be 'User not logged in'");
    }

    @Test
    @DisplayName("Testcase 11: User avatar is updated in user_test table")
    void testUserAvatarIsUpdatedInUserTestTable() {
        // Arrange - Create a logged-in user with old avatar
        UserTest savedUser = createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "John Doe", "123 Main St", "old_avatar.png");
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD, "token_" + VALID_PHONE);

        SetAvatarRequest request = new SetAvatarRequest(VALID_AVATAR_URL, VALID_PHONE);

        // Act - Call SetAvatar service
        SetAvatarResponse response = setAvatarService.setAvatar(request);

        // Assert - Response should succeed
        assertEquals("success", response.getStatus());
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode());

        // Verify user avatar is updated in user_test table
        UserTest updatedUser = userTestRepository.findByPhoneNumber(VALID_PHONE).orElse(null);
        assertNotNull(updatedUser, "User should exist");
        assertEquals(VALID_AVATAR_URL, updatedUser.getAvatar(), "User avatar should be updated to new URL");
    }

    @Test
    @DisplayName("Testcase 12: Multiple SetAvatar calls for same user - overwrites previous avatar")
    void testMultipleSetAvatarCallsForSameUserOverwritesPreviousAvatar() {
        // Arrange - Create a logged-in user
        UserTest savedUser = createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "John Doe", "123 Main St", "old_avatar.png");
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD, "token_" + VALID_PHONE);

        // Act - First SetAvatar call
        SetAvatarRequest request1 = new SetAvatarRequest(VALID_AVATAR_URL, VALID_PHONE);
        SetAvatarResponse response1 = setAvatarService.setAvatar(request1);

        // Assert - First call succeeds
        assertEquals("success", response1.getStatus());
        assertEquals(ResponseCode.SUCCESS.getCode(), response1.getCode());

        // Act - Second SetAvatar call with different avatar
        SetAvatarRequest request2 = new SetAvatarRequest(VALID_AVATAR_URL_2, VALID_PHONE);
        SetAvatarResponse response2 = setAvatarService.setAvatar(request2);

        // Assert - Second call succeeds
        assertEquals("success", response2.getStatus());
        assertEquals(ResponseCode.SUCCESS.getCode(), response2.getCode());

        // Verify both records are saved but user has latest avatar
        assertEquals(2, setAvatarRepository.findAll().size(),
                "Two records should be saved in set_avatar table");
        UserTest updatedUser = userTestRepository.findByPhoneNumber(VALID_PHONE).orElse(null);
        assertEquals(VALID_AVATAR_URL_2, updatedUser.getAvatar(), "User avatar should be updated to latest URL");
    }
}
