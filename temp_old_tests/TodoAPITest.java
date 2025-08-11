package com.apiautomation.framework.tests;

import com.apiautomation.framework.api.TodoAPIClient;
import com.apiautomation.framework.utils.AssertionUtils;
import com.apiautomation.framework.utils.PerformanceUtils;
import com.apiautomation.framework.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;

/**
 * Test class for Todo API endpoints
 */
@Epic("Todo API Testing")
@Feature("Todo Management")
public class TodoAPITest {
    private TodoAPIClient todoAPIClient;
    private TestDataGenerator testDataGenerator;
    private AssertionUtils assertionUtils;
    private PerformanceUtils performanceUtils;
    private int createdTodoId;

    @BeforeClass
    public void setUp() {
        todoAPIClient = new TodoAPIClient();
        testDataGenerator = new TestDataGenerator();
        assertionUtils = new AssertionUtils();
        performanceUtils = new PerformanceUtils();
    }

    @Test(description = "Get all todos")
    @Story("Retrieve Todos")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAllTodos() {
        Response response = performanceUtils.measureResponseTime(() -> 
            todoAPIClient.getAllTodos()
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        Assert.assertTrue(todoAPIClient.validateTodoListStructure(response), 
            "Todo list structure validation failed");
    }

    @Test(description = "Get todo by ID")
    @Story("Retrieve Todo by ID")
    @Severity(SeverityLevel.NORMAL)
    public void testGetTodoById() {
        int todoId = 1;
        Response response = performanceUtils.measureResponseTime(() -> 
            todoAPIClient.getTodoById(todoId)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        Assert.assertTrue(todoAPIClient.validateTodoDataStructure(response), 
            "Todo data structure validation failed");
        
        // Verify specific fields
        response.then()
                .assertThat()
                .body("id", org.hamcrest.Matchers.equalTo(todoId))
                .body("userId", org.hamcrest.Matchers.notNullValue())
                .body("title", org.hamcrest.Matchers.notNullValue())
                .body("completed", org.hamcrest.Matchers.notNullValue());
    }

    @Test(description = "Create new todo")
    @Story("Create Todo")
    @Severity(SeverityLevel.HIGH)
    public void testCreateTodo() {
        Map<String, Object> todoData = testDataGenerator.generateTodoData();
        
        Response response = performanceUtils.measureResponseTime(() -> 
            todoAPIClient.createTodo(todoData)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Store created todo ID for cleanup
        createdTodoId = response.jsonPath().getInt("id");
        Assert.assertTrue(createdTodoId > 0, "Todo ID should be positive");
    }

    @Test(description = "Update todo")
    @Story("Update Todo")
    @Severity(SeverityLevel.HIGH)
    @DependsOnMethods("testCreateTodo")
    public void testUpdateTodo() {
        Map<String, Object> updateData = testDataGenerator.generateTodoData();
        
        Response response = performanceUtils.measureResponseTime(() -> 
            todoAPIClient.updateTodo(createdTodoId, updateData)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Verify updated data
        response.then()
                .assertThat()
                .body("id", org.hamcrest.Matchers.equalTo(createdTodoId))
                .body("title", org.hamcrest.Matchers.equalTo(updateData.get("title")));
    }

    @Test(description = "Patch todo")
    @Story("Patch Todo")
    @Severity(SeverityLevel.MEDIUM)
    @DependsOnMethods("testCreateTodo")
    public void testPatchTodo() {
        Map<String, Object> patchData = Map.of("completed", true);
        
        Response response = performanceUtils.measureResponseTime(() -> 
            todoAPIClient.patchTodo(createdTodoId, patchData)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify patched data
        response.then()
                .assertThat()
                .body("id", org.hamcrest.Matchers.equalTo(createdTodoId))
                .body("completed", org.hamcrest.Matchers.equalTo(true));
    }

    @Test(description = "Delete todo")
    @Story("Delete Todo")
    @Severity(SeverityLevel.HIGH)
    @DependsOnMethods("testCreateTodo")
    public void testDeleteTodo() {
        Response response = performanceUtils.measureResponseTime(() -> 
            todoAPIClient.deleteTodo(createdTodoId)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify deletion (API returns 200 for successful deletion)
        response.then()
                .assertThat()
                .statusCode(200);
    }

    @Test(description = "Get todos by user ID")
    @Story("Retrieve Todos by User")
    @Severity(SeverityLevel.NORMAL)
    public void testGetTodosByUserId() {
        int userId = 1;
        Response response = performanceUtils.measureResponseTime(() -> 
            todoAPIClient.getTodosByUserId(userId)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify all todos belong to the specified user
        response.then()
                .assertThat()
                .body("userId", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.equalTo(userId)));
    }

    @Test(description = "Get todos by completion status")
    @Story("Retrieve Todos by Status")
    @Severity(SeverityLevel.NORMAL)
    public void testGetTodosByCompletionStatus() {
        boolean completed = false;
        Response response = performanceUtils.measureResponseTime(() -> 
            todoAPIClient.getTodosByCompletionStatus(completed)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 3000);
        
        // Verify all todos have the specified completion status
        response.then()
                .assertThat()
                .body("completed", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.equalTo(completed)));
    }

    @Test(description = "Create todo with generated test data")
    @Story("Create Todo with Generated Data")
    @Severity(SeverityLevel.MEDIUM)
    public void testCreateTodoWithGeneratedData() {
        Response response = performanceUtils.measureResponseTime(() -> 
            todoAPIClient.createTodoWithGeneratedData()
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Verify todo structure
        Assert.assertTrue(todoAPIClient.validateTodoDataStructure(response), 
            "Generated todo data structure validation failed");
    }

    @Test(description = "Create todo with AI-generated test data")
    @Story("Create Todo with AI Data")
    @Severity(SeverityLevel.MEDIUM)
    public void testCreateTodoWithAIData() {
        String requirements = "Create a todo for project management and team collaboration";
        
        Response response = performanceUtils.measureResponseTime(() -> 
            todoAPIClient.createTodoWithAIData(requirements)
        );
        
        assertionUtils.assertResponseSuccess(response);
        assertionUtils.assertResponseTime(response, 5000);
        
        // Verify todo structure
        Assert.assertTrue(todoAPIClient.validateTodoDataStructure(response), 
            "AI-generated todo data structure validation failed");
    }

    @Test(description = "Test todo completion workflow")
    @Story("Todo Workflow")
    @Severity(SeverityLevel.HIGH)
    public void testTodoCompletionWorkflow() {
        // Create a todo
        Map<String, Object> todoData = testDataGenerator.generateTodoData();
        Response createResponse = todoAPIClient.createTodo(todoData);
        assertionUtils.assertResponseSuccess(createResponse);
        
        int todoId = createResponse.jsonPath().getInt("id");
        
        // Mark as completed
        Map<String, Object> completeData = Map.of("completed", true);
        Response completeResponse = todoAPIClient.patchTodo(todoId, completeData);
        assertionUtils.assertResponseSuccess(completeResponse);
        
        // Verify completion
        completeResponse.then()
                .assertThat()
                .body("completed", org.hamcrest.Matchers.equalTo(true));
        
        // Cleanup
        todoAPIClient.deleteTodo(todoId);
    }

    @Test(description = "Validate todo data types")
    @Story("Data Validation")
    @Severity(SeverityLevel.MEDIUM)
    public void testTodoDataTypes() {
        Response response = todoAPIClient.getAllTodos();
        
        // Verify data types
        response.then()
                .assertThat()
                .body("id", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.instanceOf(Integer.class)))
                .body("userId", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.instanceOf(Integer.class)))
                .body("completed", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.instanceOf(Boolean.class)));
    }

    @Test(description = "Performance test for todo operations")
    @Story("Todo Performance Testing")
    @Severity(SeverityLevel.MEDIUM)
    public void testTodoPerformance() {
        // Test multiple todo operations under load
        for (int i = 0; i < 5; i++) {
            Response response = todoAPIClient.getAllTodos();
            assertionUtils.assertResponseSuccess(response);
            assertionUtils.assertResponseTime(response, 5000);
        }
    }

    @Test(description = "Negative test - Get todo with invalid ID")
    @Story("Negative Testing")
    @Severity(SeverityLevel.LOW)
    public void testGetTodoWithInvalidId() {
        int invalidId = 99999;
        Response response = todoAPIClient.getTodoById(invalidId);
        
        // Should return 404 for non-existent todo
        response.then()
                .assertThat()
                .statusCode(404);
    }

    @Test(description = "Test todo data consistency")
    @Story("Data Consistency")
    @Severity(SeverityLevel.MEDIUM)
    public void testTodoDataConsistency() {
        Response response = todoAPIClient.getAllTodos();
        
        // Verify that all todos have consistent data structure
        response.then()
                .assertThat()
                .body("", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.hasKey("id")))
                .body("", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.hasKey("userId")))
                .body("", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.hasKey("title")))
                .body("", org.hamcrest.Matchers.everyItem(
                    org.hamcrest.Matchers.hasKey("completed")));
    }

    @AfterClass
    public void tearDown() {
        // Cleanup any test data if needed
        if (createdTodoId > 0) {
            try {
                todoAPIClient.deleteTodo(createdTodoId);
            } catch (Exception e) {
                // Log cleanup failure but don't fail the test
                System.out.println("Cleanup failed for todo ID: " + createdTodoId);
            }
        }
    }
} 