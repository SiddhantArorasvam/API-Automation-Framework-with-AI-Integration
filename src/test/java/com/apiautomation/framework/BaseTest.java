package com.apiautomation.framework;

import com.apiautomation.framework.config.ConfigManager;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base test class providing common setup and utilities
 */
public abstract class BaseTest {
    
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected static final String BASE_URL = ConfigManager.getBaseUrl();
    protected static final int API_TIMEOUT = ConfigManager.getApiTimeout();
    protected static final int MAX_RESPONSE_TIME = ConfigManager.getMaxResponseTime();
    
    @BeforeClass
    public void setUp() {
        logger.info("🚀 Setting up test environment...");
        logger.info("📍 Base URL: {}", BASE_URL);
        logger.info("⏱️  API Timeout: {}ms", API_TIMEOUT);
        logger.info("⚡ Max Response Time: {}ms", MAX_RESPONSE_TIME);
        
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        
        logger.info("✅ Test environment setup completed");
    }
    
    @AfterClass
    public void tearDown() {
        logger.info("🧹 Cleaning up test environment...");
        // Add any cleanup logic here if needed
        logger.info("✅ Test environment cleanup completed");
    }
    
    @Step("Verify response status code")
    protected void verifyStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            logger.error("❌ Status code mismatch. Expected: {}, Actual: {}", expectedStatusCode, actualStatusCode);
            logger.error("Response body: {}", response.getBody().asString());
            throw new AssertionError(String.format("Status code mismatch. Expected: %d, Actual: %d", 
                expectedStatusCode, actualStatusCode));
        }
        logger.info("✅ Status code verified: {}", actualStatusCode);
    }
    
    @Step("Verify response time")
    protected void verifyResponseTime(Response response) {
        long responseTime = response.getTime();
        if (responseTime > MAX_RESPONSE_TIME) {
            logger.warn("⚠️  Response time ({}) exceeds threshold ({})", responseTime, MAX_RESPONSE_TIME);
        } else {
            logger.info("✅ Response time: {}ms (within threshold)", responseTime);
        }
    }
    
    @Step("Log response details")
    protected void logResponseDetails(Response response, String testName) {
        logger.info("📊 Test: {} - Status: {}, Time: {}ms", 
            testName, response.getStatusCode(), response.getTime());
        
        if (response.getStatusCode() >= 400) {
            logger.error("❌ Error response body: {}", response.getBody().asString());
        }
    }
    
    protected String getTestData(String key, String defaultValue) {
        return ConfigManager.getProperty(key, defaultValue);
    }
    
    protected int getTestData(String key, int defaultValue) {
        return ConfigManager.getIntProperty(key, defaultValue);
    }
} 