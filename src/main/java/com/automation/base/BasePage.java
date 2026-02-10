package com.automation.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    private static final int DEFAULT_TIMEOUT = 10;
    private static final int MAX_RETRIES = 3;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        PageFactory.initElements(driver, this);
    }

    protected void click(WebElement element) {
        try {
            waitForElementToBeClickable(element);
            logger.info("Clicking on element: " + element.toString());
            element.click();
        } catch (StaleElementReferenceException e) {
            logger.warn("Stale element encountered on click, retrying...");
            // Refresh PageFactory elements
            PageFactory.initElements(driver, this);
            waitForElementToBeClickable(element);
            element.click();
        } catch (Exception e) {
            logger.error("Failed to click element after retries", e);
            throw e;
        }
    }

    protected void sendKeys(WebElement element, String text) {
        try {
            waitForElementToBeVisible(element);
            element.clear();
            logger.info("Entering text: " + text);
            element.sendKeys(text);
        } catch (StaleElementReferenceException e) {
            logger.warn("Stale element encountered on sendKeys, retrying...");
            // Refresh PageFactory elements
            PageFactory.initElements(driver, this);
            waitForElementToBeVisible(element);
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            logger.error("Failed to send keys after retries", e);
            throw e;
        }
    }

    protected String getText(WebElement element) {
        try {
            waitForElementToBeVisible(element);
            return element.getText();
        } catch (StaleElementReferenceException e) {
            logger.warn("Stale element encountered on getText, retrying...");
            PageFactory.initElements(driver, this);
            waitForElementToBeVisible(element);
            return element.getText();
        }
    }

    protected boolean isElementDisplayed(WebElement element) {
        try {
            waitForElementToBeVisible(element);
            return element.isDisplayed();
        } catch (StaleElementReferenceException e) {
            logger.warn("Stale element on isDisplayed, returning false");
            return false;
        } catch (Exception e) {
            logger.debug("Element not displayed: " + e.getMessage());
            return false;
        }
    }

    protected void waitForElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}

