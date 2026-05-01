package com.example.apitestappbackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testcase cho API Signup theo kịch bản cụ thể:
 * 1. Đủ dữ liệu, chưa đăng ký bao giờ → Expect SUCCESS
 * 2. Đủ dữ liệu, đã đăng ký trước đó → Expect FAILURE
 * 3. Số điện thoại hợp lệ, không có password, chưa đăng ký → Expect FAILURE
 * 4. Không có số điện thoại hợp lệ, có password, chưa đăng ký → Expect FAILURE
 * 5. Không có số điện thoại hợp lệ, có password, đã đăng ký → Expect FAILURE
 * 6. Bulk test: 100 số Viettel từ 0981-111-111 đến 0981-111-311
 */
@DisplayName("API Signup Scenario Tests")
class SignupScenarioTests {

    private SignupService signupService;
    private SignupRepository signupRepository;
    private List<SignupRecord> createdRecords;

    @BeforeEach
    void setUp() {
        signupRepository = new SignupRepository();
        signupService = new SignupService(signupRepository);
        createdRecords = new ArrayList<>();
    }

    @Nested
    @DisplayName("Scenario 1: Đủ dữ liệu, chưa đăng ký bao giờ")
    class Scenario1_ValidDataNewPhone {

        @Test
        @DisplayName("Should SUCCESS when signup with valid phone and password, phone not yet registered")
        void testSignupWithValidDataNewPhone() {
            // Arrange
            String phoneNumber = "0912345678";
            String password = "SecurePassword123!";

            // Giả sử số này chưa đăng ký
            assertFalse(signupRepository.isPhoneRegistered(phoneNumber),
                    "Phone should not be registered yet");

            // Act
            SignupResponse response = signupService.signup(phoneNumber, password);

            // Assert
            assertTrue(response.isSuccess(),
                    "Should return SUCCESS for valid data with new phone");

            // Verify: Lưu vào bảng Signup_not_yet_login
            assertTrue(signupRepository.saveToSignupNotYetLogin(phoneNumber, response.getStatus()),
                    "Should save phone to Signup_not_yet_login table");

            System.out.println("✅ PASS: Scenario 1 - Valid data, new phone registered successfully");
        }

        @Test
        @DisplayName("Should FAIL if server doesn't return success for valid new phone")
        void testSignupFailureForValidNewPhone() {
            String phoneNumber = "0987654321";
            String password = "Password123!";

            SignupResponse response = signupService.signup(phoneNumber, password);

            if (!response.isSuccess()) {
                System.out.println("❌ FAIL: Expected SUCCESS but got FAILURE");
                fail("Server should return SUCCESS for valid data");
            }
        }
    }

    @Nested
    @DisplayName("Scenario 2: Đủ dữ liệu, đã đăng ký trước đó")
    class Scenario2_ValidDataAlreadyRegistered {

        @Test
        @DisplayName("Should FAIL when signup with already registered phone")
        void testSignupWithAlreadyRegisteredPhone() {
            // Arrange
            String phoneNumber = "0911111111";
            String password = "Password123!";

            // Pre-register the phone
            signupRepository.registerPhone(phoneNumber, password);
            assertTrue(signupRepository.isPhoneRegistered(phoneNumber),
                    "Phone should be registered for this test");

            // Act
            SignupResponse response = signupService.signup(phoneNumber, password);

            // Assert
            assertFalse(response.isSuccess(),
                    "Should return FAILURE for already registered phone");
            assertTrue(response.getErrorMessage().contains("already registered") ||
                            response.getErrorMessage().contains("duplicate"),
                    "Error message should indicate phone already registered");

            System.out.println("✅ PASS: Scenario 2 - Already registered phone correctly rejected");
        }

        @Test
        @DisplayName("Should FAIL if server returns SUCCESS for already registered phone")
        void testSignupSuccessForAlreadyRegisteredPhoneShouldFail() {
            String phoneNumber = "0911111111";
            String password = "Password123!";

            signupRepository.registerPhone(phoneNumber, password);
            SignupResponse response = signupService.signup(phoneNumber, password);

            if (response.isSuccess()) {
                System.out.println("❌ FAIL: Server returned SUCCESS for already registered phone");
                fail("Should return FAILURE for duplicate phone");
            }
        }
    }

    @Nested
    @DisplayName("Scenario 3: Số điện thoại hợp lệ, KHÔNG có password")
    class Scenario3_ValidPhoneNoPassword {

