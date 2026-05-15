package com.example.apitestappbackend.MapTest;

import com.example.apitestappbackend.DTO.NodeTest.NodeRequest;
import com.example.apitestappbackend.DTO.NodeTest.NodeResponse;
import com.example.apitestappbackend.ResponseCode;
import com.example.apitestappbackend.models.MapTest;
import com.example.apitestappbackend.models.NodeTest;
import com.example.apitestappbackend.repository.MapTestRepository;
import com.example.apitestappbackend.repository.NodeTestRepository;
import com.example.apitestappbackend.services.NodeTestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("NodeTest Scenario Database Tests - PostgreSQL Integration (hospital_test_2)")
public class NodeTestScenarioDbTest {

    @Autowired
    private NodeTestRepository nodeTestRepository;

    @Autowired
    private MapTestRepository mapTestRepository;

    @Autowired
    private NodeTestService nodeTestService;

    private static final String VALID_BUILDING_CODE = "BLD_NODE_001";
    private static final String VALID_BUILDING_NAME = "Node Test Building";
    private static final String VALID_IMAGE_URL = "https://example.com/node-building.jpg";
    private static final Double VALID_SCALE_X = 1.0;
    private static final Double VALID_SCALE_Y = 1.0;

    private static final Double VALID_X_COORDINATE = 12.5;
    private static final Double VALID_Y_COORDINATE = 34.75;
    private static final String VALID_TYPE = "room";
    private static final Boolean VALID_IS_PASSABLE = true;

    @BeforeEach
    void setUp() {
        nodeTestRepository.deleteAll();
        mapTestRepository.deleteAll();
    }

    private MapTest createAndSaveMapTest() {
        MapTest mapTest = new MapTest();
        mapTest.setBuildingCode(VALID_BUILDING_CODE);
        mapTest.setBuildingName(VALID_BUILDING_NAME);
        mapTest.setImageUrl(VALID_IMAGE_URL);
        mapTest.setScaleX(VALID_SCALE_X);
        mapTest.setScaleY(VALID_SCALE_Y);
        mapTest.setStatus("success");
        mapTest.setCode(ResponseCode.SUCCESS.getCode());
        mapTest.setMessage(ResponseCode.SUCCESS.getMessage());
        mapTest.setUsedInTest(false);
        return mapTestRepository.save(mapTest);
    }

    @Test
    @DisplayName("Testcase 1: Valid request - insert NodeTest into hospital_test_2 should PASS")
    void testInsertNodeTestWithValidRequest() {
        // Arrange
        MapTest savedMap = createAndSaveMapTest();
        NodeRequest request = new NodeRequest(
                savedMap.getId(),
                VALID_X_COORDINATE,
                VALID_Y_COORDINATE,
                VALID_TYPE,
                VALID_IS_PASSABLE
        );

        // Act
        NodeResponse response = nodeTestService.insertNodeTest(request);

        // Assert response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getCode(), "Code should be 1000");
        assertEquals(ResponseCode.SUCCESS.getMessage(), response.getMessage(), "Message should match SUCCESS");
        assertFalse(response.getUsedInTest(), "usedInTest should be false");
        assertNotNull(response.getTimestamp(), "Timestamp should not be null");
        assertNotNull(response.getCreatedAt(), "createdAt should not be null");

        assertNotNull(response.getData(), "Response data should not be null");
        assertNotNull(response.getData().getId(), "Node ID should be generated");
        assertEquals(savedMap.getId(), response.getData().getMapTest().getId(), "Map id should match saved map");
        assertEquals(VALID_X_COORDINATE, response.getData().getXCoordinate(), "X coordinate should match");
        assertEquals(VALID_Y_COORDINATE, response.getData().getYCoordinate(), "Y coordinate should match");
        assertEquals(VALID_TYPE, response.getData().getType(), "Type should match");
        assertEquals(VALID_IS_PASSABLE, response.getData().getIsPassable(), "Passable flag should match");

        // Verify data is saved in real database
        assertEquals(1, nodeTestRepository.count(), "Exactly one NodeTest should be saved");

        NodeTest savedNode = nodeTestRepository.findAll().get(0);
        assertEquals(savedMap.getId(), savedNode.getMapTest().getId(), "Saved node should reference the same map");
        assertEquals(VALID_X_COORDINATE, savedNode.getXCoordinate(), "Saved x coordinate should match");
        assertEquals(VALID_Y_COORDINATE, savedNode.getYCoordinate(), "Saved y coordinate should match");
        assertEquals(VALID_TYPE, savedNode.getType(), "Saved type should match");
        assertEquals(VALID_IS_PASSABLE, savedNode.getIsPassable(), "Saved passable flag should match");
        assertEquals(ResponseCode.SUCCESS.getCode(), savedNode.getCode(), "Saved code should be SUCCESS");
        assertEquals(ResponseCode.SUCCESS.getMessage(), savedNode.getMessage(), "Saved message should be SUCCESS");
        assertEquals("success", savedNode.getStatus(), "Saved status should be success");
        assertNotNull(savedNode.getTimeStamp(), "Saved timestamp should not be null");
        assertNotNull(savedNode.getCreatedAt(), "Saved createdAt should not be null");
    }

    @Test
    @DisplayName("Testcase 2: Missing mapId - should fail and not save NodeTest")
    void testInsertNodeTestWithMissingMapId() {
        // Arrange
        NodeRequest request = new NodeRequest(
                null,
                VALID_X_COORDINATE,
                VALID_Y_COORDINATE,
                VALID_TYPE,
                VALID_IS_PASSABLE
        );

        // Act
        NodeResponse response = nodeTestService.insertNodeTest(request);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals("fail", response.getStatus(), "Status should be fail");
        assertEquals(ResponseCode.MISSING_PARAM.getCode(), response.getCode(), "Code should be 2001");
        assertEquals(ResponseCode.MISSING_PARAM.getMessage(), response.getMessage(), "Message should match MISSING_PARAM");
        assertFalse(response.getUsedInTest(), "usedInTest should be false");
        assertNull(response.getData(), "Data should be null");
        assertEquals(0, nodeTestRepository.count(), "No NodeTest should be saved");
    }
}
