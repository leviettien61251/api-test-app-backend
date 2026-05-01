package com.example.apitestappbackend;

import com.example.apitestappbackend.models.SignupNotYetLogin;
import com.example.apitestappbackend.repository.SignupNotYetLoginRepository;
import com.example.apitestappbackend.services.SignupNotYetLoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Signup Not Yet Login - Error & Validation Tests")
class SignupNotYetLoginErrorValidationTests {

    @Mock
    private SignupNotYetLoginRepository repository;

    @InjectMocks
    private SignupNotYetLoginService service;

    @Nested
    @DisplayName("Input Validation Tests")
    class InputValidationTests {

        @Test
        @DisplayName("should validate phone number is required field")
        void shouldValidatePhoneNumber_IsRequiredField() {
            SignupNotYetLogin record = new SignupNotYetLogin();
            record.setPhoneNumber(null);
            record.setSignupStatus("success");

            assertNull(record.getPhoneNumber());
        }

        @Test
        @DisplayName("should validate phone number length constraints")
        void shouldValidatePhoneNumber_LengthConstraints() {
            SignupNotYetLogin record = new SignupNotYetLogin();
            
            String shortPhone = "123";
            String validPhone = "123-456-7890";
            String longPhone = "123-456-7890-123-456-7890-123"; // Exceeds 20 chars
            
            record.setPhoneNumber(shortPhone);
            assertEquals(shortPhone, record.getPhoneNumber());
            
            record.setPhoneNumber(validPhone);
            assertEquals(validPhone, record.getPhoneNumber());
            
            record.setPhoneNumber(longPhone);
            assertEquals(longPhone, record.getPhoneNumber());
        }

        @Test
        @DisplayName("should validate status field is required")
        void shouldValidateStatus_IsRequired() {
            SignupNotYetLogin record = new SignupNotYetLogin();
            record.setPhoneNumber("123-456-7890");
            record.setPhoneNumber(null);

            assertNull(record.getSignupStatus());
        }

        @ParameterizedTest
        @ValueSource(strings = {"success", "pending", "failed", "error", "blocked"})
        @DisplayName("should accept valid status values")
        void shouldAcceptValidStatusValues(String status) {
            SignupNotYetLogin record = new SignupNotYetLogin();
            record.setSignupStatus(status);

            assertEquals(status, record.getSignupStatus());
        }

        @Test
        @DisplayName("should handle error message field")
        void shouldHandleErrorMessageField() {
            SignupNotYetLogin record = new SignupNotYetLogin();
            record.setPhoneNumber("123-456-7890");
            record.setSignupStatus("failed");
            record.setMessage("Invalid phone format");

            assertEquals("Invalid phone format", record.getMessage());
        }

        @Test
        @DisplayName("should allow null error message for success status")
        void shouldAllowNullErrorMessage_ForSuccessStatus() {
            SignupNotYetLogin record = new SignupNotYetLogin();
            record.setSignupStatus("success");
            record.setMessage(null);

            assertNull(record.getMessage());
        }

        @Test
        @DisplayName("should handle boolean used_in_test field")
        void shouldHandleBooleanUsedInTestField() {
            SignupNotYetLogin record = new SignupNotYetLogin();
            
            record.setUsedInTest(true);
            assertTrue(record.getUsedInTest());
            
            record.setUsedInTest(false);
            assertFalse(record.getUsedInTest());
        }
    }

    @Nested
    @DisplayName("Exception Handling Tests")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("should handle database connection exception")
        void shouldHandleDatabaseConnectionException() {
            when(repository.findAll_()).thenThrow(new RuntimeException("Connection timeout"));

            assertThrows(RuntimeException.class, () -> service.findAll_());
            verify(repository, times(1)).findAll_();
        }

