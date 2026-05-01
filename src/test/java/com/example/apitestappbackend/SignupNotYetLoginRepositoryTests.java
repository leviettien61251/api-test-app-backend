package com.example.apitestappbackend;

import com.example.apitestappbackend.models.SignupNotYetLogin;
import com.example.apitestappbackend.repository.SignupNotYetLoginRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("SignupNotYetLoginRepository Tests")
class SignupNotYetLoginRepositoryTests {

    @Autowired
    private SignupNotYetLoginRepository repository;

    private SignupNotYetLogin testRecord;

    @BeforeEach
    void setUp() {
        testRecord = new SignupNotYetLogin();
    }

    @Nested
    @DisplayName("findAll_() - Happy Path Tests")
    class FindAllHappyPath {

        @Test
        @DisplayName("should return list of records when data exists")
        void shouldReturnListOfRecords_WhenDataExists() {
            SignupNotYetLogin record1 = createAndSaveRecord("123-456-7890", "success", null);
            SignupNotYetLogin record2 = createAndSaveRecord("111-222-3333", "pending", null);

            List<SignupNotYetLogin> result = repository.findAll_();

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertTrue(result.stream().anyMatch(r -> r.getPhoneNumber().equals("123-456-7890")));
            assertTrue(result.stream().anyMatch(r -> r.getPhoneNumber().equals("111-222-3333")));
        }

        @Test
        @DisplayName("should return empty list when no records exist")
        void shouldReturnEmptyList_WhenNoRecordsExist() {
            List<SignupNotYetLogin> result = repository.findAll_();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("should return all records with correct data")
        void shouldReturnAllRecords_WithCorrectData() {
            createAndSaveRecord("111-1111", "success", null);
            createAndSaveRecord("222-2222", "failed", "Invalid format");
            createAndSaveRecord("333-3333", "pending", null);

            List<SignupNotYetLogin> result = repository.findAll_();

            assertEquals(3, result.size());
            assertTrue(result.stream().anyMatch(r -> "success".equals(r.getSignupStatus())));
            assertTrue(result.stream().anyMatch(r -> "failed".equals(r.getSignupStatus())));
        }

        @Test
        @DisplayName("should preserve all entity fields after retrieval")
        void shouldPreserveAllFields_AfterRetrieval() {
            SignupNotYetLogin savedRecord = createAndSaveRecord("555-5555", "success", "Test error");
            savedRecord.setUsedInTest(true);
            repository.save(savedRecord);

            List<SignupNotYetLogin> result = repository.findAll_();
            SignupNotYetLogin retrievedRecord = result.stream()
                .filter(r -> r.getPhoneNumber().equals("555-5555"))
                .findFirst()
                .orElse(null);

            assertNotNull(retrievedRecord);
            assertEquals("555-5555", retrievedRecord.getPhoneNumber());
            assertEquals("success", retrievedRecord.getSignupStatus());
            assertEquals("Test error", retrievedRecord.getMessage());
            assertTrue(retrievedRecord.getUsedInTest());
        }
    }

    @Nested
    @DisplayName("findAll_() - Data Integrity Tests")
    class FindAllDataIntegrity {

        @Test
        @DisplayName("should return records with correct status values")
        void shouldReturnRecords_WithCorrectStatusValues() {
            String[] statusValues = {"success", "pending", "failed"};
            for (String status : statusValues) {
                createAndSaveRecord("phone-" + status, status, null);
            }

            List<SignupNotYetLogin> result = repository.findAll_();

            assertEquals(3, result.size());
            for (String status : statusValues) {
                assertTrue(result.stream().anyMatch(r -> status.equals(r.getSignupStatus())));
            }
        }

        @Test
        @DisplayName("should handle records with error messages")
        void shouldHandleRecords_WithErrorMessages() {
            createAndSaveRecord("999-0000", "failed", "Phone number already exists");
            createAndSaveRecord("888-8888", "success", null);

            List<SignupNotYetLogin> result = repository.findAll_();

            SignupNotYetLogin recordWithError = result.stream()
                .filter(r -> r.getPhoneNumber().equals("999-0000"))
                .findFirst()
                .orElse(null);

            assertNotNull(recordWithError);
            assertNotNull(recordWithError.getMessage());
            assertEquals("Phone number already exists", recordWithError.getMessage());
        }

        @Test
        @DisplayName("should handle records with test flag set")
        void shouldHandleRecords_WithTestFlagSet() {
            SignupNotYetLogin record = createAndSaveRecord("777-7777", "success", null);
            record.setUsedInTest(true);
            repository.save(record);

            List<SignupNotYetLogin> result = repository.findAll_();

            SignupNotYetLogin retrievedRecord = result.stream()
                .filter(r -> r.getPhoneNumber().equals("777-7777"))
                .findFirst()
                .orElse(null);

            assertNotNull(retrievedRecord);
            assertTrue(retrievedRecord.getUsedInTest());
        }
    }

    @Nested
    @DisplayName("findAll_() - Record Count Tests")
    class FindAllRecordCount {

        @Test
        @DisplayName("should return correct count of single record")
        void shouldReturnCorrectCount_OfSingleRecord() {
            createAndSaveRecord("111-1111", "success", null);

            List<SignupNotYetLogin> result = repository.findAll_();

            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("should return correct count of multiple records")
        void shouldReturnCorrectCount_OfMultipleRecords() {
            for (int i = 0; i < 5; i++) {
                createAndSaveRecord(String.format("111-%d%d%d%d", i, i, i, i), "success", null);
            }

            List<SignupNotYetLogin> result = repository.findAll_();

            assertEquals(5, result.size());
        }

        @Test
        @DisplayName("should handle large number of records")
        void shouldHandleLargeNumber_OfRecords() {
            for (int i = 0; i < 100; i++) {
                createAndSaveRecord(String.format("100-%03d-%04d", i, i*10), "success", null);
            }

            List<SignupNotYetLogin> result = repository.findAll_();

            assertEquals(100, result.size());
        }
    }

    private SignupNotYetLogin createAndSaveRecord(String phoneNumber, String status, String errorMessage) {
        SignupNotYetLogin record = new SignupNotYetLogin();
        record.setPhoneNumber(phoneNumber);
        record.setSignupStatus(status);
        record.setMessage(errorMessage);
        record.setUsedInTest(false);
        return repository.save(record);
    }
}
