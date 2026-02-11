package com.automation.listeners;

import com.automation.logger.TestLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TestNG Listener that captures test lifecycle events and emits
 * structured JSON logs for the analysis pipeline.
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private final TestLogger testLogger;
    private final Map<String, Long> testStartTimes;

    public TestListener() {
        this.testLogger = TestLogger.getInstance();
        this.testStartTimes = new ConcurrentHashMap<>();
        logger.info("TestListener initialized - logging to: " +
                    testLogger.getLogFilePath());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testId = generateTestId(result);
        testStartTimes.put(testId, System.currentTimeMillis());
        
        testLogger.logTestStart(
                testId,
                result.getMethod().getMethodName(),
                result.getTestContext().getName(),
                result.getTestClass().getName()
        );
        
        logger.info("Test started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testId = generateTestId(result);
        long duration = calculateDuration(testId);
        
        testLogger.logTestSuccess(
                testId,
                result.getMethod().getMethodName(),
                result.getTestContext().getName(),
                result.getTestClass().getName(),
                duration
        );
        
        logger.info("Test passed: " + result.getMethod().getMethodName() + 
                    " (Duration: " + duration + "ms)");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testId = generateTestId(result);
        long duration = calculateDuration(testId);
        
        Throwable throwable = result.getThrowable();
        String errorMessage = throwable != null ? throwable.getMessage() : "Unknown error";
        String stacktrace = throwable != null ? getStackTrace(throwable) : "";
        
        testLogger.logTestFailure(
                testId,
                result.getMethod().getMethodName(),
                result.getTestContext().getName(),
                result.getTestClass().getName(),
                duration,
                errorMessage,
                stacktrace
        );
        
        logger.error("Test failed: " + result.getMethod().getMethodName() + 
                     " - " + errorMessage);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testId = generateTestId(result);
        
        Throwable throwable = result.getThrowable();
        String reason = throwable != null ? throwable.getMessage() : "Test skipped";
        
        testLogger.logTestSkipped(
                testId,
                result.getMethod().getMethodName(),
                result.getTestContext().getName(),
                result.getTestClass().getName(),
                reason
        );
        
        logger.warn("Test skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("Test suite started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test suite finished: " + context.getName() + 
                    " | Passed: " + context.getPassedTests().size() +
                    " | Failed: " + context.getFailedTests().size() +
                    " | Skipped: " + context.getSkippedTests().size());
        
        logger.info("Analytics logs available at: " + 
                    testLogger.getLogFilePath().toAbsolutePath());
    }

    private String generateTestId(ITestResult result) {
        return result.getTestClass().getName() + "." + 
               result.getMethod().getMethodName() + "_" + 
               result.getStartMillis();
    }

    private long calculateDuration(String testId) {
        Long startTime = testStartTimes.remove(testId);
        if (startTime != null) {
            return System.currentTimeMillis() - startTime;
        }
        return 0;
    }

    private String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
