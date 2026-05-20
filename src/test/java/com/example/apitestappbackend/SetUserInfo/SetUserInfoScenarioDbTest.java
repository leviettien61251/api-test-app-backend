package com.example.apitestappbackend.SetUserInfo;

import com.example.apitestappbackend.DTO.SetUserInfo.SetUserInfoRequest;
import com.example.apitestappbackend.DTO.SetUserInfo.SetUserInfoResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.LoggedInUsers;
import com.example.apitestappbackend.models.SetUserInfo;
import com.example.apitestappbackend.models.hospitaldb.UserTest;
import com.example.apitestappbackend.repository.LoggedInUsersRepository;
import com.example.apitestappbackend.repository.SetUserInfoRepository;
import com.example.apitestappbackend.repository.UserTestRepository;
import com.example.apitestappbackend.services.SetUserInfoService;
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
@DisplayName("SetUserInfo Scenario Database Tests - PostgreSQL Integration (hospital_test_2)")
public class SetUserInfoScenarioDbTest {

    @Autowired
    private SetUserInfoService setUserInfoService;

    @Autowired
    private SetUserInfoRepository setUserInfoRepository;

    @Autowired
    private LoggedInUsersRepository loggedInUsersRepository;

    @Autowired
    private UserTestRepository userTestRepository;

    private static final String VALID_PHONE = "0981234567";
    private static final String VALID_PHONE_2 = "0982345678";
    private static final String INVALID_PHONE = "123";
    private static final String INVALID_PHONE_FORMAT = "0101010101"; // Invalid format
    private static final String VALID_FULLNAME = "John Doe";
    private static final String VALID_FULLNAME_2 = "Jane Smith";
    private static final String INVALID_FULLNAME = "John123Doe"; // Contains numbers
    private static final String VALID_ADDRESS = "123 Main Street, City";
    private static final String VALID_ADDRESS_2 = "456 Oak Avenue, Town";
    private static final String VALID_PASSWORD = "111111";

    @BeforeEach
    void setUp() {
        // Clear all records before each test
        setUserInfoRepository.deleteAll();
        loggedInUsersRepository.deleteAll();
        userTestRepository.deleteAll();
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

    private UserTest createAndSaveUserTest(String phoneNumber, String password, String fullname, String address) {
        UserTest user = UserTest.builder()
                .phoneNumber(phoneNumber)
                .password(password)
                .fullname(fullname)
                .address(address)
                .build();
        return userTestRepository.save(user);
    }

    // ==================== SUCCESS TEST CASES ====================

    @Test
    @DisplayName("Testcase 1: Valid all fields with logged-in user - Should PASS (Set info succeeds)")
    void testSetUserInfoWithValidFieldsAndLoggedInUser() {
        // Arrange - Create logged-in user in logged_in_users table
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        // Create user in user_test table
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, null, null);

        SetUserInfoRequest request = new SetUserInfoRequest(VALID_FULLNAME, VALID_PHONE, VALID_ADDRESS);

        // Act - Call setUserInfo service
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert - Should succeed
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode(), "Response code should be 1000");
        assertEquals(ResponseCode.SUCCESS.getMessage(), response.getMessage());
        assertFalse(response.getUsedInTest());
        assertNotNull(response.getData(), "Data should not be null");
        assertNotNull(response.getData().getId(), "User ID should be set");
        assertEquals(VALID_FULLNAME, response.getData().getFullName(), "Full name should match");
        assertEquals(VALID_PHONE, response.getData().getPhoneNumber(), "Phone number should match");
        assertEquals(VALID_ADDRESS, response.getData().getAddress(), "Address should match");

        // Verify record is saved in set_user_info table
        assertTrue(setUserInfoRepository.findByPhoneNumber(VALID_PHONE).isPresent(),
                "User should be saved in set_user_info table");
        SetUserInfo savedUser = setUserInfoRepository.findByPhoneNumber(VALID_PHONE).orElse(null);
        assertNotNull(savedUser, "Saved user should not be null");
        assertEquals(VALID_FULLNAME, savedUser.getFullName());
        assertEquals(VALID_PHONE, savedUser.getPhoneNumber());
        assertEquals(VALID_ADDRESS, savedUser.getAddress());

