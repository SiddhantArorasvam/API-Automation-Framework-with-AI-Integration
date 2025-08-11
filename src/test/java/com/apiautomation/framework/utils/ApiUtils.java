package com.apiautomation.framework.utils;

import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Utility class for common API operations and assertions
 */
public class ApiUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(ApiUtils.class);
    
    @Step("Verify JSON response contains field")
    public static void verifyJsonFieldExists(Response response, String jsonPath, String fieldName) {
        try {
            Object value = JsonPath.read(response.getBody().asString(), jsonPath);
            if (value == null) {
                throw new AssertionError("Field '" + fieldName + "' not found at path: " + jsonPath);
            }
            logger.info("✅ Field '{}' found at path '{}': {}", fieldName, jsonPath, value);
        } catch (Exception e) {
            throw new AssertionError("Failed to verify field '" + fieldName + "' at path '" + jsonPath + "': " + e.getMessage());
        }
    }
    
    @Step("Verify JSON response field value")
    public static void verifyJsonFieldValue(Response response, String jsonPath, Object expectedValue, String fieldName) {
        try {
            Object actualValue = JsonPath.read(response.getBody().asString(), jsonPath);
            if (!expectedValue.equals(actualValue)) {
                throw new AssertionError(String.format("Field '%s' value mismatch. Expected: %s, Actual: %s", 
                    fieldName, expectedValue, actualValue));
            }
            logger.info("✅ Field '{}' value verified: {}", fieldName, actualValue);
        } catch (Exception e) {
            throw new AssertionError("Failed to verify field '" + fieldName + "' value at path '" + jsonPath + "': " + e.getMessage());
        }
    }
    
    @Step("Verify JSON array size")
    public static void verifyJsonArraySize(Response response, String jsonPath, int expectedSize, String arrayName) {
        try {
            List<Object> array = JsonPath.read(response.getBody().asString(), jsonPath);
            if (array.size() != expectedSize) {
                throw new AssertionError(String.format("Array '%s' size mismatch. Expected: %d, Actual: %d", 
                    arrayName, expectedSize, array.size()));
            }
            logger.info("✅ Array '{}' size verified: {}", arrayName, array.size());
        } catch (Exception e) {
            throw new AssertionError("Failed to verify array '" + arrayName + "' size at path '" + jsonPath + "': " + e.getMessage());
        }
    }
    
    @Step("Verify JSON response structure")
    public static void verifyJsonStructure(Response response, String... requiredFields) {
        String responseBody = response.getBody().asString();
        
        for (String field : requiredFields) {
            if (!responseBody.contains(field)) {
                throw new AssertionError("Required field '" + field + "' not found in response");
            }
        }
        
        logger.info("✅ JSON structure verified with {} required fields", requiredFields.length);
    }
    
    @Step("Extract value from JSON response")
    public static <T> T extractJsonValue(Response response, String jsonPath, Class<T> type) {
        try {
            T value = JsonPath.read(response.getBody().asString(), jsonPath);
            logger.info("✅ Extracted value from '{}': {}", jsonPath, value);
            return value;
        } catch (Exception e) {
            logger.error("❌ Failed to extract value from '{}': {}", jsonPath, e.getMessage());
            throw new RuntimeException("Failed to extract value from JSON path: " + jsonPath, e);
        }
    }
    
    @Step("Verify response headers")
    public static void verifyResponseHeaders(Response response, Map<String, String> expectedHeaders) {
        for (Map.Entry<String, String> entry : expectedHeaders.entrySet()) {
            String headerName = entry.getKey();
            String expectedValue = entry.getValue();
            String actualValue = response.getHeader(headerName);
            
            if (!expectedValue.equals(actualValue)) {
                throw new AssertionError(String.format("Header '%s' value mismatch. Expected: %s, Actual: %s", 
                    headerName, expectedValue, actualValue));
            }
        }
        
        logger.info("✅ Response headers verified: {}", expectedHeaders.keySet());
    }
    
    @Step("Verify response content type")
    public static void verifyContentType(Response response, String expectedContentType) {
        String actualContentType = response.getContentType();
        if (!actualContentType.contains(expectedContentType)) {
            throw new AssertionError(String.format("Content type mismatch. Expected: %s, Actual: %s", 
                expectedContentType, actualContentType));
        }
        logger.info("✅ Content type verified: {}", actualContentType);
    }
    
    @Step("Generate test data")
    public static String generateTestData(String prefix, String suffix) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + "_" + timestamp + "_" + suffix;
    }
} 