package com.apiautomation.framework.tests;

import com.apiautomation.framework.api.UserAPIClient;
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
 * Test class for User API endpoints with AI integration
 */
@Epic("User Management API")
@Feature("User CRUD Operations")
public class UserAPITest {
    private static final Logger logger = LoggerFactory.getLogger(UserAPITest.class);
    private UserAPIClient userAPIClient;
    private TestDataGenerator testDataGenerator;
    private AIService aiService;
    
    @BeforeClass
    public void setUp() {
        userAPIClient = new UserAPIClient();
        testDataGenerator = new TestDataGenerator();
        aiService = new AIService();
        logger.info("User API Test setup completed");
    }
    
    @Test(description = "Get all users and validate response")
    @Story("Get All Users")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to retrieve all users and validate the response structure")
    public void testGetAllUsers() {
        logger.info("Starting test: Get all users");
        
        Response response = userAPIClient.getAllUsers();
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertListNotEmpty(response, "");
        
        // Validate first user structure
        AssertionUtils.assertJsonFieldExists(response, "[0].id");
        AssertionUtils.assertJsonFieldExists(response, "[0].name");
        AssertionUtils.assertJsonFieldExists(response, "[0].email");
        AssertionUtils.assertJsonFieldExists(response, "[0].address");
        AssertionUtils.assertJsonFieldExists(response, "[0].company");
        
        logger.info("Test completed: Get all users");
    }
    
    @Test(description = "Get user by specific ID")
    @Story("Get User by ID")
    @Severity(SeverityLevel.HIGH)
    @Description("Test to retrieve a specific user by ID and validate the response")
    public void testGetUserById() {
        logger.info("Starting test: Get user by ID");
        
        int userId = 1;
        Response response = userAPIClient.getUserById(userId);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertJsonFieldValue(response, "id", userId);
        AssertionUtils.assertStringFieldNotEmpty(response, "name");
        AssertionUtils.assertStringFieldNotEmpty(response, "email");
        
        // Validate user data structure
        boolean isValidStructure = userAPIClient.validateUserDataStructure(response);
        Assert.assertTrue(isValidStructure, "User data structure validation failed");
        
        logger.info("Test completed: Get user by ID");
    }
    
    @Test(description = "Create new user with generated test data")
    @Story("Create User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to create a new user with dynamically generated test data")
    public void testCreateUser() {
        logger.info("Starting test: Create new user");
        
        Map<String, Object> userData = testDataGenerator.generateUserData();
        Response response = userAPIClient.createUser(userData);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 201);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertJsonFieldExists(response, "id");
        AssertionUtils.assertJsonFieldValue(response, "name", userData.get("name"));
        AssertionUtils.assertJsonFieldValue(response, "email", userData.get("email"));
        
