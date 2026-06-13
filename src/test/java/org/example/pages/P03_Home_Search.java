package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class P03_Home_Search extends BasePage {

    // ── Header Search Bar ─────────────────────────────────────────────
    @FindBy(name = "search")         private WebElement searchInput;
    @FindBy(css  = "#search button") private WebElement searchButton;

    // ── Static Locators ───────────────────────────────────────────────
    private static final By PRODUCT_CARDS      = By.cssSelector(".product-layout, .product-thumb");

    private static final By FIRST_RESULT_LINK  = By.cssSelector(".product-layout .name a, .product-thumb .caption h4 a");

    private static final By NO_RESULTS_MSG     = By.xpath("//p[contains(., 'There is no product') or contains(., 'matches the search')] | //div[contains(@class, 'text-empty')]");

    private static final By PRODUCT_HEADING = By.cssSelector("h1.page-title, .page-title, h1");

    public P03_Home_Search(WebDriver driver) {
        super(driver);
    }

    // ── Search ────────────────────────────────────────────────────────

    public void searchFor(String keyword) {
        type(searchInput, keyword);

        searchInput.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("route=product/search"),
                ExpectedConditions.urlContains("route=product%2Fsearch")
        ));
    }
    // ── Results ───────────────────────────────────────────────────────
    public boolean hasSearchResults() {
        try {
            return wait.until(driver -> findAll(PRODUCT_CARDS).stream()
                    .anyMatch(WebElement::isDisplayed));
        } catch (Exception e) {
            return false;
        }
    }

    public int getResultCount() {
        try {
            wait.until(driver -> !findAll(PRODUCT_CARDS).isEmpty());
            return (int) findAll(PRODUCT_CARDS).stream()
                    .filter(WebElement::isDisplayed)
                    .count();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean anyResultContainsText(String keyword) {
        try {
            return wait.until(driver -> findAll(PRODUCT_CARDS).stream()
                    .filter(WebElement::isDisplayed)
                    .anyMatch(c -> c.getText().toLowerCase().contains(keyword.toLowerCase())));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNoResultsMessageDisplayed() {
        try {
            return wait.until(driver -> findAll(NO_RESULTS_MSG).stream()
                    .anyMatch(WebElement::isDisplayed));
        } catch (Exception e) {
            return false;
        }
    }

    public void clickFirstResult() {
        WebElement firstVisibleLink = wait.until(driver -> findAll(FIRST_RESULT_LINK).stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(null));

        if (firstVisibleLink == null) {
            throw new NoSuchElementException("👉 \"No visible product link was found to interact with.\"");
        }

        click(firstVisibleLink);

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("route=product/product"),
                ExpectedConditions.urlContains("route=product%2Fproduct")
        ));
    }

    // ── Product Detail ────────────────────────────────────────────────

    public boolean isProductDetailPageDisplayed() {
        try {
            wait.until(ExpectedConditions.urlContains("route=product/product"));

            WebElement headingElement = wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_HEADING));

            return headingElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    public String getProductDetailName() {
        return getText(PRODUCT_HEADING);
    }

}