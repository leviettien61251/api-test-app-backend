package com.example.apitestappbackend.MapTest;

import com.example.apitestappbackend.DTO.MapTest.MapTestRequest;
import com.example.apitestappbackend.DTO.MapTest.MapTestResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.MapTest;
import com.example.apitestappbackend.repository.MapTestRepository;
import com.example.apitestappbackend.services.MapTestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("MapTest Scenario Database Tests - hospital_test_2 Integration")
public class MapTestScenarioDbTest {

    @Autowired
    private MapTestRepository mapTestRepository;

    @Autowired
    private MapTestService mapTestService;

    private static final String VALID_BUILDING_CODE = "BLD001";
    private static final String VALID_BUILDING_CODE_2 = "BLD002";
    private static final String VALID_BUILDING_NAME = "Hospital Building A";
    private static final String VALID_BUILDING_NAME_2 = "Hospital Building B";
    private static final String VALID_IMAGE_URL = "https://example.com/building.jpg";
    private static final String INVALID_IMAGE_URL = "not_a_valid_url";
    private static final Double VALID_SCALE_X = 1.5;
    private static final Double VALID_SCALE_Y = 2.0;
    private static final Double INVALID_SCALE_X = Double.NaN;
    private static final Double INVALID_SCALE_Y = Double.NaN;

    @BeforeEach
    void setUp() {
        mapTestRepository.deleteAll();
    }

    private MapTest createAndSaveMapTest(String buildingCode, String buildingName, String imageUrl, Double scaleX, Double scaleY) {
        MapTest mapTest = new MapTest();
        mapTest.setBuildingCode(buildingCode);
        mapTest.setBuildingName(buildingName);
        mapTest.setImageUrl(imageUrl);
        mapTest.setScaleX(scaleX);
        mapTest.setScaleY(scaleY);
        mapTest.setCode(ResponseCode.SUCCESS.getCode());
        mapTest.setMessage(ResponseCode.SUCCESS.getMessage());
        mapTest.setUsedInTest(false);
        return mapTestRepository.save(mapTest);
    }

    @Test
    @DisplayName("Testcase 1: Valid request with all required parameters - Should PASS (Success)")
    void testMapTestWithValidAllParameters() {
        // Arrange
        MapTestRequest request = new MapTestRequest(
                VALID_BUILDING_CODE,
                VALID_BUILDING_NAME,
                VALID_IMAGE_URL,
                VALID_SCALE_X,
                VALID_SCALE_Y
        );

        // Act
        MapTestResponse response = mapTestService.mapTest(request);

        // Assert - Response validation with code and message comparison
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode(), "Response code should be 1000");
        assertEquals(ResponseCode.SUCCESS.getMessage(), response.getMessage(), "Message should match ResponseCode.SUCCESS");
        assertFalse(response.getUsedInTest());
        assertNotNull(response.getData(), "Data should not be null");
        assertNotNull(response.getData().getId(), "ID should be generated");
        assertEquals(VALID_BUILDING_CODE, response.getData().getBuildingCode());
        assertEquals(VALID_BUILDING_NAME, response.getData().getBuildingName());
        assertEquals(VALID_IMAGE_URL, response.getData().getImageUrl());
        assertEquals(VALID_SCALE_X, response.getData().getScaleX());
        assertEquals(VALID_SCALE_Y, response.getData().getScaleY());

