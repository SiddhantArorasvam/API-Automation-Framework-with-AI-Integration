package com.apiautomation.framework.tests;

import com.apiautomation.framework.api.PhotoAPIClient;
import com.apiautomation.framework.utils.AssertionUtils;
import com.apiautomation.framework.utils.PerformanceUtils;
import com.apiautomation.framework.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;

/**
 * Test class for Photo API endpoints
 */
@Epic("Photo API Testing")
@Feature("Photo Management")
public class PhotoAPITest {
    private PhotoAPIClient photoAPIClient;
    private TestDataGenerator testDataGenerator;
    private AssertionUtils assertionUtils;
    private PerformanceUtils performanceUtils;
    private int createdPhotoId;

    @BeforeClass
    public void setUp() {
        photoAPIClient = new PhotoAPIClient();
        testDataGenerator = new TestDataGenerator();
        assertionUtils = new AssertionUtils();
        performanceUtils = new PerformanceUtils();
    }

    @Test(description = "Get all photos")
    @Story("Retrieve Photos")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAllPhotos() {
        Response response = performanceUtils.measureResponseTime(() -> 
            photoAPIClient.getAllPhotos()
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        Assert.assertTrue(photoAPIClient.validatePhotoListStructure(response), 
            "Photo list structure validation failed");
    }

    @Test(description = "Get photo by ID")
    @Story("Retrieve Photo by ID")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPhotoById() {
        int photoId = 1;
        Response response = performanceUtils.measureResponseTime(() -> 
            photoAPIClient.getPhotoById(photoId)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        Assert.assertTrue(photoAPIClient.validatePhotoDataStructure(response), 
            "Photo data structure validation failed");
        
        // Verify specific fields
        response.then()
                .assertThat()
                .body("id", org.hamcrest.Matchers.equalTo(photoId))
                .body("albumId", org.hamcrest.Matchers.notNullValue())
                .body("title", org.hamcrest.Matchers.notNullValue())
                .body("url", org.hamcrest.Matchers.notNullValue())
                .body("thumbnailUrl", org.hamcrest.Matchers.notNullValue());
    }

    @Test(description = "Create new photo")
    @Story("Create Photo")
    @Severity(SeverityLevel.HIGH)
    public void testCreatePhoto() {
        Map<String, Object> photoData = testDataGenerator.generatePhotoData();
        
        Response response = performanceUtils.measureResponseTime(() -> 
            photoAPIClient.createPhoto(photoData)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Store created photo ID for cleanup
        createdPhotoId = response.jsonPath().getInt("id");
        Assert.assertTrue(createdPhotoId > 0, "Photo ID should be positive");
    }

    @Test(description = "Update photo")
    @Story("Update Photo")
    @Severity(SeverityLevel.HIGH)
    @DependsOnMethods("testCreatePhoto")
    public void testUpdatePhoto() {
        Map<String, Object> updateData = testDataGenerator.generatePhotoData();
        
        Response response = performanceUtils.measureResponseTime(() -> 
            photoAPIClient.updatePhoto(createdPhotoId, updateData)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Verify updated data
        response.then()
                .assertThat()
                .body("id", org.hamcrest.Matchers.equalTo(createdPhotoId))
                .body("title", org.hamcrest.Matchers.equalTo(updateData.get("title")));
    }

    @Test(description = "Patch photo")
    @Story("Patch Photo")
    @Severity(SeverityLevel.MEDIUM)
    @DependsOnMethods("testCreatePhoto")
    public void testPatchPhoto() {
        Map<String, Object> patchData = Map.of("title", "Patched Photo Title");
        
        Response response = performanceUtils.measureResponseTime(() -> 
            photoAPIClient.patchPhoto(createdPhotoId, patchData)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify patched data
        response.then()
                .assertThat()
                .body("id", org.hamcrest.Matchers.equalTo(createdPhotoId))
                .body("title", org.hamcrest.Matchers.equalTo("Patched Photo Title"));
    }

    @Test(description = "Delete photo")
    @Story("Delete Photo")
    @Severity(SeverityLevel.HIGH)
    @DependsOnMethods("testCreatePhoto")
    public void testDeletePhoto() {
        Response response = performanceUtils.measureResponseTime(() -> 
            photoAPIClient.deletePhoto(createdPhotoId)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify deletion (API returns 200 for successful deletion)
        response.then()
                .assertThat()
                .statusCode(200);
    }

    @Test(description = "Get photos by album ID")
    @Story("Retrieve Photos by Album")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPhotosByAlbumId() {
        int albumId = 1;
        Response response = performanceUtils.measureResponseTime(() -> 
            photoAPIClient.getPhotosByAlbumId(albumId)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify all photos belong to the specified album
        response.then()
                .assertThat()
                .body("albumId", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.equalTo(albumId)));
    }

    @Test(description = "Get photos by title")
    @Story("Retrieve Photos by Title")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPhotosByTitle() {
        String title = "accusamus beatae ad facilis cum similique qui sunt";
        Response response = performanceUtils.measureResponseTime(() -> 
            photoAPIClient.getPhotosByTitle(title)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify all photos have the specified title
        response.then()
                .assertThat()
                .body("title", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.equalTo(title)));
    }

    @Test(description = "Create photo with generated test data")
    @Story("Create Photo with Generated Data")
    @Severity(SeverityLevel.MEDIUM)
    public void testCreatePhotoWithGeneratedData() {
        Response response = performanceUtils.measureResponseTime(() -> 
            photoAPIClient.createPhotoWithGeneratedData()
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Verify photo structure
        Assert.assertTrue(photoAPIClient.validatePhotoDataStructure(response), 
            "Generated photo data structure validation failed");
    }

    @Test(description = "Create photo with AI-generated test data")
    @Story("Create Photo with AI Data")
    @Severity(SeverityLevel.MEDIUM)
    public void testCreatePhotoWithAIData() {
        String requirements = "Create a photo with nature landscape and wildlife theme";
        
        Response response = performanceUtils.measureResponseTime(() -> 
            photoAPIClient.createPhotoWithAIData(requirements)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Verify photo structure
        Assert.assertTrue(photoAPIClient.validatePhotoDataStructure(response), 
            "AI-generated photo data structure validation failed");
    }

    @Test(description = "Validate photo URL format")
    @Story("Data Validation")
    @Severity(SeverityLevel.MEDIUM)
    public void testPhotoUrlFormat() {
        Response response = photoAPIClient.getAllPhotos();
        
        // Verify URL format for all photos
        response.then()
                .assertThat()
                .body("url", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.matchesPattern("^https?://.*")));
        
        // Verify thumbnail URL format
        response.then()
                .assertThat()
                .body("thumbnailUrl", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.matchesPattern("^https?://.*")));
    }

    @Test(description = "Performance test for photo operations")
    @Story("Photo Performance Testing")
    @Severity(SeverityLevel.MEDIUM)
    public void testPhotoPerformance() {
        // Test multiple photo operations under load
        for (int i = 0; i < 5; i++) {
            Response response = photoAPIClient.getAllPhotos();
            assertionUtils.assertResponseSuccess(response);
            assertionUtils.assertResponseTime(response, 5000);
        }
    }

    @Test(description = "Negative test - Get photo with invalid ID")
    @Story("Negative Testing")
    @Severity(SeverityLevel.LOW)
    public void testGetPhotoWithInvalidId() {
        int invalidId = 99999;
        Response response = photoAPIClient.getPhotoById(invalidId);
        
        // Should return 404 for non-existent photo
        response.then()
                .assertThat()
                .statusCode(404);
    }

    @Test(description = "Test photo data consistency")
    @Story("Data Consistency")
    @Severity(SeverityLevel.MEDIUM)
    public void testPhotoDataConsistency() {
        Response response = photoAPIClient.getAllPhotos();
        
        // Verify that all photos have consistent data structure
        response.then()
                .assertThat()
                .body("", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.hasKey("id")))
                .body("", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.hasKey("albumId")))
                .body("", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.hasKey("title")))
                .body("", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.hasKey("url")))
                .body("", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.hasKey("thumbnailUrl")));
    }

    @AfterClass
    public void tearDown() {
        // Cleanup any test data if needed
        if (createdPhotoId > 0) {
            try {
                photoAPIClient.deletePhoto(createdPhotoId);
            } catch (Exception e) {
                // Log cleanup failure but don't fail the test
                System.out.println("Cleanup failed for photo ID: " + createdPhotoId);
            }
        }
    }
} 