package com.example.apitestappbackend.SignUp;

import com.example.apitestappbackend.DTO.SignUp.SignUpRequest;
import com.example.apitestappbackend.DTO.SignUp.SignUpResponse;
import com.example.apitestappbackend.models.SignupNotYetLogin;
import com.example.apitestappbackend.repository.SignupNotYetLoginRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testcase cho API Signup sử dụng DATABASE THỰC (PostgreSQL)
 *
 * 6 Kịch bản test:
 * 1. Đủ dữ liệu, chưa đăng ký → SUCCESS
 * 2. Đủ dữ liệu, đã đăng ký → FAILURE
 * 3. Số điện thoại hợp lệ, không có password → FAILURE
 * 4. Số điện thoại không hợp lệ, có password → FAILURE
 * 5. Số điện thoại không hợp lệ, có password, đã đăng ký → FAILURE
 * 6. Bulk test: 100 số Viettel mà trừ số 8 trong 098 thì các số còn lại đều là số lẻ 0981-111-111 đến 0981-111-311
 *
 * Yêu cầu: PostgreSQL phải chạy tại localhost:5432
 * Database: hospital_test_2
 * User: postgres / Password: 123456789
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("SignUp API Database Tests")
public class SignUpScenarioDbTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SignupNotYetLoginRepository signupNotYetLoginRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String SIGNUP_ENDPOINT = "/api/v1/signup";
    private static final String VALID_PHONE_1 = "0901234567";
    private static final String VALID_PHONE_2 = "0912345678";
    private static final String VALID_PASSWORD = "password123";
    private static final String INVALID_PHONE = "0123456789"; // Missing required digit pattern
    private static final String BLANK_PASSWORD = "";

    @BeforeEach
    void setUp() {
        signupNotYetLoginRepository.deleteAll();
    }

    @Test
    @DisplayName("Scenario 1: Valid data, not yet registered → SUCCESS")
    void testScenario1_ValidDataNotYetRegistered() throws Exception {
        SignUpRequest request = new SignUpRequest(VALID_PHONE_1, VALID_PASSWORD);

        MvcResult result = mockMvc.perform(post(SIGNUP_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        SignUpResponse response = objectMapper.readValue(responseBody, SignUpResponse.class);

        assertThat(response.getSignupStatus()).isEqualTo("success");
        assertThat(response.getCode()).isEqualTo("1000");
        assertThat(response.getMessage()).isEqualTo("Request processed successfully");
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getPhoneNumber()).isEqualTo(VALID_PHONE_1);
        assertThat(response.getData().getId()).isNotNull();

        // Verify data is saved in database
        assertThat(signupNotYetLoginRepository.existsByPhoneNumber(VALID_PHONE_1)).isTrue();
    }

    @Test
    @DisplayName("Scenario 2: Valid data, already registered → FAILURE")
    void testScenario2_ValidDataAlreadyRegistered() throws Exception {
        // First registration - should succeed
        SignUpRequest firstRequest = new SignUpRequest(VALID_PHONE_2, VALID_PASSWORD);
        mockMvc.perform(post(SIGNUP_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isCreated());

        // Second registration with same phone - should fail
        SignUpRequest secondRequest = new SignUpRequest(VALID_PHONE_2, VALID_PASSWORD);
        MvcResult result = mockMvc.perform(post(SIGNUP_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(secondRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        SignUpResponse response = objectMapper.readValue(responseBody, SignUpResponse.class);

        assertThat(response.getSignupStatus()).isEqualTo("fail");
        assertThat(response.getCode()).isEqualTo("3006");
        assertThat(response.getMessage()).isEqualTo("User already exists");
    }

    @Test
    @DisplayName("Scenario 3: Valid phone, no password → FAILURE")
    void testScenario3_ValidPhoneNoPassword() throws Exception {
        SignUpRequest request = new SignUpRequest(VALID_PHONE_1, BLANK_PASSWORD);

        MvcResult result = mockMvc.perform(post(SIGNUP_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        SignUpResponse response = objectMapper.readValue(responseBody, SignUpResponse.class);

        assertThat(response.getSignupStatus()).isEqualTo("fail");
        assertThat(response.getCode()).isEqualTo("2001");
        assertThat(response.getMessage()).isEqualTo("Thiếu trường password");

        // Verify data is NOT saved in database
        assertThat(signupNotYetLoginRepository.existsByPhoneNumber(VALID_PHONE_1)).isFalse();
    }

    @Test
    @DisplayName("Scenario 4: Invalid phone, has password → FAILURE")
    void testScenario4_InvalidPhoneHasPassword() throws Exception {
        SignUpRequest request = new SignUpRequest(INVALID_PHONE, VALID_PASSWORD);

        MvcResult result = mockMvc.perform(post(SIGNUP_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        SignUpResponse response = objectMapper.readValue(responseBody, SignUpResponse.class);

        assertThat(response.getSignupStatus()).isEqualTo("fail");
        assertThat(response.getCode()).isEqualTo("2003");
        assertThat(response.getMessage()).isEqualTo("Số điện thoại không hợp lệ");

        // Verify data is NOT saved in database
        assertThat(signupNotYetLoginRepository.existsByPhoneNumber(INVALID_PHONE)).isFalse();
    }

    @Test
    @DisplayName("Scenario 5: Invalid phone, has password, already registered → FAILURE")
    //doi lai invalid phone
    void testScenario5_InvalidPhoneAlreadyRegistered() throws Exception {
        // Pre-register with invalid phone (manually insert for this scenario)
        SignupNotYetLogin preregistered = new SignupNotYetLogin();
        preregistered.setPhoneNumber(INVALID_PHONE);
        preregistered.setPassword(VALID_PASSWORD);
        preregistered.setSignupStatus("fail");
        signupNotYetLoginRepository.save(preregistered);

        // Try to register with same invalid phone
        SignUpRequest request = new SignUpRequest(INVALID_PHONE, VALID_PASSWORD);

        MvcResult result = mockMvc.perform(post(SIGNUP_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        SignUpResponse response = objectMapper.readValue(responseBody, SignUpResponse.class);

        // The API validates phone format first before checking if user exists
        assertThat(response.getSignupStatus()).isEqualTo("fail");
        assertThat(response.getCode()).isEqualTo("2003");
        assertThat(response.getMessage()).isEqualTo("Số điện thoại không hợp lệ");
    }

    @Test
    @DisplayName("Scenario 6: Bulk test - 100 Viettel numbers (0981111111 to 0981111311, all odd digits except 8)")
    void testScenario6_BulkViettelNumbers() throws Exception {
        int successCount = 0;
        int baseNumber = 1111111;
        int totalBulkTests = 100;

        for (int i = 0; i < totalBulkTests; i++) {
            // Generate odd numbers: 1111111, 1111113, 1111115, ..., 1111309
            // (increment by 2 to ensure all digits are odd)
            int phoneNumber = baseNumber + (i * 2);
            String viettelPhone = "098" + String.format("%06d", phoneNumber);
            SignUpRequest request = new SignUpRequest(viettelPhone, VALID_PASSWORD);

            MvcResult result = mockMvc.perform(post(SIGNUP_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andReturn();

            if (result.getResponse().getStatus() == 201) {
                String responseBody = result.getResponse().getContentAsString();
                SignUpResponse response = objectMapper.readValue(responseBody, SignUpResponse.class);

                if ("success".equals(response.getSignupStatus())) {
                    successCount++;
                    assertThat(response.getCode()).isEqualTo("1000");
                    assertThat(response.getData().getPhoneNumber()).isEqualTo(viettelPhone);
                    assertThat(signupNotYetLoginRepository.existsByPhoneNumber(viettelPhone)).isTrue();
                }
            }
        }

        // All 100 registrations should succeed
        assertThat(successCount).isEqualTo(totalBulkTests);
        assertThat(signupNotYetLoginRepository.count()).isEqualTo(totalBulkTests);
    }

    @Test
    @DisplayName("Additional Test: Invalid password format (too short)")
    void testAdditional_InvalidPasswordTooShort() throws Exception {
        SignUpRequest request = new SignUpRequest(VALID_PHONE_1, "pass"); // Less than 6 characters

        MvcResult result = mockMvc.perform(post(SIGNUP_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        SignUpResponse response = objectMapper.readValue(responseBody, SignUpResponse.class);

        assertThat(response.getSignupStatus()).isEqualTo("fail");
        assertThat(response.getCode()).isEqualTo("2003");
        assertThat(response.getMessage()).isEqualTo("Password không hợp lệ");
    }

    @Test
    @DisplayName("Additional Test: Valid password with special characters")
    void testAdditional_ValidPasswordWithSpecialCharacters() throws Exception {
        String validPhone = "0932456789";
        SignUpRequest request = new SignUpRequest(validPhone, "Pass@123");

        MvcResult result = mockMvc.perform(post(SIGNUP_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        SignUpResponse response = objectMapper.readValue(responseBody, SignUpResponse.class);

        assertThat(response.getSignupStatus()).isEqualTo("success");
        assertThat(response.getCode()).isEqualTo("1000");
        assertThat(response.getData().getPhoneNumber()).isEqualTo(validPhone);
    }

    @Test
    @DisplayName("Additional Test: Different valid Viettel phone prefixes")
    void testAdditional_DifferentViettelPrefixes() throws Exception {
        String[] viettelPhones = {
                "0981234567", // 098
                "0971234567", // 097
                "0861234567", // 086
                "0351234567", // 035
                "0321234567"  // 032
        };

        for (String phone : viettelPhones) {
            SignUpRequest request = new SignUpRequest(phone, VALID_PASSWORD);

            mockMvc.perform(post(SIGNUP_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            assertThat(signupNotYetLoginRepository.existsByPhoneNumber(phone)).isTrue();
        }
    }
}