        // Verify record is saved in database
        assertTrue(mapTestRepository.findAll().stream()
                .anyMatch(m -> m.getBuildingCode().equals(VALID_BUILDING_CODE)),
                "Record should be saved in maps_test table");
    }

    @Test
    @DisplayName("Testcase 2: Missing building code - Should PASS (Validation fails)")
    void testMapTestWithMissingBuildingCode() {
        // Arrange
        MapTestRequest request = new MapTestRequest(
                "",
                VALID_BUILDING_NAME,
                VALID_IMAGE_URL,
                VALID_SCALE_X,
                VALID_SCALE_Y
        );

        // Act
        MapTestResponse response = mapTestService.mapTest(request);

        // Assert - Compare response code and message with ResponseCode enum
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode(), "Response code should be 2001");
        assertEquals(ResponseCode.MISSING_PARAM.getMessage(), response.getMessage(), "Message should match ResponseCode.MISSING_PARAM");
        assertFalse(response.getUsedInTest());
        assertNull(response.getData(), "Data should be null");

        // Verify no record is saved
        assertTrue(mapTestRepository.findAll().isEmpty(), "No record should be saved for missing building code");
    }

    @Test
    @DisplayName("Testcase 3: Missing building name - Should PASS (Validation fails)")
    void testMapTestWithMissingBuildingName() {
        // Arrange
        MapTestRequest request = new MapTestRequest(
                VALID_BUILDING_CODE,
                "",
                VALID_IMAGE_URL,
                VALID_SCALE_X,
                VALID_SCALE_Y
        );

        // Act
        MapTestResponse response = mapTestService.mapTest(request);

        // Assert - Compare response code and message
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode(), "Response code should be 2001");
        assertEquals(ResponseCode.MISSING_PARAM.getMessage(), response.getMessage());
        assertFalse(response.getUsedInTest());

        // Verify no record is saved
        assertTrue(mapTestRepository.findAll().isEmpty(), "No record should be saved for missing building name");
    }

    @Test
    @DisplayName("Testcase 4: Missing image URL - Should PASS (Validation fails)")
    void testMapTestWithMissingImageUrl() {
        // Arrange
        MapTestRequest request = new MapTestRequest(
                VALID_BUILDING_CODE,
                VALID_BUILDING_NAME,
                "",
                VALID_SCALE_X,
                VALID_SCALE_Y
        );

        // Act
        MapTestResponse response = mapTestService.mapTest(request);

        // Assert - Compare response code and message
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode(), "Response code should be 2001");
        assertEquals(ResponseCode.MISSING_PARAM.getMessage(), response.getMessage());
        assertFalse(response.getUsedInTest());

        // Verify no record is saved
        assertTrue(mapTestRepository.findAll().isEmpty(), "No record should be saved for missing image URL");
    }

    @Test
    @DisplayName("Testcase 5: Invalid image URL format - Should PASS (Validation fails)")
    void testMapTestWithInvalidImageUrl() {
        // Arrange
        MapTestRequest request = new MapTestRequest(
                VALID_BUILDING_CODE,
                VALID_BUILDING_NAME,
                INVALID_IMAGE_URL,
                VALID_SCALE_X,
                VALID_SCALE_Y
        );

        // Act
        MapTestResponse response = mapTestService.mapTest(request);

        // Assert - Compare response code (should be INVALID_VALUE)
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.INVALID_VALUE.getCode(), response.getCode(), "Response code should be 2003");
        assertNotNull(response.getMessage(), "Message should not be null");
        assertFalse(response.getUsedInTest());

        // Verify no record is saved
        assertTrue(mapTestRepository.findAll().isEmpty(), "No record should be saved for invalid image URL");
    }





    @Test
    @DisplayName("Testcase 8: Invalid scale X (NaN) - Should PASS (Validation fails)")
    void testMapTestWithInvalidScaleX() {
        // Arrange
        MapTestRequest request = new MapTestRequest(
                VALID_BUILDING_CODE,
                VALID_BUILDING_NAME,
                VALID_IMAGE_URL,
                INVALID_SCALE_X,
                VALID_SCALE_Y
        );

        // Act
        MapTestResponse response = mapTestService.mapTest(request);

        // Assert - Compare response code and message
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.INVALID_VALUE.getCode(), response.getCode(), "Response code should be 2003");
        assertEquals(ResponseCode.INVALID_VALUE.getMessage(), response.getMessage());
        assertFalse(response.getUsedInTest());

        // Verify no record is saved
        assertTrue(mapTestRepository.findAll().isEmpty(), "No record should be saved for invalid scale X");
    }

    @Test
    @DisplayName("Testcase 9: Invalid scale Y (NaN) - Should PASS (Validation fails)")
    void testMapTestWithInvalidScaleY() {
        // Arrange
        MapTestRequest request = new MapTestRequest(
                VALID_BUILDING_CODE,
                VALID_BUILDING_NAME,
                VALID_IMAGE_URL,
                VALID_SCALE_X,
                INVALID_SCALE_Y
        );

        // Act
        MapTestResponse response = mapTestService.mapTest(request);

        // Assert - Compare response code and message
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.INVALID_VALUE.getCode(), response.getCode(), "Response code should be 2003");
        assertEquals(ResponseCode.INVALID_VALUE.getMessage(), response.getMessage());
        assertFalse(response.getUsedInTest());

        // Verify no record is saved
        assertTrue(mapTestRepository.findAll().isEmpty(), "No record should be saved for invalid scale Y");
    }

    @Test
    @DisplayName("Testcase 10: Valid parameters with whitespace - Should PASS (Trimmed and success)")
    void testMapTestWithWhitespace() {
        // Arrange
        MapTestRequest request = new MapTestRequest(
                "  " + VALID_BUILDING_CODE + "  ",
                "  " + VALID_BUILDING_NAME + "  ",
                "  " + VALID_IMAGE_URL + "  ",
                VALID_SCALE_X,
                VALID_SCALE_Y
        );

        // Act
        MapTestResponse response = mapTestService.mapTest(request);

        // Assert - Response validation
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode(), "Response code should be 1000");
        assertEquals(ResponseCode.SUCCESS.getMessage(), response.getMessage());
        assertFalse(response.getUsedInTest());
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(VALID_BUILDING_CODE, response.getData().getBuildingCode(), "Building code should be trimmed");

        // Verify record is saved with trimmed values
        assertTrue(mapTestRepository.findAll().stream()
                .anyMatch(m -> m.getBuildingCode().equals(VALID_BUILDING_CODE)),
                "Record should be saved with trimmed building code");
    }

    @Test
    @DisplayName("Testcase 11: Multiple successful map tests - Should PASS (Both saved)")
    void testMultipleSuccessfulMapTests() {
        // Arrange - First request
        MapTestRequest request1 = new MapTestRequest(
                VALID_BUILDING_CODE,
                VALID_BUILDING_NAME,
                VALID_IMAGE_URL,
                VALID_SCALE_X,
                VALID_SCALE_Y
        );

        // Act - First map test
        MapTestResponse response1 = mapTestService.mapTest(request1);

        // Assert - First response
        assertEquals("success", response1.getStatus());
        assertEquals(ResponseCode.SUCCESS.getCode(), response1.getCode());
        assertEquals(ResponseCode.SUCCESS.getMessage(), response1.getMessage());
        assertNotNull(response1.getData());
        String firstId = response1.getData().getId();

        // Arrange - Second request
        MapTestRequest request2 = new MapTestRequest(
                VALID_BUILDING_CODE_2,
                VALID_BUILDING_NAME_2,
                VALID_IMAGE_URL,
                VALID_SCALE_X,
                VALID_SCALE_Y
        );

        // Act - Second map test
        MapTestResponse response2 = mapTestService.mapTest(request2);

        // Assert - Second response
        assertEquals("success", response2.getStatus());
        assertEquals(ResponseCode.SUCCESS.getCode(), response2.getCode());
        assertEquals(ResponseCode.SUCCESS.getMessage(), response2.getMessage());
        assertNotNull(response2.getData());
        String secondId = response2.getData().getId();

        // Verify both records are saved in database with correct code and message
        assertNotEquals(firstId, secondId, "IDs should be different");
        assertEquals(2, mapTestRepository.findAll().size(), "Should have 2 records in database");
        mapTestRepository.findAll().forEach(mapTest -> {
            assertEquals(ResponseCode.SUCCESS.getCode(), mapTest.getCode(), "All saved records should have SUCCESS code");
            assertEquals(ResponseCode.SUCCESS.getMessage(), mapTest.getMessage(), "All saved records should have SUCCESS message");
        });
    }

    @Test
    @DisplayName("Testcase 12: Valid parameters with different scale values - Should PASS (Success)")
    void testMapTestWithDifferentScaleValues() {
        // Arrange
        MapTestRequest request = new MapTestRequest(
                VALID_BUILDING_CODE,
                VALID_BUILDING_NAME,
                VALID_IMAGE_URL,
                0.5,
                3.5
        );

        // Act
        MapTestResponse response = mapTestService.mapTest(request);

        // Assert - Compare response code and message
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode());
        assertEquals(ResponseCode.SUCCESS.getMessage(), response.getMessage());
        assertNotNull(response.getData());
        assertEquals(0.5, response.getData().getScaleX());
        assertEquals(3.5, response.getData().getScaleY());

        // Verify record in database has correct code and message
        MapTest savedMapTest = mapTestRepository.findAll().get(0);
        assertEquals(ResponseCode.SUCCESS.getCode(), savedMapTest.getCode());
        assertEquals(ResponseCode.SUCCESS.getMessage(), savedMapTest.getMessage());
    }

    @Test
    @DisplayName("Testcase 13: Missing all parameters - Should PASS (Validation fails)")
    void testMapTestWithAllParametersMissing() {
        // Arrange
        MapTestRequest request = new MapTestRequest(
                "",
                "",
                "",
                null,
                null
        );

        // Act
        MapTestResponse response = mapTestService.mapTest(request);

        // Assert - Compare response code and message (should fail on first validation - building code)
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode());
        assertEquals(ResponseCode.MISSING_PARAM.getMessage(), response.getMessage());
        assertFalse(response.getUsedInTest());

        // Verify no record is saved
        assertTrue(mapTestRepository.findAll().isEmpty(), "No record should be saved");
    }

    @Test
    @DisplayName("Testcase 14: Valid request then verify database record has correct response code and message")
    void testMapTestDatabaseRecordValidation() {
        // Arrange
        MapTestRequest request = new MapTestRequest(
                VALID_BUILDING_CODE,
                VALID_BUILDING_NAME,
                VALID_IMAGE_URL,
                VALID_SCALE_X,
                VALID_SCALE_Y
        );

        // Act
        MapTestResponse response = mapTestService.mapTest(request);

        // Assert response
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode());
        assertEquals(ResponseCode.SUCCESS.getMessage(), response.getMessage());

        // Fetch saved record from database
        MapTest savedRecord = mapTestRepository.findAll().get(0);

        // Assert database record matches ResponseCode enum values
        assertNotNull(savedRecord, "Database record should exist");
        assertEquals(ResponseCode.SUCCESS.getCode(), savedRecord.getCode(), "Database code should match ResponseCode.SUCCESS");
        assertEquals(ResponseCode.SUCCESS.getMessage(), savedRecord.getMessage(), "Database message should match ResponseCode.SUCCESS");
        assertEquals("success", savedRecord.getStatus());
        assertEquals(VALID_BUILDING_CODE, savedRecord.getBuildingCode());
        assertEquals(VALID_BUILDING_NAME, savedRecord.getBuildingName());
        assertNotNull(savedRecord.getCreatedAt());
    }
}