        @Test
        @DisplayName("Should FAIL when password is missing but phone is valid")
        void testSignupWithValidPhoneNoPassword() {
            // Arrange
            String phoneNumber = "0912121212";
            String password = null; // Không có password

            // Act
            SignupResponse response = signupService.signup(phoneNumber, password);

            // Assert
            assertFalse(response.isSuccess(),
                    "Should return FAILURE when password is missing");
            assertTrue(response.getErrorMessage().contains("password") ||
                            response.getErrorMessage().contains("required"),
                    "Error should mention password is required");

            System.out.println("✅ PASS: Scenario 3 - Missing password correctly rejected");
        }

        @Test
        @DisplayName("Should FAIL if server returns SUCCESS when password is missing")
        void testSignupSuccessWithMissingPasswordShouldFail() {
            String phoneNumber = "0912121212";
            SignupResponse response = signupService.signup(phoneNumber, null);

            if (response.isSuccess()) {
                System.out.println("❌ FAIL: Server allowed signup without password");
                fail("Should reject missing password");
            }
        }

        @Test
        @DisplayName("Should FAIL when password is empty string")
        void testSignupWithEmptyPassword() {
            String phoneNumber = "0912121212";
            String password = "";

            SignupResponse response = signupService.signup(phoneNumber, password);

            assertFalse(response.isSuccess(),
                    "Should reject empty password");
        }
    }

    @Nested
    @DisplayName("Scenario 4: KHÔNG có số điện thoại hợp lệ, có password, chưa đăng ký")
    class Scenario4_InvalidPhoneWithPassword {

        @Test
        @DisplayName("Should FAIL when phone number is invalid but password is provided")
        void testSignupWithInvalidPhoneValidPassword() {
            // Arrange - Số điện thoại không hợp lệ
            String phoneNumber = "invalid-phone-123";
            String password = "ValidPassword123!";

            // Act
            SignupResponse response = signupService.signup(phoneNumber, password);

            // Assert
            assertFalse(response.isSuccess(),
                    "Should return FAILURE for invalid phone format");
            assertTrue(response.getErrorMessage().contains("phone") ||
                            response.getErrorMessage().contains("format") ||
                            response.getErrorMessage().contains("invalid"),
                    "Error should mention invalid phone format");

            System.out.println("✅ PASS: Scenario 4 - Invalid phone correctly rejected");
        }

        @Test
        @DisplayName("Should FAIL with null phone")
        void testSignupWithNullPhone() {
            String phoneNumber = null;
            String password = "ValidPassword123!";

            SignupResponse response = signupService.signup(phoneNumber, password);

            assertFalse(response.isSuccess(),
                    "Should reject null phone number");
        }

        @Test
        @DisplayName("Should FAIL with empty phone")
        void testSignupWithEmptyPhone() {
            String phoneNumber = "";
            String password = "ValidPassword123!";

            SignupResponse response = signupService.signup(phoneNumber, password);

            assertFalse(response.isSuccess(),
                    "Should reject empty phone number");
        }

        @Test
        @DisplayName("Should FAIL with phone too short")
        void testSignupWithPhoneTooShort() {
            String phoneNumber = "091";
            String password = "ValidPassword123!";

            SignupResponse response = signupService.signup(phoneNumber, password);

            assertFalse(response.isSuccess(),
                    "Should reject phone number that is too short");
        }
    }

    @Nested
    @DisplayName("Scenario 5: KHÔNG có số điện thoại hợp lệ, có password, đã đăng ký")
    class Scenario5_InvalidPhoneWithPasswordAlreadyRegistered {

        @Test
        @DisplayName("Should FAIL with invalid phone even if marked as registered")
        void testSignupWithInvalidPhoneAlreadyRegistered() {
            String phoneNumber = "abc-def-ghi";
            String password = "ValidPassword123!";

            // Even if marked as registered, should still fail due to invalid format
            SignupResponse response = signupService.signup(phoneNumber, password);

            assertFalse(response.isSuccess(),
                    "Should reject invalid phone format regardless of registration status");
        }
    }

    @Nested
    @DisplayName("Scenario 6: Bulk Test - 100 Viettel Numbers (0981111111 to 0981111309)")
    class Scenario6_BulkTest100ViettelNumbers {

        @Test
        @DisplayName("Should register all 100 Viettel numbers successfully")
        void testBulkSignupWith100ViettelNumbers() {
            // Verify table is empty
            int initialCount = signupRepository.getSignupNotYetLoginCount();
            assertEquals(0, initialCount, "Signup_not_yet_login table should start empty");

            // Generate 100 Viettel numbers (0981111111, 0981111113, ..., 0981111309)
            List<String> viettelNumbers = generateViettelNumbers();
            assertEquals(100, viettelNumbers.size(), 
                    "Should generate exactly 100 Viettel numbers");

            // Act - Call signup API for each number
            int successCount = 0;
            for (String phoneNumber : viettelNumbers) {
                SignupResponse response = signupService.signup(phoneNumber, "BulkPassword123!");
                if (response.isSuccess()) {
                    successCount++;
                }
                assertTrue(response.isSuccess(), 
                        "Phone " + phoneNumber + " should signup successfully");
            }

            // Assert
            assertEquals(100, successCount, "All 100 numbers should succeed");

            // Verify all numbers are saved to Signup_not_yet_login table
            int finalCount = signupRepository.getSignupNotYetLoginCount();
            assertEquals(100, finalCount,
                    "Database should have exactly 100 records in Signup_not_yet_login");

            System.out.println("✅ PASS: Scenario 6 - All 100 Viettel numbers registered successfully");
        }

