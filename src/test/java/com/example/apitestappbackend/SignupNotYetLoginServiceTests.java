package com.example.apitestappbackend;

import com.example.apitestappbackend.models.SignupNotYetLogin;
import com.example.apitestappbackend.repository.SignupNotYetLoginRepository;
import com.example.apitestappbackend.services.SignupNotYetLoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SignupNotYetLoginService Tests")
class SignupNotYetLoginServiceTests {

    @Mock
    private SignupNotYetLoginRepository repository;

    @InjectMocks
    private SignupNotYetLoginService service;

    private SignupNotYetLogin testRecord;
    private List<SignupNotYetLogin> testRecordList;

    @BeforeEach
    void setUp() {
        testRecord = createTestRecord("123-456-7890", "success");
        testRecordList = new ArrayList<>();
    }

    @Nested
    @DisplayName("findAll_() - Happy Path Tests")
    class FindAllHappyPath {

        @Test
        @DisplayName("should return list of records when data exists")
        void shouldReturnListOfRecords_WhenDataExists() {
            testRecordList.add(testRecord);
            when(repository.findAll_()).thenReturn(testRecordList);

            List<SignupNotYetLogin> result = service.findAll_();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("123-456-7890", result.get(0).getPhoneNumber());
            assertEquals("success", result.get(0).getSignupStatus());
            verify(repository, times(1)).findAll_();
        }

        @Test
        @DisplayName("should return empty list when no records exist")
        void shouldReturnEmptyList_WhenNoRecordsExist() {
            when(repository.findAll_()).thenReturn(new ArrayList<>());

            List<SignupNotYetLogin> result = service.findAll_();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            assertEquals(0, result.size());
            verify(repository, times(1)).findAll_();
        }

        @Test
        @DisplayName("should return multiple records correctly")
        void shouldReturnMultipleRecords_Correctly() {
            SignupNotYetLogin record1 = createTestRecord("111-222-3333", "success");
            SignupNotYetLogin record2 = createTestRecord("444-555-6666", "pending");
            SignupNotYetLogin record3 = createTestRecord("777-888-9999", "failed");
            testRecordList.add(record1);
            testRecordList.add(record2);
            testRecordList.add(record3);
            when(repository.findAll_()).thenReturn(testRecordList);

            List<SignupNotYetLogin> result = service.findAll_();

            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals("111-222-3333", result.get(0).getPhoneNumber());
            assertEquals("444-555-6666", result.get(1).getPhoneNumber());
            assertEquals("777-888-9999", result.get(2).getPhoneNumber());
            verify(repository, times(1)).findAll_();
        }

        @Test
        @DisplayName("should preserve all fields in returned records")
        void shouldPreserveAllFields_InReturnedRecords() {
            SignupNotYetLogin recordWithDetails = new SignupNotYetLogin();
            recordWithDetails.setId("test-id-123");
            recordWithDetails.setPhoneNumber("555-1234");
            recordWithDetails.setSignupStatus("success");
            recordWithDetails.setMessage(null);
            recordWithDetails.setUsedInTest(false);
            testRecordList.add(recordWithDetails);
            when(repository.findAll_()).thenReturn(testRecordList);

            List<SignupNotYetLogin> result = service.findAll_();

            assertNotNull(result);
            assertEquals(1, result.size());
            SignupNotYetLogin returnedRecord = result.get(0);
            assertEquals("test-id-123", returnedRecord.getId());
            assertEquals("555-1234", returnedRecord.getPhoneNumber());
            assertEquals("success", returnedRecord.getSignupStatus());
            assertNull(returnedRecord.getMessage());
            assertFalse(returnedRecord.getUsedInTest());
        }
    }

    @Nested
    @DisplayName("findAll_() - Error Handling Tests")
    class FindAllErrorHandling {

        @Test
        @DisplayName("should handle repository exception gracefully")
        void shouldHandleRepositoryException() {
            when(repository.findAll_()).thenThrow(new RuntimeException("Database connection error"));

            assertThrows(RuntimeException.class, () -> service.findAll_());
            verify(repository, times(1)).findAll_();
        }

        @Test
        @DisplayName("should handle null repository response")
        void shouldHandleNullRepositoryResponse() {
            when(repository.findAll_()).thenReturn(null);

            List<SignupNotYetLogin> result = service.findAll_();

            assertNull(result);
            verify(repository, times(1)).findAll_();
        }

        @Test
        @DisplayName("should call repository exactly once per request")
        void shouldCallRepositoryExactlyOnce() {
            when(repository.findAll_()).thenReturn(testRecordList);

            service.findAll_();
            service.findAll_();

            verify(repository, times(2)).findAll_();
        }
    }

    @Nested
    @DisplayName("findAll_() - Data Scenario Tests")
    class FindAllDataScenarios {

        @Test
        @DisplayName("should return records with various status values")
        void shouldReturnRecords_WithVariousStatusValues() {
            String[] statusValues = {"success", "pending", "failed", "error"};
            for (int i = 0; i < statusValues.length; i++) {
                SignupNotYetLogin record = createTestRecord(String.format("111-%d-%d-%d", i, i+1, i+2), statusValues[i]);
                testRecordList.add(record);
            }
            when(repository.findAll_()).thenReturn(testRecordList);

            List<SignupNotYetLogin> result = service.findAll_();

            assertEquals(4, result.size());
            for (int i = 0; i < statusValues.length; i++) {
                assertEquals(statusValues[i], result.get(i).getSignupStatus());
            }
        }

        @Test
        @DisplayName("should return records with error messages")
        void shouldReturnRecords_WithErrorMessages() {
            SignupNotYetLogin recordWithError = new SignupNotYetLogin();
            recordWithError.setPhoneNumber("999-0000");
            recordWithError.setSignupStatus("failed");
            recordWithError.setMessage("Invalid phone format");
            testRecordList.add(recordWithError);
            when(repository.findAll_()).thenReturn(testRecordList);

            List<SignupNotYetLogin> result = service.findAll_();

            assertEquals(1, result.size());
            assertEquals("Invalid phone format", result.get(0).getMessage());
        }

        @Test
        @DisplayName("should return records with test flag values")
        void shouldReturnRecords_WithTestFlagValues() {
            SignupNotYetLogin testRecord1 = createTestRecord("111-1111", "success");
            testRecord1.setUsedInTest(true);
            SignupNotYetLogin testRecord2 = createTestRecord("222-2222", "success");
            testRecord2.setUsedInTest(false);
            testRecordList.add(testRecord1);
            testRecordList.add(testRecord2);
            when(repository.findAll_()).thenReturn(testRecordList);

            List<SignupNotYetLogin> result = service.findAll_();

            assertEquals(2, result.size());
            assertTrue(result.get(0).getUsedInTest());
            assertFalse(result.get(1).getUsedInTest());
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
