package org.example.pages;

import org.example.utils.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BasePage {

    protected final WebDriver     driver;
    protected final WebDriverWait wait;
    private   final ConfigReader  config = ConfigReader.getInstance();

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    // ── Visibility ────────────────────────────────────────────────────

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    // ── Clickability ──────────────────────────────────────────────────

    protected WebElement waitForClickability(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement waitForClickability(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // ── Interactions ──────────────────────────────────────────────────

    protected void click(By locator) {
        waitForClickability(locator).click();
    }

    protected void click(WebElement element) {
        waitForClickability(element).click();
    }

    protected void type(By locator, String text) {
        WebElement el = waitForVisibility(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected void type(WebElement element, String text) {
        waitForVisibility(element).clear();
        element.sendKeys(text);
    }

    protected void selectByVisibleText(By locator, String visibleText) {
        new Select(waitForVisibility(locator)).selectByVisibleText(visibleText);
    }

    protected void selectByIndex(By locator, int index) {
        new Select(waitForVisibility(locator)).selectByIndex(index);
    }

    // ── Reading ───────────────────────────────────────────────────────

    protected String getText(By locator) {
        return waitForVisibility(locator).getText().trim();
    }

    protected String getText(WebElement element) {
        return waitForVisibility(element).getText().trim();
    }

    protected String getAttribute(By locator, String attribute) {
        return waitForVisibility(locator).getAttribute(attribute);
    }

    // ── State ─────────────────────────────────────────────────────────

    protected boolean isDisplayed(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return waitForVisibility(element).isDisplayed();
        } catch (TimeoutException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    // ── Navigation ────────────────────────────────────────────────────

    protected void navigateTo(String relativePath) {
        driver.get(config.getBaseUrl() + relativePath);
    }

    protected void navigateToBaseUrl() {
        driver.get(config.getBaseUrl());
    }

    // ── JavaScript Utilities ──────────────────────────────────────────

    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", element);
    }

    // ── URL & Title ───────────────────────────────────────────────────

    protected String getCurrentUrl()  { return driver.getCurrentUrl(); }
    protected String getPageTitle()   { return driver.getTitle(); }
    protected String getBaseUrl()     { return config.getBaseUrl(); }
}