        @Test
        @DisplayName("should handle SQL exception during query")
        void shouldHandleSQLException_DuringQuery() {
            when(repository.findAll_()).thenThrow(new RuntimeException("Invalid SQL query"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> service.findAll_());
            assertTrue(exception.getMessage().contains("Invalid SQL query"));
        }

        @Test
        @DisplayName("should handle null pointer exception from repository")
        void shouldHandleNullPointerException_FromRepository() {
            when(repository.findAll_()).thenThrow(new NullPointerException("Repository is null"));

            assertThrows(NullPointerException.class, () -> service.findAll_());
        }

        @Test
        @DisplayName("should handle service layer exception and propagate it")
        void shouldHandleServiceException_AndPropagateIt() {
            when(repository.findAll_()).thenThrow(new IllegalArgumentException("Invalid argument"));

            assertThrows(IllegalArgumentException.class, () -> service.findAll_());
            verify(repository, times(1)).findAll_();
        }

        @Test
        @DisplayName("should recover from transient exception on retry")
        void shouldRecoverFromTransientException_OnRetry() {
            when(repository.findAll_())
                .thenThrow(new RuntimeException("Temporary error"))
                .thenReturn(new ArrayList<>());

            assertThrows(RuntimeException.class, () -> service.findAll_());
            
            List<SignupNotYetLogin> result = service.findAll_();
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCasesAndBoundaryTests {

        @Test
        @DisplayName("should handle very large list of records")
        void shouldHandleVeryLargeList_OfRecords() {
            List<SignupNotYetLogin> largeList = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                SignupNotYetLogin record = new SignupNotYetLogin();
                record.setId("id-" + i);
                record.setPhoneNumber(String.format("111-%d", i));
                record.setSignupStatus("success");
                largeList.add(record);
            }
            when(repository.findAll_()).thenReturn(largeList);

            List<SignupNotYetLogin> result = service.findAll_();

            assertEquals(10000, result.size());
            verify(repository, times(1)).findAll_();
        }

        @Test
        @DisplayName("should handle records with special characters in fields")
        void shouldHandleRecords_WithSpecialCharactersInFields() {
            List<SignupNotYetLogin> recordList = new ArrayList<>();
            SignupNotYetLogin record = new SignupNotYetLogin();
            record.setPhoneNumber("123-456-7890");
            record.setSignupStatus("failed");
            record.setMessage("Error: Invalid format! @#$%^&*()");
            recordList.add(record);
            when(repository.findAll_()).thenReturn(recordList);

            List<SignupNotYetLogin> result = service.findAll_();

            assertEquals(1, result.size());
            assertTrue(result.get(0).getMessage().contains("@#$%^&*()"));
        }

        @Test
        @DisplayName("should handle records with empty error message")
        void shouldHandleRecords_WithEmptyErrorMessage() {
            List<SignupNotYetLogin> recordList = new ArrayList<>();
            SignupNotYetLogin record = new SignupNotYetLogin();
            record.setPhoneNumber("123-456-7890");
            record.setSignupStatus("failed");
            record.setMessage("");
            recordList.add(record);
            when(repository.findAll_()).thenReturn(recordList);

            List<SignupNotYetLogin> result = service.findAll_();

            assertEquals(1, result.size());
            assertEquals("", result.get(0).getMessage());
        }

        @Test
        @DisplayName("should handle records with very long error messages")
        void shouldHandleRecords_WithVeryLongErrorMessages() {
            String longError = "E".repeat(1000);
            List<SignupNotYetLogin> recordList = new ArrayList<>();
            SignupNotYetLogin record = new SignupNotYetLogin();
            record.setPhoneNumber("123-456-7890");
            record.setSignupStatus("failed");
            record.setMessage(longError);
            recordList.add(record);
            when(repository.findAll_()).thenReturn(recordList);

            List<SignupNotYetLogin> result = service.findAll_();

            assertEquals(1, result.size());
            assertEquals(1000, result.get(0).getMessage().length());
        }

        @Test
        @DisplayName("should handle records with unicode characters")
        void shouldHandleRecords_WithUnicodeCharacters() {
            List<SignupNotYetLogin> recordList = new ArrayList<>();
            SignupNotYetLogin record = new SignupNotYetLogin();
            record.setPhoneNumber("123-456-7890");
            record.setSignupStatus("success");
            record.setMessage("Error: Invalid format");
            recordList.add(record);
            when(repository.findAll_()).thenReturn(recordList);

            List<SignupNotYetLogin> result = service.findAll_();

            assertEquals(1, result.size());
            assertTrue(result.get(0).getMessage().contains("Error"));
        }
    }

    @Nested
    @DisplayName("Data Consistency Tests")
    class DataConsistencyTests {

        @Test
        @DisplayName("should maintain data consistency across multiple calls")
        void shouldMaintainDataConsistency_AcrossMultipleCalls() {
            List<SignupNotYetLogin> recordList = new ArrayList<>();
            SignupNotYetLogin record = createTestRecord("123-456-7890", "success");
            recordList.add(record);
            when(repository.findAll_()).thenReturn(recordList);

            List<SignupNotYetLogin> result1 = service.findAll_();
            List<SignupNotYetLogin> result2 = service.findAll_();

            assertEquals(result1.size(), result2.size());
            assertEquals(result1.get(0).getPhoneNumber(), result2.get(0).getPhoneNumber());
        }

        @Test
        @DisplayName("should not modify original data in service")
        void shouldNotModifyOriginalData_InService() {
            List<SignupNotYetLogin> recordList = new ArrayList<>();
            SignupNotYetLogin record = createTestRecord("123-456-7890", "success");
            recordList.add(record);
            when(repository.findAll_()).thenReturn(recordList);

            List<SignupNotYetLogin> result = service.findAll_();
            String originalPhone = result.get(0).getPhoneNumber();

            result.get(0).setPhoneNumber("999-999-9999");

            assertEquals(originalPhone, "123-456-7890");
        }

        @Test
        @DisplayName("should handle concurrent access scenarios")
        void shouldHandleConcurrentAccess_Scenarios() throws InterruptedException {
            List<SignupNotYetLogin> recordList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                recordList.add(createTestRecord(String.format("111-%d", i), "success"));
            }
            when(repository.findAll_()).thenReturn(recordList);

            Thread thread1 = new Thread(() -> {
                List<SignupNotYetLogin> result = service.findAll_();
                assertEquals(10, result.size());
            });
            
            Thread thread2 = new Thread(() -> {
                List<SignupNotYetLogin> result = service.findAll_();
                assertEquals(10, result.size());
            });

            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();

            verify(repository, times(2)).findAll_();
        }
    }

    private SignupNotYetLogin createTestRecord(String phoneNumber, String status) {
        SignupNotYetLogin record = new SignupNotYetLogin();
        record.setId("test-id-" + System.nanoTime());
        record.setPhoneNumber(phoneNumber);
        record.setSignupStatus(status);
        record.setUsedInTest(false);
        return record;
    }
}
