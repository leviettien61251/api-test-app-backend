package com.example.apitestappbackend;

import com.example.apitestappbackend.controllers.SignupNotYetLoginController;
import com.example.apitestappbackend.models.SignupNotYetLogin;
import com.example.apitestappbackend.services.SignupNotYetLoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SignupNotYetLoginController.class)
@AutoConfigureRestTestClient
@DisplayName("SignupNotYetLoginController Integration Tests")
class SignupNotYetLoginControllerTests {

    @Autowired
    private MockMvc mockMvc;


    private SignupNotYetLoginService service;

    private List<SignupNotYetLogin> testRecordList;

    @BeforeEach
    void setUp() {
        testRecordList = new ArrayList<>();
    }

    @Nested
    @DisplayName("GET /api/v1/signup-not-yet-login - Happy Path")
    class GetAllHappyPath {

        @Test
        @DisplayName("should return 200 OK with list of records")
        void shouldReturn200_WithListOfRecords() throws Exception {
            SignupNotYetLogin record = createTestRecord("123-456-7890", "success");
            testRecordList.add(record);
            when(service.findAll_()).thenReturn(testRecordList);

            mockMvc.perform(get("/api/v1/signup-not-yet-login")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].phoneNumber").value("123-456-7890"))
                    .andExpect(jsonPath("$[0].signupStatus").value("success"));

            verify(service, times(1)).findAll_();
        }

        @Test
        @DisplayName("should return empty array when no records exist")
        void shouldReturnEmptyArray_WhenNoRecordsExist() throws Exception {
            when(service.findAll_()).thenReturn(new ArrayList<>());

            mockMvc.perform(get("/api/v1/signup-not-yet-login")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(service, times(1)).findAll_();
        }

        @Test
        @DisplayName("should return multiple records with correct data")
        void shouldReturnMultipleRecords_WithCorrectData() throws Exception {
            testRecordList.add(createTestRecord("111-1111", "success"));
            testRecordList.add(createTestRecord("222-2222", "pending"));
            testRecordList.add(createTestRecord("333-3333", "failed"));
            when(service.findAll_()).thenReturn(testRecordList);

            mockMvc.perform(get("/api/v1/signup-not-yet-login")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[0].phoneNumber").value("111-1111"))
                    .andExpect(jsonPath("$[1].phoneNumber").value("222-2222"))
                    .andExpect(jsonPath("$[2].phoneNumber").value("333-3333"));

            verify(service, times(1)).findAll_();
        }

        @Test
        @DisplayName("should return records with all fields populated")
        void shouldReturnRecords_WithAllFieldsPopulated() throws Exception {
            SignupNotYetLogin record = new SignupNotYetLogin();
            record.setId("test-id-123");
            record.setPhoneNumber("555-5555");
            record.setSignupStatus("success");
            record.setMessage(null);
            record.setUsedInTest(false);
            testRecordList.add(record);
            when(service.findAll_()).thenReturn(testRecordList);

            mockMvc.perform(get("/api/v1/signup-not-yet-login")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value("test-id-123"))
                    .andExpect(jsonPath("$[0].phoneNumber").value("555-5555"))
                    .andExpect(jsonPath("$[0].signupStatus").value("success"))
                    .andExpect(jsonPath("$[0].usedInTest").value(false));

            verify(service, times(1)).findAll_();
        }
    }

    @Nested
    @DisplayName("GET /api/v1/test/signup-not-yet-login - Happy Path")
    class GetAllTestEndpointHappyPath {

        @Test
        @DisplayName("should return 200 OK with ResponseEntity wrapper")
        void shouldReturn200_WithResponseEntityWrapper() throws Exception {
            SignupNotYetLogin record = createTestRecord("123-456-7890", "success");
            testRecordList.add(record);
            when(service.findAll_()).thenReturn(testRecordList);

            mockMvc.perform(get("/api/v1/test/signup-not-yet-login")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].phoneNumber").value("123-456-7890"));

            verify(service, times(1)).findAll_();
        }

        @Test
        @DisplayName("should return empty list from test endpoint")
        void shouldReturnEmptyList_FromTestEndpoint() throws Exception {
            when(service.findAll_()).thenReturn(new ArrayList<>());

            mockMvc.perform(get("/api/v1/test/signup-not-yet-login")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(service, times(1)).findAll_();
        }

        @Test
        @DisplayName("should handle test endpoint with multiple records")
        void shouldHandleTestEndpoint_WithMultipleRecords() throws Exception {
            for (int i = 0; i < 5; i++) {
                testRecordList.add(createTestRecord(String.format("111-%d%d%d%d", i, i, i, i), "success"));
            }
            when(service.findAll_()).thenReturn(testRecordList);

            mockMvc.perform(get("/api/v1/test/signup-not-yet-login")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(5)));

            verify(service, times(1)).findAll_();
        }
    }

    @Nested
    @DisplayName("HTTP Response Headers and Content-Type")
    class ResponseHeadersAndContentType {

        @Test
        @DisplayName("should return JSON content type for get all endpoint")
        void shouldReturnJsonContentType_ForGetAllEndpoint() throws Exception {
            when(service.findAll_()).thenReturn(new ArrayList<>());

            mockMvc.perform(get("/api/v1/signup-not-yet-login"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

            verify(service, times(1)).findAll_();
        }

        @Test
        @DisplayName("should return JSON content type for test endpoint")
        void shouldReturnJsonContentType_ForTestEndpoint() throws Exception {
            when(service.findAll_()).thenReturn(new ArrayList<>());

            mockMvc.perform(get("/api/v1/test/signup-not-yet-login"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

            verify(service, times(1)).findAll_();
        }

        @Test
        @DisplayName("should handle requests with various Accept headers")
        void shouldHandleRequests_WithVariousAcceptHeaders() throws Exception {
            when(service.findAll_()).thenReturn(new ArrayList<>());

            mockMvc.perform(get("/api/v1/signup-not-yet-login")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(service, times(1)).findAll_();
        }
    }

    @Nested
    @DisplayName("Error Handling and Edge Cases")
    class ErrorHandlingAndEdgeCases {

        @Test
        @DisplayName("should handle service exception with 500 error")
        void shouldHandleServiceException_With500Error() throws Exception {
            when(service.findAll_()).thenThrow(new RuntimeException("Database connection failed"));

            mockMvc.perform(get("/api/v1/signup-not-yet-login")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());

            verify(service, times(1)).findAll_();
        }

        @Test
        @DisplayName("should handle invalid endpoint with 404 error")
        void shouldHandleInvalidEndpoint_With404Error() throws Exception {
            mockMvc.perform(get("/api/v1/invalid-endpoint")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("should handle null service response gracefully")
        void shouldHandleNullServiceResponse_Gracefully() throws Exception {
            when(service.findAll_()).thenReturn(null);

            mockMvc.perform(get("/api/v1/signup-not-yet-login")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(service, times(1)).findAll_();
        }

        @Test
        @DisplayName("should handle records with error messages in response")
        void shouldHandleRecords_WithErrorMessagesInResponse() throws Exception {
            SignupNotYetLogin errorRecord = new SignupNotYetLogin();
            errorRecord.setPhoneNumber("999-0000");
            errorRecord.setSignupStatus("failed");
            errorRecord.setMessage("Invalid phone format");
            testRecordList.add(errorRecord);
            when(service.findAll_()).thenReturn(testRecordList);

            mockMvc.perform(get("/api/v1/signup-not-yet-login")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].message").value("Invalid phone format"));

            verify(service, times(1)).findAll_();
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