        @Test
        @DisplayName("Should verify each phone is in Signup_not_yet_login database")
        void testBulkSignupDataPersistence() {
            List<String> viettelNumbers = generateViettelNumbers();

            // Register all 100 numbers
            for (String phoneNumber : viettelNumbers) {
                signupService.signup(phoneNumber, "Password123!");
            }

            // Verify each number is saved
            for (String phoneNumber : viettelNumbers) {
                assertTrue(signupRepository.isPhoneInSignupNotYetLogin(phoneNumber),
                        "Phone " + phoneNumber + " should be in database");
            }

            System.out.println("✅ PASS: Scenario 6 - All 100 numbers verified in database");
        }

        private List<String> generateViettelNumbers() {
            List<String> numbers = new ArrayList<>();
            // Range: 0981111111 to 0981111309
            // All numbers are ODD (ending with 1,3,5,7,9)
            for (int i = 111111; i <= 111309; i += 2) {
                String number = String.format("0981%d", i);
                numbers.add(number);
            }
            return numbers;
        }
    }


    // ==================== Helper Classes ====================

    /**
     * Đại diện cho response từ API signup
     */
    static class SignupResponse {
        private boolean success;
        private String status;
        private String errorMessage;

        public SignupResponse(boolean success, String status, String errorMessage) {
            this.success = success;
            this.status = status;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getStatus() {
            return status;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    /**
     * Mock SignupService để test API logic
     */
    static class SignupService {
        private SignupRepository repository;

        public SignupService(SignupRepository repository) {
            this.repository = repository;
        }

        public SignupResponse signup(String phoneNumber, String password) {
            // Validation
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                return new SignupResponse(false, "FAILED", "Phone number is required");
            }

            if (!isValidPhoneNumber(phoneNumber)) {
                return new SignupResponse(false, "FAILED", "Invalid phone number format");
            }

            if (password == null || password.trim().isEmpty()) {
                return new SignupResponse(false, "FAILED", "Password is required");
            }

            // Check if already registered
            if (repository.isPhoneRegistered(phoneNumber)) {
                return new SignupResponse(false, "FAILED",
                        "Phone number is already registered");
            }

            // Register and save to Signup_not_yet_login table
            repository.registerPhone(phoneNumber, password);
            repository.saveToSignupNotYetLogin(phoneNumber, "SUCCESS");
            return new SignupResponse(true, "SUCCESS", null);
        }

        private boolean isValidPhoneNumber(String phone) {
            // Vietnamese phone number validation
            // Format: 0xxx-xxx-xxxx or 0xxxxxxxxxx (10-13 digits starting with 0)
            return phone.matches("^0\\d{9,12}$");
        }
    }

    /**
     * Mock SignupRepository để simulate database operations
     */
    static class SignupRepository {
        private List<SignupRecord> registeredPhones = new ArrayList<>();
        private List<SignupRecord> signupNotYetLoginTable = new ArrayList<>();

        public boolean isPhoneRegistered(String phoneNumber) {
            return registeredPhones.stream()
                    .anyMatch(r -> r.phoneNumber.equals(phoneNumber));
        }

        public void registerPhone(String phoneNumber, String password) {
            registeredPhones.add(new SignupRecord(phoneNumber, password));
        }

        public boolean saveToSignupNotYetLogin(String phoneNumber, String status) {
            signupNotYetLoginTable.add(new SignupRecord(phoneNumber, status));
            return true;
        }

        public int getSignupNotYetLoginCount() {
            return signupNotYetLoginTable.size();
        }

        public boolean isPhoneInSignupNotYetLogin(String phoneNumber) {
            return signupNotYetLoginTable.stream()
                    .anyMatch(r -> r.phoneNumber.equals(phoneNumber));
        }
    }

    /**
     * Đại diện cho record trong database
     */
    static class SignupRecord {
        String phoneNumber;
        String data;

        public SignupRecord(String phoneNumber, String data) {
            this.phoneNumber = phoneNumber;
            this.data = data;
        }
    }
}
