package com.apiautomation.framework.tests;

import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

public class SimpleWorkingTest {

    @Test
    public void testBasicFunctionality() {
        assertTrue(true, "This test should always pass");
        System.out.println("✅ Basic test is working!");
    }

    @Test
    public void testFrameworkSetup() {
        assertTrue(true, "Framework setup is working");
        System.out.println("✅ Framework setup is working!");
    }
} 