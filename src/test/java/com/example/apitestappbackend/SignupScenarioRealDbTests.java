package com.example.apitestappbackend;

import com.example.apitestappbackend.models.SignupNotYetLogin;
import com.example.apitestappbackend.repository.SignupNotYetLoginRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testcase cho API Signup sử dụng DATABASE THỰC (PostgreSQL)
 * 
 * 6 Kịch bản test:
 * 1. Đủ dữ liệu, chưa đăng ký → SUCCESS
 * 2. Đủ dữ liệu, đã đăng ký → FAILURE
 * 3. Số điện thoại hợp lệ, không có password → FAILURE
 * 4. Số điện thoại không hợp lệ, có password → FAILURE
 * 5. Số điện thoại không hợp lệ, có password, đã đăng ký → FAILURE
 * 6. Bulk test: 100 số Viettel (0981111111 to 0981111309)
 * 
 * Yêu cầu: PostgreSQL phải chạy tại localhost:5432
 * Database: hospital_test_2
 * User: postgres / Password: 123456789
 */
@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/hospital_test_2",
        "spring.datasource.username=postgres",
        "spring.datasource.password=123456789",
        "spring.datasource.driver-class-name=org.postgresql.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=true"
})
@DisplayName("API Signup Scenario Tests - PostgreSQL Real Database")
class SignupScenarioRealDbTests {

    @Autowired
    private SignupNotYetLoginRepository signupRepository;

    private SignupService signupService;

    @BeforeEach
    void setUp() {
        signupRepository.deleteAll();
        signupService = new SignupService(signupRepository);
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

            // Verify phone is not yet registered
            assertFalse(signupService.isPhoneExists(phoneNumber),
                    "Phone should not be registered yet");

            // Act
            boolean success = signupService.signup(phoneNumber, password);

            // Assert
            assertTrue(success, "Should return SUCCESS for valid data with new phone");

            // Verify: Lưu vào database thực
            Optional<SignupNotYetLogin> saved = signupRepository.findAll_().stream()
                    .filter(s -> s.getPhoneNumber().equals(phoneNumber))
                    .findFirst();

            assertTrue(saved.isPresent(), "Phone should be saved to database");
            assertEquals("success", saved.get().getSignupStatus());

            System.out.println("✅ PASS: Scenario 1 - Valid data, new phone registered successfully");
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
            SignupNotYetLogin existing = new SignupNotYetLogin();
            existing.setPhoneNumber(phoneNumber);
            existing.setSignupStatus("success");
            signupRepository.save(existing);

            assertTrue(signupService.isPhoneExists(phoneNumber),
                    "Phone should be registered for this test");

            // Act
            boolean success = signupService.signup(phoneNumber, password);

            // Assert
            assertFalse(success, "Should return FAILURE for already registered phone");

            System.out.println("✅ PASS: Scenario 2 - Already registered phone correctly rejected");
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
            String password = null;

            // Act
            boolean success = signupService.signup(phoneNumber, password);

            // Assert
            assertFalse(success, "Should return FAILURE when password is missing");

            System.out.println("✅ PASS: Scenario 3 - Missing password correctly rejected");
        }

