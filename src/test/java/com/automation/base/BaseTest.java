package com.automation.base;

import com.automation.config.CloudConfig;
import com.automation.config.CloudConfig.ExecutionEnv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.time.Duration;

/**
 * Base test class with support for local, BrowserStack, and LambdaTest
 * execution.
 */
public class BaseTest {

    // ThreadLocal WebDriver for thread-safe parallel execution
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    // Default test URL - can be overridden via config
    protected static final String BASE_URL = "https://practicetestautomation.com/practice-test-login/";

    protected WebDriver getDriver() {
        return driver.get();
    }

    protected void setDriver(WebDriver webDriver) {
        driver.set(webDriver);
    }

    protected void removeDriver() {
        driver.remove();
    }

    @BeforeMethod
    @Parameters({ "browser" })
    public void setUp(@Optional("chrome") String browser, Method method) {
        String testName = method.getName();
        ExecutionEnv env = CloudConfig.getExecutionEnv();

        logger.info("========================================");
        logger.info("Test: " + testName);
        logger.info("Environment: " + env);
        logger.info("Browser: " + browser);
        logger.info("========================================");

        try {
            initializeDriver(browser, testName, env);
            WebDriver drv = getDriver();
            if (drv == null) {
                throw new RuntimeException("WebDriver was not initialized for thread");
            }
            drv.manage().window().maximize();
            drv.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            // Increase page load timeout to reduce false timeouts when running many browsers in parallel
            drv.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
            drv.get(BASE_URL);
            logger.info("Navigated to: " + BASE_URL);
        } catch (MalformedURLException e) {
            logger.error("Failed to initialize cloud driver", e);
            throw new RuntimeException("Cloud driver initialization failed", e);
        }
    }

    private void initializeDriver(String browser, String testName, ExecutionEnv env)
            throws MalformedURLException {

        if (browser == null || browser.isEmpty()) {
            browser = "chrome";
        }

        switch (env) {
            case BROWSERSTACK:
                logger.info("Initializing BrowserStack driver...");
                setDriver(CloudConfig.createBrowserStackDriver(browser, testName));
                break;

            case LAMBDATEST:
                logger.info("Initializing LambdaTest driver...");
                setDriver(CloudConfig.createLambdaTestDriver(browser, testName));
                break;

            case LOCAL:
            default:
                logger.info("Initializing local driver...");
                setDriver(createLocalDriver(browser));
                break;
        }

        logger.info("WebDriver initialized successfully for: " + env);
    }

    private WebDriver createLocalDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                // Allow remote origins when using newer Chrome/Chromedriver
                chromeOptions.addArguments("--remote-allow-origins=*");

                // Use a unique temporary profile per thread to avoid profile lock/contention
                String userDataDir = System.getProperty("java.io.tmpdir") + "/chrome-profile-" + Thread.currentThread().getId();
                chromeOptions.addArguments("--user-data-dir=" + userDataDir);

                // Stability flags for parallel runs
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-background-timer-throttling");
                chromeOptions.addArguments("--disable-renderer-backgrounding");
                chromeOptions.addArguments("--disable-backgrounding-occluded-windows");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--no-first-run");
                chromeOptions.addArguments("--disable-infobars");

                // Headless mode for CI or when explicitly requested
                if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }
                return new ChromeDriver(chromeOptions);

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
                    firefoxOptions.addArguments("--headless");
                }
                return new FirefoxDriver(firefoxOptions);

            case "edge":
                return new EdgeDriver();

            default:
                logger.warn("Browser '{}' not supported, defaulting to Chrome", browser);
                return new ChromeDriver();
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        String testName = result.getName();
        boolean passed = result.getStatus() == ITestResult.SUCCESS;

        WebDriver drv = getDriver();

        // Update cloud platform test status
        if (CloudConfig.getExecutionEnv() != ExecutionEnv.LOCAL && drv != null) {
            String reason = passed ? "Test passed"
                    : (result.getThrowable() != null ? result.getThrowable().getMessage() : "Test failed");
            CloudConfig.markTestStatus(drv, passed, reason);
        }

        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("❌ Test FAILED: " + testName);
            if (result.getThrowable() != null) {
                logger.error("Error: " + result.getThrowable().getMessage());
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("✅ Test PASSED: " + testName);
        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.warn("⏭️ Test SKIPPED: " + testName);
        }

        if (drv != null) {
            logger.info("Closing browser");
            try {
                drv.quit();
            } catch (Exception e) {
                logger.warn("Error while quitting driver: " + e.getMessage());
            }
        }

        // Remove thread-local reference to avoid leaks
        removeDriver();
    }

    /**
     * Get the current execution environment
     */
    protected ExecutionEnv getExecutionEnvironment() {
        return CloudConfig.getExecutionEnv();
    }

    /**
     * Check if running on cloud platform
     */
    protected boolean isCloudExecution() {
        ExecutionEnv env = CloudConfig.getExecutionEnv();
        return env == ExecutionEnv.BROWSERSTACK || env == ExecutionEnv.LAMBDATEST;
    }
}
