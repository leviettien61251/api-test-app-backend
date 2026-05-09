package com.example.apitestappbackend.GetUserInfo;

import com.example.apitestappbackend.DTO.GetUserInfo.GetUserInfoRequest;
import com.example.apitestappbackend.DTO.GetUserInfo.GetUserInfoResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.GetUserInfo;
import com.example.apitestappbackend.models.UserTest;
import com.example.apitestappbackend.repository.GetUserInfoRepository;
import com.example.apitestappbackend.repository.UserTestRepository;
import com.example.apitestappbackend.services.GetUserInfoService;
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
@DisplayName("GetUserInfo Scenario Database Tests - PostgreSQL Integration")
public class GetUserInfoScenarioDbTest {

    @Autowired
    private GetUserInfoRepository getUserInfoRepository;

    @Autowired
    private UserTestRepository userTestRepository;

    @Autowired
    private GetUserInfoService getUserInfoService;

    private static final String VALID_PHONE = "0901234567";
    private static final String VALID_PHONE_2 = "0912345678";
    private static final String INVALID_PHONE = "123";
    private static final String VALID_PASSWORD = "password123";

    @BeforeEach
    void setUp() {
        // Clear all records before each test
        getUserInfoRepository.deleteAll();
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

    @Test
    @DisplayName("Testcase 1: Valid phone, existing user - Should SUCCESS (GetUserInfo success)")
    void testGetUserInfoWithValidPhoneAndExistingUser() {
        // Arrange - Create a user in user_test table
        UserTest savedUser = createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "John Doe", "123 Main St", "avatar.png");

        GetUserInfoRequest request = new GetUserInfoRequest(VALID_PHONE);

        // Act - Call GetUserInfo service
        GetUserInfoResponse response = getUserInfoService.getUserInfo(request);

        // Assert - GetUserInfo should succeed
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode(), "Response code should be 1000");
        assertEquals(ResponseCode.SUCCESS.getMessage(), response.getMessage(), "Message should match SUCCESS message");
        assertFalse(response.getUsedInTest());
        assertNotNull(response.getData(), "Data should not be null");
        assertNotNull(response.getData().getId(), "User ID should be set");
        assertEquals(VALID_PHONE, response.getData().getPhoneNumber(), "Phone number should match");
        assertEquals("John Doe", response.getData().getFullName(), "Full name should match");
        assertEquals("123 Main St", response.getData().getAddress(), "Address should match");
        assertEquals("avatar.png", response.getData().getAvatar(), "Avatar should match");

