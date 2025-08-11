package com.apiautomation.framework.tests;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

@Epic("API Testing Framework")
@Feature("Basic Functionality")
public class SimpleTest {

    @Test
    @Story("Simple Test")
    @Description("A simple test to verify the framework works")
    public void simpleTest() {
        assertTrue(true, "This test should always pass");
    }
} 