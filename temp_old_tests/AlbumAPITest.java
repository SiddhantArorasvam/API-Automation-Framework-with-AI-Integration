package com.apiautomation.framework.tests;

import com.apiautomation.framework.api.AlbumAPIClient;
import com.apiautomation.framework.utils.AssertionUtils;
import com.apiautomation.framework.utils.PerformanceUtils;
import com.apiautomation.framework.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;

/**
 * Test class for Album API endpoints
 */
@Epic("Album API Testing")
@Feature("Album Management")
public class AlbumAPITest {
    private AlbumAPIClient albumAPIClient;
    private TestDataGenerator testDataGenerator;
    private AssertionUtils assertionUtils;
    private PerformanceUtils performanceUtils;
    private int createdAlbumId;

    @BeforeClass
    public void setUp() {
        albumAPIClient = new AlbumAPIClient();
        testDataGenerator = new TestDataGenerator();
        assertionUtils = new AssertionUtils();
        performanceUtils = new PerformanceUtils();
    }

    @Test(description = "Get all albums")
    @Story("Retrieve Albums")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAllAlbums() {
        Response response = performanceUtils.measureResponseTime(() -> 
            albumAPIClient.getAllAlbums()
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        Assert.assertTrue(albumAPIClient.validateAlbumListStructure(response), 
            "Album list structure validation failed");
    }

    @Test(description = "Get album by ID")
    @Story("Retrieve Album by ID")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAlbumById() {
        int albumId = 1;
        Response response = performanceUtils.measureResponseTime(() -> 
            albumAPIClient.getAlbumById(albumId)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        Assert.assertTrue(albumAPIClient.validateAlbumDataStructure(response), 
            "Album data structure validation failed");
        
        // Verify specific fields
        response.then()
                .assertThat()
                .body("id", org.hamcrest.Matchers.equalTo(albumId))
                .body("title", org.hamcrest.Matchers.notNullValue())
                .body("userId", org.hamcrest.Matchers.notNullValue());
    }

    @Test(description = "Create new album")
    @Story("Create Album")
    @Severity(SeverityLevel.HIGH)
    public void testCreateAlbum() {
        Map<String, Object> albumData = testDataGenerator.generateAlbumData();
        
        Response response = performanceUtils.measureResponseTime(() -> 
            albumAPIClient.createAlbum(albumData)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Store created album ID for cleanup
        createdAlbumId = response.jsonPath().getInt("id");
        Assert.assertTrue(createdAlbumId > 0, "Album ID should be positive");
    }

    @Test(description = "Update album")
    @Story("Update Album")
    @Severity(SeverityLevel.HIGH)
    @DependsOnMethods("testCreateAlbum")
    public void testUpdateAlbum() {
        Map<String, Object> updateData = testDataGenerator.generateAlbumData();
        
        Response response = performanceUtils.measureResponseTime(() -> 
            albumAPIClient.updateAlbum(createdAlbumId, updateData)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Verify updated data
        response.then()
                .assertThat()
                .body("id", org.hamcrest.Matchers.equalTo(createdAlbumId))
                .body("title", org.hamcrest.Matchers.equalTo(updateData.get("title")));
    }

    @Test(description = "Patch album")
    @Story("Patch Album")
    @Severity(SeverityLevel.MEDIUM)
    @DependsOnMethods("testCreateAlbum")
    public void testPatchAlbum() {
        Map<String, Object> patchData = Map.of("title", "Patched Album Title");
        
        Response response = performanceUtils.measureResponseTime(() -> 
            albumAPIClient.patchAlbum(createdAlbumId, patchData)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify patched data
        response.then()
                .assertThat()
                .body("id", org.hamcrest.Matchers.equalTo(createdAlbumId))
                .body("title", org.hamcrest.Matchers.equalTo("Patched Album Title"));
    }

    @Test(description = "Get albums by user ID")
    @Story("Retrieve Albums by User")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAlbumsByUserId() {
        int userId = 1;
        Response response = performanceUtils.measureResponseTime(() -> 
            albumAPIClient.getAlbumsByUserId(userId)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify all albums belong to the specified user
        response.then()
                .assertThat()
                .body("userId", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.equalTo(userId)));
    }

    @Test(description = "Get albums by title")
    @Story("Retrieve Albums by Title")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAlbumsByTitle() {
        String title = "quidem molestiae enim";
        Response response = performanceUtils.measureResponseTime(() -> 
            albumAPIClient.getAlbumsByTitle(title)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify all albums have the specified title
        response.then()
                .assertThat()
                .body("title", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.equalTo(title)));
    }

    @Test(description = "Create album with generated test data")
    @Story("Create Album with Generated Data")
    @Severity(SeverityLevel.MEDIUM)
    public void testCreateAlbumWithGeneratedData() {
        Response response = performanceUtils.measureResponseTime(() -> 
            albumAPIClient.createAlbumWithGeneratedData()
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Verify album structure
        Assert.assertTrue(albumAPIClient.validateAlbumDataStructure(response), 
            "Generated album data structure validation failed");
    }

    @Test(description = "Create album with AI-generated test data")
    @Story("Create Album with AI Data")
    @Severity(SeverityLevel.MEDIUM)
    public void testCreateAlbumWithAIData() {
        String requirements = "Create an album with a nature theme and outdoor activities";
        
        Response response = performanceUtils.measureResponseTime(() -> 
            albumAPIClient.createAlbumWithAIData(requirements)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Verify album structure
        Assert.assertTrue(albumAPIClient.validateAlbumDataStructure(response), 
            "AI-generated album data structure validation failed");
    }

    @Test(description = "Get album photos")
    @Story("Retrieve Album Photos")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAlbumPhotos() {
        int albumId = 1;
        Response response = performanceUtils.measureResponseTime(() -> 
            albumAPIClient.getAlbumPhotos(albumId)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify photos structure
        response.then()
                .assertThat()
                .body("", org.hamcrest.Matchers.hasSize(org.hamcrest.Matchers.greaterThan(0)))
                .body("[0].albumId", org.hamcrest.Matchers.equalTo(albumId))
                .body("[0].id", org.hamcrest.Matchers.notNullValue())
                .body("[0].title", org.hamcrest.Matchers.notNullValue())
                .body("[0].url", org.hamcrest.Matchers.notNullValue());
    }

    @Test(description = "Delete album")
    @Story("Delete Album")
    @Severity(SeverityLevel.HIGH)
    @DependsOnMethods("testCreateAlbum")
    public void testDeleteAlbum() {
        Response response = performanceUtils.measureResponseTime(() -> 
            albumAPIClient.deleteAlbum(createdAlbumId)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify deletion (API returns 200 for successful deletion)
        response.then()
                .assertThat()
                .statusCode(200);
    }

    @Test(description = "Performance test for album operations")
    @Story("Album Performance Testing")
    @Severity(SeverityLevel.MEDIUM)
    public void testAlbumPerformance() {
        // Test multiple album operations under load
        for (int i = 0; i < 5; i++) {
            Response response = albumAPIClient.getAllAlbums();
            assertionUtils.assertResponseSuccess(response);
            assertionUtils.assertResponseTime(response, 5000);
        }
    }

    @Test(description = "Negative test - Get album with invalid ID")
    @Story("Negative Testing")
    @Severity(SeverityLevel.LOW)
    public void testGetAlbumWithInvalidId() {
        int invalidId = 99999;
        Response response = albumAPIClient.getAlbumById(invalidId);
        
        // Should return 404 for non-existent album
        response.then()
                .assertThat()
                .statusCode(404);
    }

    @AfterClass
    public void tearDown() {
        // Cleanup any test data if needed
        if (createdAlbumId > 0) {
            try {
                albumAPIClient.deleteAlbum(createdAlbumId);
            } catch (Exception e) {
                // Log cleanup failure but don't fail the test
                System.out.println("Cleanup failed for album ID: " + createdAlbumId);
            }
        }
    }
} 