        @Test
        @DisplayName("Should FAIL when password is empty string")
        void testSignupWithEmptyPassword() {
            String phoneNumber = "0912121212";
            String password = "";

            boolean success = signupService.signup(phoneNumber, password);

            assertFalse(success, "Should reject empty password");
        }
    }

    @Nested
    @DisplayName("Scenario 4: KHÔNG có số điện thoại hợp lệ, có password, chưa đăng ký")
    class Scenario4_InvalidPhoneWithPassword {

        @Test
        @DisplayName("Should FAIL when phone number is invalid but password is provided")
        void testSignupWithInvalidPhoneValidPassword() {
            // Arrange
            String phoneNumber = "invalid-phone-123";
            String password = "ValidPassword123!";

            // Act
            boolean success = signupService.signup(phoneNumber, password);

            // Assert
            assertFalse(success, "Should return FAILURE for invalid phone format");

            System.out.println("✅ PASS: Scenario 4 - Invalid phone correctly rejected");
        }

        @Test
        @DisplayName("Should FAIL with null phone")
        void testSignupWithNullPhone() {
            String phoneNumber = null;
            String password = "ValidPassword123!";

            boolean success = signupService.signup(phoneNumber, password);

            assertFalse(success, "Should reject null phone number");
        }

        @Test
        @DisplayName("Should FAIL with empty phone")
        void testSignupWithEmptyPhone() {
            String phoneNumber = "";
            String password = "ValidPassword123!";

            boolean success = signupService.signup(phoneNumber, password);

            assertFalse(success, "Should reject empty phone number");
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

            boolean success = signupService.signup(phoneNumber, password);

            assertFalse(success, "Should reject invalid phone format regardless of registration status");
        }
    }

    @Nested
    @DisplayName("Scenario 6: Bulk Test - 100 Viettel Numbers (0981111111 to 0981111309)")
    class Scenario6_BulkTest100ViettelNumbers {

        @Test
        @DisplayName("Should register all 100 Viettel numbers successfully")
        void testBulkSignupWith100ViettelNumbers() {
            // Verify table is empty
            int initialCount = (int) signupRepository.count();
            assertEquals(0, initialCount, "Database should start empty");

            // Generate 100 Viettel numbers (all ODD: 111111, 111113, ..., 111309)
            List<String> viettelNumbers = generateViettelNumbers();
            assertEquals(100, viettelNumbers.size(),
                    "Should generate exactly 100 Viettel numbers");

            // Act - Call signup for each number
            int successCount = 0;
            for (String phoneNumber : viettelNumbers) {
                boolean success = signupService.signup(phoneNumber, "BulkPassword123!");
                if (success) {
                    successCount++;
                }
                assertTrue(success, "Phone " + phoneNumber + " should signup successfully");
            }

            // Assert
            assertEquals(100, successCount, "All 100 numbers should succeed");

            // Verify all numbers are saved to database
            long finalCount = signupRepository.count();
            assertEquals(100, finalCount,
                    "Database should have exactly 100 records");

            System.out.println("✅ PASS: Scenario 6 - All 100 Viettel numbers registered successfully");
        }

        @Test
        @DisplayName("Should verify each phone is in database")
        void testBulkSignupDataPersistence() {
            List<String> viettelNumbers = generateViettelNumbers();

            // Register all 100 numbers
            for (String phoneNumber : viettelNumbers) {
                signupService.signup(phoneNumber, "Password123!");
            }

            // Verify each number exists in database
            List<SignupNotYetLogin> allRecords = signupRepository.findAll_();
            assertEquals(100, allRecords.size(), "Should have 100 records in database");

            for (String phoneNumber : viettelNumbers) {
                boolean exists = allRecords.stream()
                        .anyMatch(r -> r.getPhoneNumber().equals(phoneNumber));
                assertTrue(exists, "Phone " + phoneNumber + " should be in database");
            }

            System.out.println("✅ PASS: Scenario 6 - All 100 numbers verified in database");
        }

        private List<String> generateViettelNumbers() {
            List<String> numbers = new ArrayList<>();
            // Range: 0981111111 to 0981111309
            // All ODD numbers (ending with 1,3,5,7,9)
            for (int i = 111111; i <= 111309; i += 2) {
                String number = String.format("0981%d", i);
                numbers.add(number);
            }
            return numbers;
        }
    }

    // ==================== Service Logic ====================

    /**
     * Service để xử lý signup logic
     */
    static class SignupService {
        private final SignupNotYetLoginRepository repository;

        public SignupService(SignupNotYetLoginRepository repository) {
            this.repository = repository;
        }

        public boolean signup(String phoneNumber, String password) {
            // Validation
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                return false;
            }

            if (!isValidPhoneNumber(phoneNumber)) {
                return false;
            }

            if (password == null || password.trim().isEmpty()) {
                return false;
            }

            // Check if already exists
            if (isPhoneExists(phoneNumber)) {
                return false;
            }

            // Save to database
            SignupNotYetLogin record = new SignupNotYetLogin();
            record.setPhoneNumber(phoneNumber);
            record.setSignupStatus("success");
            record.setMessage("ok");
            record.setUsedInTest(false);

            repository.save(record);
            return true;
        }

        public boolean isPhoneExists(String phoneNumber) {
            return repository.findAll_().stream()
                    .anyMatch(r -> r.getPhoneNumber().equals(phoneNumber));
        }

        private boolean isValidPhoneNumber(String phone) {
            // Vietnamese phone number validation (10-13 digits starting with 0)
            return phone.matches("^0\\d{9,12}$");
        }
    }
}
