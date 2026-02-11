package com.automation.config;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for cloud-based test execution platforms.
 * Supports BrowserStack and LambdaTest.
 */
public class CloudConfig {

    // Environment variable names
    private static final String BROWSERSTACK_USERNAME = "BROWSERSTACK_USERNAME";
    private static final String BROWSERSTACK_ACCESS_KEY = "BROWSERSTACK_ACCESS_KEY";
    private static final String LAMBDATEST_USERNAME = "LAMBDATEST_USERNAME";
    private static final String LAMBDATEST_ACCESS_KEY = "LAMBDATEST_ACCESS_KEY";

    // Cloud URLs
    private static final String BROWSERSTACK_HUB_URL = "https://%s:%s@hub-cloud.browserstack.com/wd/hub";
    private static final String LAMBDATEST_HUB_URL = "https://%s:%s@hub.lambdatest.com/wd/hub";

    /**
     * Execution environment options
     */
    public enum ExecutionEnv {
        LOCAL,
        BROWSERSTACK,
        LAMBDATEST
    }

    /**
     * Get execution environment from system property or default to LOCAL
     */
    public static ExecutionEnv getExecutionEnv() {
        String env = System.getProperty("execution.env", "local").toUpperCase();
        try {
            return ExecutionEnv.valueOf(env);
        } catch (IllegalArgumentException e) {
            return ExecutionEnv.LOCAL;
        }
    }

    /**
     * Create BrowserStack WebDriver
     */
    public static WebDriver createBrowserStackDriver(String browser, String testName) 
            throws MalformedURLException {
        
        String username = getEnvOrProperty(BROWSERSTACK_USERNAME, "browserstack.username");
        String accessKey = getEnvOrProperty(BROWSERSTACK_ACCESS_KEY, "browserstack.accessKey");

        if (username == null || accessKey == null) {
            throw new RuntimeException("BrowserStack credentials not found. Provide them as environment variables BROWSERSTACK_USERNAME and BROWSERSTACK_ACCESS_KEY, or as Maven system properties -Dbrowserstack.username and -Dbrowserstack.accessKey.");
        }

        MutableCapabilities capabilities = new MutableCapabilities();
        
        // Browser capabilities
        Map<String, Object> browserstackOptions = new HashMap<>();
        browserstackOptions.put("os", "Windows");
        browserstackOptions.put("osVersion", "11");
        browserstackOptions.put("browserVersion", "latest");
        browserstackOptions.put("projectName", "Test Automation Framework");
        browserstackOptions.put("buildName", getBuildName());
        browserstackOptions.put("sessionName", testName);
        browserstackOptions.put("local", "false");
        browserstackOptions.put("seleniumVersion", "4.27.0");
        browserstackOptions.put("debug", "true");
        browserstackOptions.put("networkLogs", "true");
        browserstackOptions.put("consoleLogs", "info");

        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("bstack:options", browserstackOptions);

        String hubUrl = String.format(BROWSERSTACK_HUB_URL, username, accessKey);
        return new RemoteWebDriver(new URL(hubUrl), capabilities);
    }

    /**
     * Create LambdaTest WebDriver
     */
    public static WebDriver createLambdaTestDriver(String browser, String testName) 
            throws MalformedURLException {
        
        String username = getEnvOrProperty(LAMBDATEST_USERNAME, "lambdatest.username");
        String accessKey = getEnvOrProperty(LAMBDATEST_ACCESS_KEY, "lambdatest.accessKey");

        if (username == null || accessKey == null) {
            throw new RuntimeException("LambdaTest credentials not found. Provide them as environment variables LAMBDATEST_USERNAME and LAMBDATEST_ACCESS_KEY, or as Maven system properties -Dlambdatest.username and -Dlambdatest.accessKey.");
        }

        MutableCapabilities capabilities = new MutableCapabilities();
        
        // LambdaTest specific options
        Map<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("platform", "Windows 11");
        ltOptions.put("browserVersion", "latest");
        ltOptions.put("project", "Test Automation Framework");
        ltOptions.put("build", getBuildName());
        ltOptions.put("name", testName);
        ltOptions.put("selenium_version", "4.27.0");
        ltOptions.put("console", "true");
        ltOptions.put("network", "true");
        ltOptions.put("video", "true");
        ltOptions.put("screenshot", "true");
        ltOptions.put("visual", "true");
        ltOptions.put("w3c", true);

        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("LT:Options", ltOptions);

        String hubUrl = String.format(LAMBDATEST_HUB_URL, username, accessKey);
        return new RemoteWebDriver(new URL(hubUrl), capabilities);
    }

    /**
     * Get value from environment variable or system property
     */
    private static String getEnvOrProperty(String envName, String propertyName) {
        // First try direct environment variable lookup (common case)
        String value = System.getenv(envName);

        // If not found, try case-insensitive environment variable lookup (Windows/CI differences)
        if (value == null || value.isEmpty()) {
            Map<String, String> envs = System.getenv();
            for (String key : envs.keySet()) {
                if (key.equalsIgnoreCase(envName)) {
                    value = envs.get(key);
                    break;
                }
            }
        }

        // Next try a JVM system property (passed via -D)
        if (value == null || value.isEmpty()) {
            value = System.getProperty(propertyName);
        }

        // Normalize empty -> null
        if (value != null && value.isEmpty()) {
            value = null;
        }

        return value;
    }

    /**
     * Get build name from Jenkins or generate default
     */
    private static String getBuildName() {
        String buildNumber = System.getenv("BUILD_NUMBER");
        String jobName = System.getenv("JOB_NAME");
        
        if (buildNumber != null && jobName != null) {
            return jobName + " #" + buildNumber;
        }
        return "Local Build - " + System.currentTimeMillis();
    }

    /**
     * Mark test status on cloud platform
     */
    public static void markTestStatus(WebDriver driver, boolean passed, String reason) {
        if (driver instanceof RemoteWebDriver) {
            String script = String.format(
                "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"%s\", \"reason\": \"%s\"}}",
                passed ? "passed" : "failed",
                reason.replace("\"", "'")
            );
            try {
                ((RemoteWebDriver) driver).executeScript(script);
            } catch (Exception ignored) {
                // May not be BrowserStack, try LambdaTest format
            }
        }
    }
}