        // Verify record is saved in get_user_info table
        assertTrue(getUserInfoRepository.findAll().size() > 0,
                "Record should be saved in get_user_info table");
        GetUserInfo savedGetUserInfo = getUserInfoRepository.findAll().get(0);
        assertNotNull(savedGetUserInfo);
        assertEquals(VALID_PHONE, savedGetUserInfo.getPhoneNumber());
        assertEquals("success", savedGetUserInfo.getStatus());
        assertEquals(ResponseCode.SUCCESS.getCode(), savedGetUserInfo.getCode());
    }

    @Test
    @DisplayName("Testcase 2: Valid phone, non-existent user - Should FAIL (USER_NOT_FOUND)")
    void testGetUserInfoWithValidPhoneButNonExistentUser() {
        // Arrange - Do NOT create any user
        GetUserInfoRequest request = new GetUserInfoRequest(VALID_PHONE);

        // Act - Call GetUserInfo service
        GetUserInfoResponse response = getUserInfoService.getUserInfo(request);

        // Assert - GetUserInfo should fail with USER_NOT_FOUND
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.USER_NOT_FOUND.getCode(), response.getCode(),
                "Response code should be 3007 (USER_NOT_FOUND)");
        assertEquals(ResponseCode.USER_NOT_FOUND.getMessage(), response.getMessage(),
                "Message should match USER_NOT_FOUND message");
        assertFalse(response.getUsedInTest());
        assertNull(response.getData(), "Data should be null");

        // Verify record is NOT saved in get_user_info table
        assertEquals(0, getUserInfoRepository.findAll().size(),
                "No record should be saved in get_user_info table on failed query");
    }

    @Test
    @DisplayName("Testcase 3: Empty phone number - Should FAIL (MISSING_PARAM)")
    void testGetUserInfoWithEmptyPhoneNumber() {
        // Arrange - Create a user for comparison
        createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "John Doe", "123 Main St", "avatar.png");

        GetUserInfoRequest request = new GetUserInfoRequest("");

        // Act - Call GetUserInfo service with empty phone
        GetUserInfoResponse response = getUserInfoService.getUserInfo(request);

        // Assert - GetUserInfo should fail with MISSING_PARAM
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode(),
                "Response code should be 2001 (MISSING_PARAM)");
        assertEquals(ResponseCode.MISSING_PARAM.getMessage(), response.getMessage(),
                "Message should match MISSING_PARAM message");
        assertFalse(response.getUsedInTest());

        // Verify record is NOT saved
        assertEquals(0, getUserInfoRepository.findAll().size(),
                "No record should be saved in get_user_info table");
    }

    @Test
    @DisplayName("Testcase 4: Invalid phone format - Should FAIL (INVALID_VALUE)")
    void testGetUserInfoWithInvalidPhoneFormat() {
        // Arrange - Create a user with valid phone for comparison
        createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "John Doe", "123 Main St", "avatar.png");

        GetUserInfoRequest request = new GetUserInfoRequest(INVALID_PHONE);

        // Act - Call GetUserInfo service with invalid phone
        GetUserInfoResponse response = getUserInfoService.getUserInfo(request);

        // Assert - GetUserInfo should fail due to invalid phone format
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.INVALID_VALUE.getCode(), response.getCode(),
                "Response code should be 2003 (INVALID_VALUE)");
        assertEquals(ResponseCode.INVALID_VALUE.getMessage(), response.getMessage(),
                "Message should match INVALID_VALUE message");
        assertFalse(response.getUsedInTest());

        // Verify record is NOT saved
        assertEquals(0, getUserInfoRepository.findAll().size(),
                "No record should be saved in get_user_info table");
    }

    @Test
    @DisplayName("Testcase 5: Phone with whitespace, existing user - Should SUCCESS (trim and process)")
    void testGetUserInfoWithPhoneWhitespaceAndExistingUser() {
        // Arrange - Create a user
        UserTest savedUser = createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "Jane Doe", "456 Oak Ave", "avatar2.png");

        GetUserInfoRequest request = new GetUserInfoRequest(" " + VALID_PHONE + " ");

        // Act - Call GetUserInfo service
        GetUserInfoResponse response = getUserInfoService.getUserInfo(request);

        // Assert - GetUserInfo should succeed (phone should be trimmed)
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode(),
                "Response code should be 1000 (SUCCESS)");
        assertEquals(ResponseCode.SUCCESS.getMessage(), response.getMessage(),
                "Message should match SUCCESS message");
        assertFalse(response.getUsedInTest());
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(VALID_PHONE, response.getData().getPhoneNumber(), "Phone number should be trimmed");
        assertEquals("Jane Doe", response.getData().getFullName());

        // Verify record is saved
        assertEquals(1, getUserInfoRepository.findAll().size(),
                "Record should be saved in get_user_info table");
    }

    @Test
    @DisplayName("Testcase 6: Null phone number - Should FAIL (MISSING_PARAM)")
    void testGetUserInfoWithNullPhoneNumber() {
        // Arrange
        GetUserInfoRequest request = new GetUserInfoRequest(null);

        // Act - Call GetUserInfo service
        GetUserInfoResponse response = getUserInfoService.getUserInfo(request);

        // Assert - GetUserInfo should fail with MISSING_PARAM or similar
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");

        // Verify record is NOT saved
        assertEquals(0, getUserInfoRepository.findAll().size(),
                "No record should be saved in get_user_info table");
    }

    @Test
    @DisplayName("Testcase 7: Multiple GetUserInfo queries for different users")
    void testMultipleGetUserInfoQueriesForDifferentUsers() {
        // Arrange - Create two different users
        UserTest user1 = createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "User One", "Address 1", "avatar1.png");
        UserTest user2 = createAndSaveUser(VALID_PHONE_2, VALID_PASSWORD, "User Two", "Address 2", "avatar2.png");

        // Act - Get info for first user
        GetUserInfoRequest request1 = new GetUserInfoRequest(VALID_PHONE);
        GetUserInfoResponse response1 = getUserInfoService.getUserInfo(request1);

        // Assert - First query succeeds
        assertEquals("success", response1.getStatus());
        assertEquals(ResponseCode.SUCCESS.getCode(), response1.getCode());
        assertEquals(VALID_PHONE, response1.getData().getPhoneNumber());
        assertEquals("User One", response1.getData().getFullName());

        // Act - Get info for second user
        GetUserInfoRequest request2 = new GetUserInfoRequest(VALID_PHONE_2);
        GetUserInfoResponse response2 = getUserInfoService.getUserInfo(request2);

        // Assert - Second query succeeds
        assertEquals("success", response2.getStatus());
        assertEquals(ResponseCode.SUCCESS.getCode(), response2.getCode());
        assertEquals(VALID_PHONE_2, response2.getData().getPhoneNumber());
        assertEquals("User Two", response2.getData().getFullName());

        // Verify both records are saved in database
        assertEquals(2, getUserInfoRepository.findAll().size(),
                "Two records should be saved in get_user_info table");
    }

    @Test
    @DisplayName("Testcase 8: Invalid phone formats variety - Should FAIL (INVALID_VALUE)")
    void testGetUserInfoWithVariousInvalidPhoneFormats() {
        String[] invalidPhones = {
                "123",           // Too short
                "ABC1234567",    // Contains letters
                "09012345",      // Too short
                "09012345678901" // Too long
        };

        for (String invalidPhone : invalidPhones) {
            GetUserInfoRequest request = new GetUserInfoRequest(invalidPhone);
            GetUserInfoResponse response = getUserInfoService.getUserInfo(request);

            assertNotNull(response, "Response should not be null for phone: " + invalidPhone);
            assertEquals("fail", response.getStatus(), "Status should be fail for phone: " + invalidPhone);
            assertEquals(ResponseCode.INVALID_VALUE.getCode(), response.getCode(),
                    "Code should be 2003 for phone: " + invalidPhone);
        }
    }

    @Test
    @DisplayName("Testcase 9: User info contains correct code and message validation")
    void testGetUserInfoCodeAndMessageValidation() {
        // Arrange - Create a user
        UserTest savedUser = createAndSaveUser(VALID_PHONE, VALID_PASSWORD, "Test User", "Test Address", "test.png");

        GetUserInfoRequest request = new GetUserInfoRequest(VALID_PHONE);

        // Act
        GetUserInfoResponse response = getUserInfoService.getUserInfo(request);

        // Assert - Validate code and message structure
        assertNotNull(response.getCode(), "Code should not be null");
        assertNotNull(response.getMessage(), "Message should not be null");
        assertTrue(response.getCode().matches("\\d+"), "Code should be numeric");
        assertTrue(!response.getMessage().isEmpty(), "Message should not be empty");

        // Verify code and message match SUCCESS
        assertEquals("1000", response.getCode(), "Code should be 1000");
        assertEquals("Request processed successfully", response.getMessage(), "Message should be correct");

        // Verify in database
        GetUserInfo savedGetUserInfo = getUserInfoRepository.findAll().get(0);
        assertEquals(response.getCode(), savedGetUserInfo.getCode(), "Saved code should match response code");
        assertEquals(response.getMessage(), savedGetUserInfo.getMessage(), "Saved message should match response message");
    }

    @Test
    @DisplayName("Testcase 10: Error handling - code and message for USER_NOT_FOUND scenario")
    void testGetUserInfoErrorCodeAndMessageValidation() {
        // Arrange
        GetUserInfoRequest request = new GetUserInfoRequest(VALID_PHONE);

        // Act
        GetUserInfoResponse response = getUserInfoService.getUserInfo(request);

        // Assert - Validate error code and message
        assertEquals("fail", response.getStatus());
        assertEquals(ResponseCode.USER_NOT_FOUND.getCode(), response.getCode(),
                "Error code should match USER_NOT_FOUND");
        assertEquals(ResponseCode.USER_NOT_FOUND.getMessage(), response.getMessage(),
                "Error message should match USER_NOT_FOUND");
        assertEquals("3007", response.getCode(), "Code should be 3007");
        assertEquals("User not found", response.getMessage(), "Message should be 'User not found'");
    }
}
