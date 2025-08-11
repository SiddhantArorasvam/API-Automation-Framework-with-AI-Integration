package com.apiautomation.framework.tests;

import com.apiautomation.framework.api.CommentAPIClient;
import com.apiautomation.framework.utils.AssertionUtils;
import com.apiautomation.framework.utils.PerformanceUtils;
import com.apiautomation.framework.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;

/**
 * Test class for Comment API endpoints
 */
@Epic("Comment API Testing")
@Feature("Comment Management")
public class CommentAPITest {
    private CommentAPIClient commentAPIClient;
    private TestDataGenerator testDataGenerator;
    private AssertionUtils assertionUtils;
    private PerformanceUtils performanceUtils;
    private int createdCommentId;

    @BeforeClass
    public void setUp() {
        commentAPIClient = new CommentAPIClient();
        testDataGenerator = new TestDataGenerator();
        assertionUtils = new AssertionUtils();
        performanceUtils = new PerformanceUtils();
    }

    @Test(description = "Get all comments")
    @Story("Retrieve Comments")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAllComments() {
        Response response = performanceUtils.measureResponseTime(() -> 
            commentAPIClient.getAllComments()
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        Assert.assertTrue(commentAPIClient.validateCommentListStructure(response), 
            "Comment list structure validation failed");
    }

    @Test(description = "Get comment by ID")
    @Story("Retrieve Comment by ID")
    @Severity(SeverityLevel.NORMAL)
    public void testGetCommentById() {
        int commentId = 1;
        Response response = performanceUtils.measureResponseTime(() -> 
            commentAPIClient.getCommentById(commentId)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        Assert.assertTrue(commentAPIClient.validateCommentDataStructure(response), 
            "Comment data structure validation failed");
        
        // Verify specific fields
        response.then()
                .assertThat()
                .body("id", org.hamcrest.Matchers.equalTo(commentId))
                .body("postId", org.hamcrest.Matchers.notNullValue())
                .body("name", org.hamcrest.Matchers.notNullValue())
                .body("email", org.hamcrest.Matchers.notNullValue())
                .body("body", org.hamcrest.Matchers.notNullValue());
    }

    @Test(description = "Create new comment")
    @Story("Create Comment")
    @Severity(SeverityLevel.HIGH)
    public void testCreateComment() {
        Map<String, Object> commentData = testDataGenerator.generateCommentData();
        
        Response response = performanceUtils.measureResponseTime(() -> 
            commentAPIClient.createComment(commentData)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Store created comment ID for cleanup
        createdCommentId = response.jsonPath().getInt("id");
        Assert.assertTrue(createdCommentId > 0, "Comment ID should be positive");
    }

    @Test(description = "Update comment")
    @Story("Update Comment")
    @Severity(SeverityLevel.HIGH)
    @DependsOnMethods("testCreateComment")
    public void testUpdateComment() {
        Map<String, Object> updateData = testDataGenerator.generateCommentData();
        
        Response response = performanceUtils.measureResponseTime(() -> 
            commentAPIClient.updateComment(createdCommentId, updateData)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Verify updated data
        response.then()
                .assertThat()
                .body("id", org.hamcrest.Matchers.equalTo(createdCommentId))
                .body("body", org.hamcrest.Matchers.equalTo(updateData.get("body")));
    }

    @Test(description = "Patch comment")
    @Story("Patch Comment")
    @Severity(SeverityLevel.MEDIUM)
    @DependsOnMethods("testCreateComment")
    public void testPatchComment() {
        Map<String, Object> patchData = Map.of("body", "Patched comment body");
        
        Response response = performanceUtils.measureResponseTime(() -> 
            commentAPIClient.patchComment(createdCommentId, patchData)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify patched data
        response.then()
                .assertThat()
                .body("id", org.hamcrest.Matchers.equalTo(createdCommentId))
                .body("body", org.hamcrest.Matchers.equalTo("Patched comment body"));
    }

    @Test(description = "Delete comment")
    @Story("Delete Comment")
    @Severity(SeverityLevel.HIGH)
    @DependsOnMethods("testCreateComment")
    public void testDeleteComment() {
        Response response = performanceUtils.measureResponseTime(() -> 
            commentAPIClient.deleteComment(createdCommentId)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify deletion (API returns 200 for successful deletion)
        response.then()
                .assertThat()
                .statusCode(200);
    }

    @Test(description = "Get comments by post ID")
    @Story("Retrieve Comments by Post")
    @Severity(SeverityLevel.NORMAL)
    public void testGetCommentsByPostId() {
        int postId = 1;
        Response response = performanceUtils.measureResponseTime(() -> 
            commentAPIClient.getCommentsByPostId(postId)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify all comments belong to the specified post
        response.then()
                .assertThat()
                .body("postId", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.equalTo(postId)));
    }

    @Test(description = "Get comments by email")
    @Story("Retrieve Comments by Email")
    @Severity(SeverityLevel.NORMAL)
    public void testGetCommentsByEmail() {
        String email = "Eliseo@gardner.biz";
        Response response = performanceUtils.measureResponseTime(() -> 
            commentAPIClient.getCommentsByEmail(email)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify all comments have the specified email
        response.then()
                .assertThat()
                .body("email", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.equalTo(email)));
    }

    @Test(description = "Create comment with generated test data")
    @Story("Create Comment with Generated Data")
    @Severity(SeverityLevel.MEDIUM)
    public void testCreateCommentWithGeneratedData() {
        Response response = performanceUtils.measureResponseTime(() -> 
            commentAPIClient.createCommentWithGeneratedData()
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Verify comment structure
        Assert.assertTrue(commentAPIClient.validateCommentDataStructure(response), 
            "Generated comment data structure validation failed");
    }

    @Test(description = "Create comment with AI-generated test data")
    @Story("Create Comment with AI Data")
    @Severity(SeverityLevel.MEDIUM)
    public void testCreateCommentWithAIData() {
        String requirements = "Create a comment about technology and innovation";
        
        Response response = performanceUtils.measureResponseTime(() -> 
            commentAPIClient.createCommentWithAIData(requirements)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Verify comment structure
        Assert.assertTrue(commentAPIClient.validateCommentDataStructure(response), 
            "AI-generated comment data structure validation failed");
    }

    @Test(description = "Performance test for comment operations")
    @Story("Comment Performance Testing")
    @Severity(SeverityLevel.MEDIUM)
    public void testCommentPerformance() {
        // Test multiple comment operations under load
        for (int i = 0; i < 5; i++) {
            Response response = commentAPIClient.getAllComments();
            assertionUtils.assertResponseSuccess(response);
            assertionUtils.assertResponseTime(response, 5000);
        }
    }

    @Test(description = "Negative test - Get comment with invalid ID")
    @Story("Negative Testing")
    @Severity(SeverityLevel.LOW)
    public void testGetCommentWithInvalidId() {
        int invalidId = 99999;
        Response response = commentAPIClient.getCommentById(invalidId);
        
        // Should return 404 for non-existent comment
        response.then()
                .assertThat()
                .statusCode(404);
    }

    @Test(description = "Validate comment email format")
    @Story("Data Validation")
    @Severity(SeverityLevel.MEDIUM)
    public void testCommentEmailFormat() {
        Response response = commentAPIClient.getAllComments();
        
        // Verify email format for all comments
        response.then()
                .assertThat()
                .body("email", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.matchesPattern("^[A-Za-z0-9+_.-]+@(.+)$")));
    }

    @AfterClass
    public void tearDown() {
        // Cleanup any test data if needed
        if (createdCommentId > 0) {
            try {
                commentAPIClient.deleteComment(createdCommentId);
            } catch (Exception e) {
                // Log cleanup failure but don't fail the test
                System.out.println("Cleanup failed for comment ID: " + createdCommentId);
            }
        }
    }
} 