package com.apiautomation.framework.tests;

import com.apiautomation.framework.utils.PerformanceUtils;
import io.qameta.allure.*;
import org.testng.annotations.*;

/**
 * Test Suite for running all API tests together
 */
@Epic("Complete API Test Suite")
@Feature("All API Endpoints")
public class TestSuite {
    
    private PerformanceUtils performanceUtils;
    
    @BeforeClass
    public void setUp() {
        performanceUtils = new PerformanceUtils();
    }
    
    @Test(description = "Run all user API tests")
    @Story("User API Test Suite")
    @Severity(SeverityLevel.CRITICAL)
    public void testUserAPISuite() {
        // This will be executed by TestNG based on the testng.xml configuration
        // Individual test methods are defined in UserAPITest class
    }
    
    @Test(description = "Run all post API tests")
    @Story("Post API Test Suite")
    @Severity(SeverityLevel.CRITICAL)
    public void testPostAPISuite() {
        // This will be executed by TestNG based on the testng.xml configuration
        // Individual test methods are defined in PostAPITest class
    }
    
    @Test(description = "Run all album API tests")
    @Story("Album API Test Suite")
    @Severity(SeverityLevel.HIGH)
    public void testAlbumAPISuite() {
        // This will be executed by TestNG based on the testng.xml configuration
        // Individual test methods are defined in AlbumAPITest class
    }
    
    @Test(description = "Run all comment API tests")
    @Story("Comment API Test Suite")
    @Severity(SeverityLevel.HIGH)
    public void testCommentAPISuite() {
        // This will be executed by TestNG based on the testng.xml configuration
        // Individual test methods are defined in CommentAPITest class
    }
    
    @Test(description = "Run all photo API tests")
    @Story("Photo API Test Suite")
    @Severity(SeverityLevel.HIGH)
    public void testPhotoAPISuite() {
        // This will be executed by TestNG based on the testng.xml configuration
        // Individual test methods are defined in PhotoAPITest class
    }
    
    @Test(description = "Run all todo API tests")
    @Story("Todo API Test Suite")
    @Severity(SeverityLevel.HIGH)
    public void testTodoAPISuite() {
        // This will be executed by TestNG based on the testng.xml configuration
        // Individual test methods are defined in TodoAPITest class
    }
    
    @Test(description = "Performance test for all APIs")
    @Story("Complete Performance Testing")
    @Severity(SeverityLevel.MEDIUM)
    public void testAllAPIPerformance() {
        // This test can be used to measure overall framework performance
        // when running all tests together
    }
    
    @AfterClass
    public void tearDown() {
        // Cleanup and generate performance report
        performanceUtils.generatePerformanceReport();
    }
} 