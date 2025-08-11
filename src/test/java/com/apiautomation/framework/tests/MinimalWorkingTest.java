package com.apiautomation.framework.tests;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MinimalWorkingTest {

    @Test
    public void testBasicAssertion() {
        assertTrue(true, "Basic assertion should pass");
        System.out.println("✅ Basic assertion test passed!");
    }

    @Test
    public void testStringComparison() {
        String expected = "Hello World";
        String actual = "Hello World";
        assertEquals(actual, expected, "Strings should match");
        System.out.println("✅ String comparison test passed!");
    }

    @Test
    public void testMathOperation() {
        int result = 2 + 2;
        assertEquals(result, 4, "2 + 2 should equal 4");
        System.out.println("✅ Math operation test passed!");
    }

    @Test
    public void testArrayOperation() {
        String[] fruits = {"apple", "banana", "orange"};
        assertNotNull(fruits, "Array should not be null");
        assertEquals(fruits.length, 3, "Array should have 3 elements");
        System.out.println("✅ Array operation test passed!");
    }
} 