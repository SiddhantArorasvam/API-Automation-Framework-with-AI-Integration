package com.apiautomation.framework.tests;

import com.apiautomation.framework.api.PostAPIClient;
import com.apiautomation.framework.utils.AssertionUtils;
import com.apiautomation.framework.utils.TestDataGenerator;
import com.apiautomation.framework.ai.AIService;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for Post API endpoints with AI integration
 */
@Epic("Post Management API")
@Feature("Post CRUD Operations")
public class PostAPITest {
    private static final Logger logger = LoggerFactory.getLogger(PostAPITest.class);
    private PostAPIClient postAPIClient;
    private TestDataGenerator testDataGenerator;
    private AIService aiService;
    
    @BeforeClass
    public void setUp() {
        postAPIClient = new PostAPIClient();
        testDataGenerator = new TestDataGenerator();
        aiService = new AIService();
        logger.info("Post API Test setup completed");
    }
    
    @Test(description = "Get all posts and validate response")
    @Story("Get All Posts")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to retrieve all posts and validate the response structure")
    public void testGetAllPosts() {
        logger.info("Starting test: Get all posts");
        
        Response response = postAPIClient.getAllPosts();
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertListNotEmpty(response, "");
        
        // Validate first post structure
        AssertionUtils.assertJsonFieldExists(response, "[0].id");
        AssertionUtils.assertJsonFieldExists(response, "[0].title");
        AssertionUtils.assertJsonFieldExists(response, "[0].body");
        AssertionUtils.assertJsonFieldExists(response, "[0].userId");
        
        // Validate post list structure
        boolean isValidStructure = postAPIClient.validatePostListStructure(response);
        Assert.assertTrue(isValidStructure, "Post list structure validation failed");
        
        logger.info("Test completed: Get all posts");
    }
    
    @Test(description = "Get post by specific ID")
    @Story("Get Post by ID")
    @Severity(SeverityLevel.HIGH)
    @Description("Test to retrieve a specific post by ID and validate the response")
    public void testGetPostById() {
        logger.info("Starting test: Get post by ID");
        
        int postId = 1;
        Response response = postAPIClient.getPostById(postId);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertJsonFieldValue(response, "id", postId);
        AssertionUtils.assertStringFieldNotEmpty(response, "title");
        AssertionUtils.assertStringFieldNotEmpty(response, "body");
        AssertionUtils.assertJsonFieldExists(response, "userId");
        
        // Validate post data structure
        boolean isValidStructure = postAPIClient.validatePostDataStructure(response);
        Assert.assertTrue(isValidStructure, "Post data structure validation failed");
        
        logger.info("Test completed: Get post by ID");
    }
    
    @Test(description = "Create new post with generated test data")
    @Story("Create Post")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to create a new post with dynamically generated test data")
    public void testCreatePost() {
        logger.info("Starting test: Create new post");
        
        Map<String, Object> postData = testDataGenerator.generatePostData();
        Response response = postAPIClient.createPost(postData);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 201);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertJsonFieldExists(response, "id");
        AssertionUtils.assertJsonFieldValue(response, "title", postData.get("title"));
        AssertionUtils.assertJsonFieldValue(response, "body", postData.get("body"));
        AssertionUtils.assertJsonFieldValue(response, "userId", postData.get("userId"));
        