        logger.info("Test completed: Create new user");
    }
    
    @Test(description = "Create user with AI-generated test data")
    @Story("AI-Powered Test Data Generation")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test to create a user using AI-generated test data")
    public void testCreateUserWithAIData() {
        logger.info("Starting test: Create user with AI data");
        
        if (!aiService.isAvailable()) {
            logger.warn("AI service not available, skipping test");
            return;
        }
        
        String requirements = "Create a realistic user profile for a software developer";
        Response response = userAPIClient.createUserWithAIData(requirements);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 201);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertJsonFieldExists(response, "id");
        
        logger.info("Test completed: Create user with AI data");
    }
    
    @Test(description = "Update existing user")
    @Story("Update User")
    @Severity(SeverityLevel.HIGH)
    @Description("Test to update an existing user's information")
    public void testUpdateUser() {
        logger.info("Starting test: Update user");
        
        int userId = 1;
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("name", "Updated User Name");
        updateData.put("email", "updated@example.com");
        
        Response response = userAPIClient.updateUser(userId, updateData);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertJsonFieldValue(response, "id", userId);
        AssertionUtils.assertJsonFieldValue(response, "name", "Updated User Name");
        AssertionUtils.assertJsonFieldValue(response, "email", "updated@example.com");
        
        logger.info("Test completed: Update user");
    }
    
    @Test(description = "Patch user with partial data")
    @Story("Patch User")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test to partially update a user using PATCH method")
    public void testPatchUser() {
        logger.info("Starting test: Patch user");
        
        int userId = 1;
        Map<String, Object> patchData = new HashMap<>();
        patchData.put("phone", "+1-555-0123");
        
        Response response = userAPIClient.patchUser(userId, patchData);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertJsonFieldValue(response, "id", userId);
        
        logger.info("Test completed: Patch user");
    }
    
    @Test(description = "Delete user")
    @Story("Delete User")
    @Severity(SeverityLevel.HIGH)
    @Description("Test to delete a user and validate the response")
    public void testDeleteUser() {
        logger.info("Starting test: Delete user");
        
        int userId = 1;
        Response response = userAPIClient.deleteUser(userId);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        
        logger.info("Test completed: Delete user");
    }
    
    @Test(description = "Get user posts")
    @Story("Get User Posts")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test to retrieve posts for a specific user")
    public void testGetUserPosts() {
        logger.info("Starting test: Get user posts");
        
        int userId = 1;
        Response response = userAPIClient.getUserPosts(userId);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertListNotEmpty(response, "");
        
        // Validate post structure
        AssertionUtils.assertJsonFieldExists(response, "[0].id");
        AssertionUtils.assertJsonFieldExists(response, "[0].title");
        AssertionUtils.assertJsonFieldExists(response, "[0].body");
        AssertionUtils.assertJsonFieldValue(response, "[0].userId", userId);
        
        logger.info("Test completed: Get user posts");
    }
    
    @Test(description = "Get user albums")
    @Story("Get User Albums")
    @Severity(SeverityLevel.LOW)
    @Description("Test to retrieve albums for a specific user")
    public void testGetUserAlbums() {
        logger.info("Starting test: Get user albums");
        
        int userId = 1;
        Response response = userAPIClient.getUserAlbums(userId);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertListNotEmpty(response, "");
        
        logger.info("Test completed: Get user albums");
    }
    
    @Test(description = "Get user todos")
    @Story("Get User Todos")
    @Severity(SeverityLevel.LOW)
    @Description("Test to retrieve todos for a specific user")
    public void testGetUserTodos() {
        logger.info("Starting test: Get user todos");
        
        int userId = 1;
        Response response = userAPIClient.getUserTodos(userId);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        AssertionUtils.assertResponseBodyNotEmpty(response);
        AssertionUtils.assertListNotEmpty(response, "");
        
        logger.info("Test completed: Get user todos");
    }
    
    @Test(description = "Test response time performance")
    @Story("Performance Testing")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test to validate API response time is within acceptable limits")
    public void testResponseTime() {
        logger.info("Starting test: Response time validation");
        
        long startTime = System.currentTimeMillis();
        Response response = userAPIClient.getAllUsers();
        long endTime = System.currentTimeMillis();
        
        long responseTime = endTime - startTime;
        long maxResponseTime = 5000; // 5 seconds
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 200);
        Assert.assertTrue(responseTime <= maxResponseTime, 
            "Response time exceeded limit. Actual: " + responseTime + "ms, Max: " + maxResponseTime + "ms");
        
        logger.info("Test completed: Response time validation. Response time: {}ms", responseTime);
    }
    
    @Test(description = "Test with invalid user ID")
    @Story("Negative Testing")
    @Severity(SeverityLevel.MEDIUM)
    @Description("Test to validate API behavior with invalid user ID")
    public void testGetUserWithInvalidId() {
        logger.info("Starting test: Get user with invalid ID");
        
        int invalidUserId = 99999;
        Response response = userAPIClient.getUserById(invalidUserId);
        
        // Assertions
        AssertionUtils.assertStatusCode(response, 404);
        
        logger.info("Test completed: Get user with invalid ID");
    }
    
    @AfterMethod
    public void afterMethod() {
        logger.info("Test method completed");
    }
    
    @AfterClass
    public void tearDown() {
        logger.info("User API Test teardown completed");
    }
} 