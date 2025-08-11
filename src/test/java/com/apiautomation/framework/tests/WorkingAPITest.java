package com.apiautomation.framework.tests;

import com.apiautomation.framework.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@Epic("API Testing Framework")
@Feature("JSONPlaceholder API Testing")
public class WorkingAPITest extends BaseTest {

    @Test
    @Story("Get Users")
    @Description("Test GET /users endpoint to retrieve all users")
    public void testGetUsers() {
        Response response = io.restassured.RestAssured.given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .extract().response();

        assertNotNull(response, "Response should not be null");
        verifyStatusCode(response, 200);
        verifyResponseTime(response);
        logResponseDetails(response, "testGetUsers");
        
        // Verify response structure
        String responseBody = response.getBody().asString();
        assertTrue(responseBody.contains("id"), "Response should contain 'id' field");
        assertTrue(responseBody.contains("name"), "Response should contain 'name' field");
        assertTrue(responseBody.contains("email"), "Response should contain 'email' field");
        
        logger.info("✅ GET /users test passed!");
    }

    @Test
    @Story("Get Specific User")
    @Description("Test GET /users/{id} endpoint to retrieve a specific user")
    public void testGetSpecificUser() {
        Response response = io.restassured.RestAssured.given()
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .extract().response();

        assertNotNull(response, "Response should not be null");
        verifyStatusCode(response, 200);
        verifyResponseTime(response);
        logResponseDetails(response, "testGetSpecificUser");
        
        // Parse JSON response
        String responseBody = response.getBody().asString();
        logger.info("Response body: {}", responseBody);
        
        // Use more flexible JSON validation
        assertTrue(responseBody.contains("id"), "Response should contain 'id' field");
        assertTrue(responseBody.contains("name"), "Response should contain 'name' field");
        assertTrue(responseBody.contains("email"), "Response should contain 'email' field");
        
        // Check if it's a valid JSON structure
        assertTrue(responseBody.startsWith("{"), "Response should start with {");
        assertTrue(responseBody.endsWith("}"), "Response should end with }");
        
        logger.info("✅ GET /users/1 test passed!");
    }

    @Test
    @Story("Get Posts")
    @Description("Test GET /posts endpoint to retrieve all posts")
    public void testGetPosts() {
        Response response = io.restassured.RestAssured.given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract().response();

        assertNotNull(response, "Response should not be null");
        verifyStatusCode(response, 200);
        verifyResponseTime(response);
        logResponseDetails(response, "testGetPosts");
        
        // Verify response structure
        String responseBody = response.getBody().asString();
        assertTrue(responseBody.contains("id"), "Response should contain 'id' field");
        assertTrue(responseBody.contains("title"), "Response should contain 'title' field");
        assertTrue(responseBody.contains("body"), "Response should contain 'body' field");
        
        logger.info("✅ GET /posts test passed!");
    }

    @Test
    @Story("Create Post")
    @Description("Test POST /posts endpoint to create a new post")
    public void testCreatePost() {
        String postData = "{\n" +
                "    \"title\": \"Test Post\",\n" +
                "    \"body\": \"This is a test post\",\n" +
                "    \"userId\": 1\n" +
                "}";

        Response response = io.restassured.RestAssured.given()
                .contentType("application/json")
                .body(postData)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .extract().response();

        assertNotNull(response, "Response should not be null");
        verifyStatusCode(response, 201);
        verifyResponseTime(response);
        logResponseDetails(response, "testCreatePost");
        
        // Verify response structure
        String responseBody = response.getBody().asString();
        assertTrue(responseBody.contains("id"), "Response should contain 'id' field");
        assertTrue(responseBody.contains("Test Post"), "Response should contain the posted title");
        assertTrue(responseBody.contains("This is a test post"), "Response should contain the posted body");
        
        logger.info("✅ POST /posts test passed!");
    }

    @Test
    @Story("Update Post")
    @Description("Test PUT /posts/{id} endpoint to update an existing post")
    public void testUpdatePost() {
        String updateData = "{\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"Updated Post\",\n" +
                "    \"body\": \"This post has been updated\",\n" +
                "    \"userId\": 1\n" +
                "}";

        Response response = io.restassured.RestAssured.given()
                .contentType("application/json")
                .body(updateData)
                .when()
                .put("/posts/1")
                .then()
                .statusCode(200)
                .extract().response();

        assertNotNull(response, "Response should not be null");
        verifyStatusCode(response, 200);
        verifyResponseTime(response);
        logResponseDetails(response, "testUpdatePost");
        
        // Verify response structure
        String responseBody = response.getBody().asString();
        logger.info("Update response body: {}", responseBody);
        
        // Use more flexible JSON validation
        assertTrue(responseBody.contains("id"), "Response should contain 'id' field");
        assertTrue(responseBody.contains("Updated Post"), "Response should contain the updated title");
        assertTrue(responseBody.contains("This post has been updated"), "Response should contain the updated body");
        
        // Check if it's a valid JSON structure
        assertTrue(responseBody.startsWith("{"), "Response should start with {");
        assertTrue(responseBody.endsWith("}"), "Response should end with }");
        
        logger.info("✅ PUT /posts/1 test passed!");
    }

    @Test
    @Story("Delete Post")
    @Description("Test DELETE /posts/{id} endpoint to delete a post")
    public void testDeletePost() {
        Response response = io.restassured.RestAssured.given()
                .when()
                .delete("/posts/1")
                .then()
                .statusCode(200)
                .extract().response();

        assertNotNull(response, "Response should not be null");
        verifyStatusCode(response, 200);
        verifyResponseTime(response);
        logResponseDetails(response, "testDeletePost");
        
        logger.info("✅ DELETE /posts/1 test passed!");
    }
} 