package com.apiautomation.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager for handling environment-specific configurations
 */
public class ConfigManager {
    private static final Properties properties = new Properties();
    private static final String DEFAULT_ENV = "qa";
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        String environment = System.getProperty("env", DEFAULT_ENV);
        String configFile = environment + ".properties";
        
        try (InputStream input = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config/" + configFile)) {
            if (input != null) {
                properties.load(input);
                System.out.println("✅ Loaded configuration from: " + configFile);
            } else {
                System.out.println("⚠️  Configuration file not found: " + configFile + ", using default");
                loadDefaultProperties();
            }
        } catch (IOException e) {
            System.out.println("⚠️  Error loading configuration: " + e.getMessage() + ", using default");
            loadDefaultProperties();
        }
    }
    
    private static void loadDefaultProperties() {
        properties.setProperty("base.url", "https://jsonplaceholder.typicode.com");
        properties.setProperty("api.timeout", "30000");
        properties.setProperty("retry.count", "3");
        properties.setProperty("ai.enabled", "false");
        properties.setProperty("test.parallel.threads", "4");
        properties.setProperty("max.response.time", "5000");
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public static int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(properties.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static String getBaseUrl() {
        return getProperty("base.url");
    }
    
    public static int getApiTimeout() {
        return getIntProperty("api.timeout", 30000);
    }
    
    public static int getRetryCount() {
        return getIntProperty("retry.count", 3);
    }
    
    public static boolean isAiEnabled() {
        return getBooleanProperty("ai.enabled", false);
    }
    
    public static int getMaxResponseTime() {
        return getIntProperty("max.response.time", 5000);
    }
} 