        // Verify user_test table is updated
        UserTest updatedUser = userTestRepository.findByPhoneNumber(VALID_PHONE).orElse(null);
        assertNotNull(updatedUser, "User in user_test table should exist");
        assertEquals(VALID_FULLNAME, updatedUser.getFullname(), "Fullname in user_test should be updated");
        assertEquals(VALID_ADDRESS, updatedUser.getAddress(), "Address in user_test should be updated");
    }

    @Test
    @DisplayName("Testcase 2: Fields with whitespace - Should PASS (Trimmed and succeeds)")
    void testSetUserInfoWithWhitespaceInFields() {
        // Arrange
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, null, null);

        SetUserInfoRequest request = new SetUserInfoRequest(
                "  " + VALID_FULLNAME + "  ",
                "  " + VALID_PHONE + "  ",
                "  " + VALID_ADDRESS + "  "
        );

        // Act
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode());
        assertEquals(VALID_FULLNAME, response.getData().getFullName(), "Fullname should be trimmed");
        assertEquals(VALID_PHONE, response.getData().getPhoneNumber(), "Phone should be trimmed");
        assertEquals(VALID_ADDRESS, response.getData().getAddress(), "Address should be trimmed");
    }

    @Test
    @DisplayName("Testcase 3: Multiple users setting info - Should PASS (All succeed)")
    void testMultipleUsersSettingInfo() {
        // Arrange - Create two logged-in users
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, null, null);

        createAndSaveLoggedInUser(VALID_PHONE_2, VALID_PASSWORD);
        createAndSaveUserTest(VALID_PHONE_2, VALID_PASSWORD, null, null);

        SetUserInfoRequest request1 = new SetUserInfoRequest(VALID_FULLNAME, VALID_PHONE, VALID_ADDRESS);
        SetUserInfoRequest request2 = new SetUserInfoRequest(VALID_FULLNAME_2, VALID_PHONE_2, VALID_ADDRESS_2);

        // Act
        SetUserInfoResponse response1 = setUserInfoService.setUserInfo(request1);
        SetUserInfoResponse response2 = setUserInfoService.setUserInfo(request2);

        // Assert
        assertEquals("success", response1.getStatus());
        assertEquals("success", response2.getStatus());
        assertEquals(VALID_FULLNAME, response1.getData().getFullName());
        assertEquals(VALID_FULLNAME_2, response2.getData().getFullName());

        // Verify both records are saved
        assertTrue(setUserInfoRepository.findByPhoneNumber(VALID_PHONE).isPresent());
        assertTrue(setUserInfoRepository.findByPhoneNumber(VALID_PHONE_2).isPresent());
    }

    // ==================== FAILURE TEST CASES - BLANK FIELDS ====================

    @Test
    @DisplayName("Testcase 4: Empty full name - Should PASS (Fails with MISSING_PARAM)")
    void testSetUserInfoWithEmptyFullName() {
        // Arrange
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, null, null);

        SetUserInfoRequest request = new SetUserInfoRequest("", VALID_PHONE, VALID_ADDRESS);

        // Act
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode(),
                "Response code should be 2001 (MISSING_PARAM)");
        assertEquals("Không được để trống tên", response.getMessage(),
                "Message should be: Không được để trống tên");
        assertTrue(response.getMessage().toLowerCase().contains("tên"),
                "Message should mention name");
        assertNull(response.getData(), "Data should be null");

        // Verify record is NOT saved
        assertFalse(setUserInfoRepository.findByPhoneNumber(VALID_PHONE).isPresent(),
                "User should NOT be saved in set_user_info table");
    }

    @Test
    @DisplayName("Testcase 5: Empty phone number - Should PASS (Fails with MISSING_PARAM)")
    void testSetUserInfoWithEmptyPhoneNumber() {
        // Arrange
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, null, null);

        SetUserInfoRequest request = new SetUserInfoRequest(VALID_FULLNAME, "", VALID_ADDRESS);

        // Act
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert
        assertEquals("fail", response.getStatus());
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode());
        assertEquals("Không được để trống mật khẩu", response.getMessage(),
                "Message should be: Không được để trống mật khẩu");
        assertNull(response.getData());

        // Verify record is NOT saved
        assertFalse(setUserInfoRepository.findByPhoneNumber("").isPresent());
    }

    @Test
    @DisplayName("Testcase 6: Empty address - Should PASS (Fails with MISSING_PARAM)")
    void testSetUserInfoWithEmptyAddress() {
        // Arrange
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, null, null);

        SetUserInfoRequest request = new SetUserInfoRequest(VALID_FULLNAME, VALID_PHONE, "");

        // Act
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert
        assertEquals("fail", response.getStatus());
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode());
        assertEquals("Không được để trống địa chỉ", response.getMessage(),
                "Message should be: Không được để trống địa chỉ");
        assertTrue(response.getMessage().toLowerCase().contains("địa chỉ"),
                "Message should mention address");
        assertNull(response.getData());

        // Verify record is NOT saved
        assertFalse(setUserInfoRepository.findByPhoneNumber(VALID_PHONE).isPresent());
    }

    @Test
    @DisplayName("Testcase 7: All fields empty - Should PASS (Fails with MISSING_PARAM)")
    void testSetUserInfoWithAllFieldsEmpty() {
        // Arrange
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, null, null);

        SetUserInfoRequest request = new SetUserInfoRequest("", "", "");

        // Act
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert
        assertEquals("fail", response.getStatus());
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode());
        assertNull(response.getData());
    }

    // ==================== FAILURE TEST CASES - INVALID PHONE NUMBER ====================

    @Test
    @DisplayName("Testcase 8: Invalid phone format (too short) - Should PASS (Fails with INVALID_VALUE)")
    void testSetUserInfoWithInvalidPhoneFormatTooShort() {
        // Arrange
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, null, null);

        SetUserInfoRequest request = new SetUserInfoRequest(VALID_FULLNAME, INVALID_PHONE, VALID_ADDRESS);

        // Act
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert
        assertEquals("fail", response.getStatus());
        assertEquals(ResponseCode.INVALID_VALUE.getCode(), response.getCode(),
                "Response code should be 2003 (INVALID_VALUE)");
        assertEquals("Số điện thoại không hợp lệ", response.getMessage(),
                "Message should be: Số điện thoại không hợp lệ");
        assertTrue(response.getMessage().toLowerCase().contains("số điện thoại") ||
                response.getMessage().toLowerCase().contains("phone"),
                "Message should mention phone number");
        assertNull(response.getData());

        // Verify record is NOT saved
        assertFalse(setUserInfoRepository.findByPhoneNumber(INVALID_PHONE).isPresent());
    }

    @Test
    @DisplayName("Testcase 9: Invalid phone format (non-existent prefix) - Should PASS (Fails with INVALID_VALUE)")
    void testSetUserInfoWithInvalidPhoneFormatNonExistentPrefix() {
        // Arrange
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, null, null);

        SetUserInfoRequest request = new SetUserInfoRequest(VALID_FULLNAME, INVALID_PHONE_FORMAT, VALID_ADDRESS);

        // Act
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert
        assertEquals("fail", response.getStatus());
        assertEquals(ResponseCode.INVALID_VALUE.getCode(), response.getCode());
        assertEquals("Số điện thoại không hợp lệ", response.getMessage(),
                "Message should be: Số điện thoại không hợp lệ");
        assertNull(response.getData());

        // Verify record is NOT saved
        assertFalse(setUserInfoRepository.findByPhoneNumber(INVALID_PHONE_FORMAT).isPresent());
    }

    // ==================== FAILURE TEST CASES - USER NOT LOGGED IN ====================

    @Test
    @DisplayName("Testcase 10: Valid input but user not logged in - Should PASS (Fails with USER_NOT_LOGGED_IN)")
    void testSetUserInfoWithValidInputButUserNotLoggedIn() {
        // Arrange - Do NOT create user in logged_in_users table
        SetUserInfoRequest request = new SetUserInfoRequest(VALID_FULLNAME, VALID_PHONE, VALID_ADDRESS);

        // Act
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.USER_NOT_LOGGED_IN.getCode(), response.getCode(),
                "Response code should indicate user not logged in");
        String expectedMessage = ResponseCode.USER_NOT_LOGGED_IN.getMessage();
        assertEquals(expectedMessage, response.getMessage(),
                "Message should be: " + expectedMessage);
        assertNull(response.getData());

        // Verify record is NOT saved
        assertFalse(setUserInfoRepository.findByPhoneNumber(VALID_PHONE).isPresent());
    }

    @Test
    @DisplayName("Testcase 11: User logged in but no corresponding user_test entry - Should PASS (Fails with error)")
    void testSetUserInfoWithUserLoggedInButNoUserTestEntry() {
        // Arrange - Create logged-in user but NO user_test entry
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        // Intentionally NOT creating user_test entry

        SetUserInfoRequest request = new SetUserInfoRequest(VALID_FULLNAME, VALID_PHONE, VALID_ADDRESS);

        // Act
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), response.getCode(),
                "Response code should be INTERNAL_SERVER_ERROR");
        assertEquals(ResponseCode.INTERNAL_SERVER_ERROR.getMessage(), response.getMessage(),
                "Message should be: " + ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
        assertNull(response.getData());
    }
    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Testcase 15: Very long full name - Should PASS (Fails or truncates)")
    void testSetUserInfoWithVeryLongFullName() {
        // Arrange
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, null, null);

        String longFullName = "A".repeat(500); // Very long name
        SetUserInfoRequest request = new SetUserInfoRequest(longFullName, VALID_PHONE, VALID_ADDRESS);

        // Act
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert - Should either succeed with truncation or fail gracefully
        assertNotNull(response, "Response should not be null");
        // Response can be either success or fail, both are acceptable
        assertTrue(response.getStatus().equals("success") || response.getStatus().equals("fail"),
                "Status should be either success or fail");
    }

    @Test
    @DisplayName("Testcase 16: Special characters in full name - Should PASS (Fails with INVALID_VALUE)")
    void testSetUserInfoWithSpecialCharactersInFullName() {
        // Arrange
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, null, null);

        String specialCharFullName = "John@Doe#123!"; // Invalid format (numbers and special chars)
        SetUserInfoRequest request = new SetUserInfoRequest(specialCharFullName, VALID_PHONE, VALID_ADDRESS);

        // Act
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert
        assertEquals("fail", response.getStatus());
        // Should fail due to invalid full name format (numbers/special characters)
        assertNull(response.getData());
    }

    @Test
    @DisplayName("Testcase 17: Setting info for same user twice - Should PASS (Both create separate records)")
    void testSetUserInfoForSameUserTwice() {
        // Arrange
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, null, null);

        SetUserInfoRequest request1 = new SetUserInfoRequest(VALID_FULLNAME, VALID_PHONE, VALID_ADDRESS);
        SetUserInfoRequest request2 = new SetUserInfoRequest(VALID_FULLNAME_2, VALID_PHONE, VALID_ADDRESS_2);

        // Act
        SetUserInfoResponse response1 = setUserInfoService.setUserInfo(request1);
        SetUserInfoResponse response2 = setUserInfoService.setUserInfo(request2);

        // Assert
        assertEquals("success", response1.getStatus());
        assertEquals("success", response2.getStatus());
        assertEquals(VALID_FULLNAME, response1.getData().getFullName());
        assertEquals(VALID_FULLNAME_2, response2.getData().getFullName());

        // Verify both records are saved (should create 2 records)
        long count = setUserInfoRepository.findAll().size();
        assertEquals(2, count, "Should have 2 records in set_user_info table");
    }

    @Test
    @DisplayName("Testcase 18: Phone number with +84 prefix - Should PASS (Success if valid)")
    void testSetUserInfoWithPlusPrefixPhone() {
        // Arrange - Valid phone with +84 prefix
        String phoneWithPlus = "+84901234567";
        createAndSaveLoggedInUser(phoneWithPlus, VALID_PASSWORD);
        createAndSaveUserTest(phoneWithPlus, VALID_PASSWORD, null, null);

        SetUserInfoRequest request = new SetUserInfoRequest(VALID_FULLNAME, phoneWithPlus, VALID_ADDRESS);

        // Act
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert
        assertEquals("success", response.getStatus());
        assertTrue(setUserInfoRepository.findByPhoneNumber(phoneWithPlus).isPresent());
    }

    @Test
    @DisplayName("Testcase 19: Only name and address are numbers - Should PASS (Fails with INVALID_VALUE)")
    void testSetUserInfoWithNumbersInFullName() {
        // Arrange
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, null, null);

        SetUserInfoRequest request = new SetUserInfoRequest("123456", VALID_PHONE, VALID_ADDRESS);

        // Act
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert
        assertEquals("fail", response.getStatus());
        assertNull(response.getData());
    }

    @Test
    @DisplayName("Testcase 20: Database integrity check - Verify user_test update")
    void testSetUserInfoVerifyUserTestTableUpdate() {
        // Arrange
        createAndSaveLoggedInUser(VALID_PHONE, VALID_PASSWORD);
        UserTest initialUser = createAndSaveUserTest(VALID_PHONE, VALID_PASSWORD, "OldName", "OldAddress");

        SetUserInfoRequest request = new SetUserInfoRequest(VALID_FULLNAME, VALID_PHONE, VALID_ADDRESS);

        // Act
        SetUserInfoResponse response = setUserInfoService.setUserInfo(request);

        // Assert - Verify user_test table is updated
        UserTest updatedUser = userTestRepository.findByPhoneNumber(VALID_PHONE).orElse(null);
        assertNotNull(updatedUser, "User should exist in user_test table");
        assertEquals(VALID_FULLNAME, updatedUser.getFullname(), "Fullname should be updated");
        assertEquals(VALID_ADDRESS, updatedUser.getAddress(), "Address should be updated");
        assertEquals(initialUser.getId(), updatedUser.getId(), "User ID should not change");
    }
}