        logger.info("Test completed: Create new post");
    }
    
    @Test(description = "Create post with AI-generated test data")
    @Story("AI-Powered Test Data Generation")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test to create a post using AI-generated test data")
    public void testCreatePostWithAIData() {
        logger.info("Starting test: Create post with AI data");
        
        if (!aiService.isAvailable()) {
            logger.warn("AI service not available, skipping test");
            return;
        }
        
        String requirements = "Create a realistic blog post about software testing best practices";
        Response response = postAPIClient.createPostWithAIData(requirements);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 201);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertJsonFieldExists(response, "id");
        
        logger.info("Test completed: Create post with AI data");
    }
    
    @Test(description = "Update existing post")
    @Story("Update Post")
    @Severity(SeverityLevel.HIGH)
    @Description("Test to update an existing post's information")
    public void testUpdatePost() {
        logger.info("Starting test: Update post");
        
        int postId = 1;
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("title", "Updated Post Title");
        updateData.put("body", "This is the updated post content with new information.");
        
        Response response = postAPIClient.updatePost(postId, updateData);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertJsonFieldValue(response, "id", postId);
        AssertionUtils.assertJsonFieldValue(response, "title", "Updated Post Title");
        AssertionUtils.assertJsonFieldValue(response, "body", "This is the updated post content with new information.");
        
        logger.info("Test completed: Update post");
    }
    
    @Test(description = "Patch post with partial data")
    @Story("Patch Post")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test to partially update a post using PATCH method")
    public void testPatchPost() {
        logger.info("Starting test: Patch post");
        
        int postId = 1;
        Map<String, Object> patchData = new HashMap<>();
        patchData.put("title", "Patched Post Title");
        
        Response response = postAPIClient.patchPost(postId, patchData);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertJsonFieldValue(response, "id", postId);
        AssertionUtils.assertJsonFieldValue(response, "title", "Patched Post Title");
        
        logger.info("Test completed: Patch post");
    }
    
    @Test(description = "Delete post")
    @Story("Delete Post")
    @Severity(SeverityLevel.HIGH)
    @Description("Test to delete a post and validate the response")
    public void testDeletePost() {
        logger.info("Starting test: Delete post");
        
        int postId = 1;
        Response response = postAPIClient.deletePost(postId);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        
        logger.info("Test completed: Delete post");
    }
    
    @Test(description = "Get posts by user ID")
    @Story("Get Posts by User")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test to retrieve posts for a specific user")
    public void testGetPostsByUserId() {
        logger.info("Starting test: Get posts by user ID");
        
        int userId = 1;
        Response response = postAPIClient.getPostsByUserId(userId);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertListNotEmpty(response, "");
        
        // Validate all posts belong to the specified user
        AssertionUtils.assertJsonFieldValue(response, "[0].userId", userId);
        
        logger.info("Test completed: Get posts by user ID");
    }
    
    @Test(description = "Get posts by title")
    @Story("Get Posts by Title")
    @Severity(SeverityLevel.LOW)
    @Description("Test to retrieve posts by title search")
    public void testGetPostsByTitle() {
        logger.info("Starting test: Get posts by title");
        
        String title = "qui est esse";
        Response response = postAPIClient.getPostsByTitle(title);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        
        logger.info("Test completed: Get posts by title");
    }
    
    @Test(description = "Get post comments")
    @Story("Get Post Comments")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test to retrieve comments for a specific post")
    public void testGetPostComments() {
        logger.info("Starting test: Get post comments");
        
        int postId = 1;
        Response response = postAPIClient.getPostComments(postId);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertListNotEmpty(response, "");
        
        // Validate comment structure
        AssertionUtils.assertJsonFieldExists(response, "[0].id");
        AssertionUtils.assertJsonFieldExists(response, "[0].postId");
        AssertionUtils.assertJsonFieldExists(response, "[0].name");
        AssertionUtils.assertJsonFieldExists(response, "[0].email");
        AssertionUtils.assertJsonFieldExists(response, "[0].body");
        
        // Validate comment belongs to the specified post
        AssertionUtils.assertJsonFieldValue(response, "[0].postId", postId);
        
        logger.info("Test completed: Get post comments");
    }
    
    @Test(description = "Test response time performance")
    @Story("Performance Testing")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test to validate API response time is within acceptable limits")
    public void testResponseTime() {
        logger.info("Starting test: Response time validation");
        
        long startTime = System.currentTimeMillis();
        Response response = postAPIClient.getAllPosts();
        long endTime = System.currentTimeMillis();
        
        long responseTime = endTime - startTime;
        long maxResponseTime = 5000; // 5 seconds
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        Assert.assertTrue(responseTime <= maxResponseTime, 
            "Response time exceeded limit. Actual: " + responseTime + "ms, Max: " + maxResponseTime + "ms");
        
        logger.info("Test completed: Response time validation. Response time: {}ms", responseTime);
    }
    
    @Test(description = "Test with invalid post ID")
    @Story("Negative Testing")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test to validate API behavior with invalid post ID")
    public void testGetPostWithInvalidId() {
        logger.info("Starting test: Get post with invalid ID");
        
        int invalidPostId = 99999;
        Response response = postAPIClient.getPostById(invalidPostId);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 404);
        
        logger.info("Test completed: Get post with invalid ID");
    }
    
    @Test(description = "Test post creation with minimal data")
    @Story("Edge Case Testing")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test to create a post with minimal required data")
    public void testCreatePostWithMinimalData() {
        logger.info("Starting test: Create post with minimal data");
        
        Map<String, Object> minimalData = new HashMap<>();
        minimalData.put("title", "Minimal Post");
        minimalData.put("body", "Minimal content");
        minimalData.put("userId", 1);
        
        Response response = postAPIClient.createPost(minimalData);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 201);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertJsonFieldExists(response, "id");
        AssertionUtils.assertJsonFieldValue(response, "title", "Minimal Post");
        AssertionUtils.assertJsonFieldValue(response, "body", "Minimal content");
        AssertionUtils.assertJsonFieldValue(response, "userId", 1);
        
        logger.info("Test completed: Create post with minimal data");
    }
    
    @Test(description = "Test post creation with maximum data")
    @Story("Edge Case Testing")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test to create a post with comprehensive data")
    public void testCreatePostWithMaximumData() {
        logger.info("Starting test: Create post with maximum data");
        
        Map<String, Object> maxData = testDataGenerator.generatePostData();
        maxData.put("tags", new String[]{"test", "automation", "api"});
        maxData.put("category", "Technology");
        maxData.put("published", true);
        
        Response response = postAPIClient.createPost(maxData);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 201);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertJsonFieldExists(response, "id");
        
        logger.info("Test completed: Create post with maximum data");
    }
    
    @AfterMethod
    public void afterMethod() {
        logger.info("Test method completed");
    }
    
    @AfterClass
    public void tearDown() {
        logger.info("Post API Test teardown completed");
    }
} 