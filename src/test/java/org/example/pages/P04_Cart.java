package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class P04_Cart extends BasePage {
    // ── Static Locators ───────────────────────────────────────────────
    private static final By ADD_TO_CART_BUTTON = By.className("button-cart");
    private static final By SUCCESS_ALERT      = By.id("notification-box-top");

    private static final By CART_ROWS       = By.cssSelector(
            "table.table.table-bordered tbody tr");
    private static final By QTY_INPUT       = By.cssSelector(
            "input[name^='quantity']");
    private static final By UPDATE_BUTTON = By.xpath(
            "//table[contains(@class,'table-bordered')]//button[i[contains(@class, 'fa-sync-alt')]]"
    );

    //    private static final By UPDATE_BUTTON   = By.cssSelector(
//            "button[data-original-title='Update']");
    private static final By REMOVE_BUTTONS  = By.xpath(
            "//table[contains(@class,'table-bordered')]//button[contains(@onclick, 'cart.remove') or i[contains(@class, 'fa-times-circle')]]"
    );
    private static final By CART_TOTAL_CELL = By.xpath(
            "(//table[contains(@class,'table-bordered')])[2]//tr[last()]//td[last()]");

    //    private static final By CART_TOTAL_CELL = By.xpath(
//            "//table[contains(@class,'table-bordered')]//tfoot//tr[last()]//td[last()]");
    private static final By EMPTY_MSG       = By.xpath(
            "//*[@id='content']//p[contains(text(),'Your shopping cart is empty')]");
    private static final By CHECKOUT_BTN    = By.xpath(
            "//a[contains(@href,'checkout/checkout') and contains(@class,'btn-primary')]");

    public P04_Cart(WebDriver driver) {
        super(driver);
    }

    public void addToCart() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(ADD_TO_CART_BUTTON));
        jsClick(btn);
    }

    public boolean isAddToCartSuccessDisplayed() {
        return isDisplayed(SUCCESS_ALERT);
    }

    // ── Navigation ────────────────────────────────────────────────────

    public void open() {
        navigateTo("index.php?route=checkout/cart");
    }

    // ── Queries ───────────────────────────────────────────────────────

    public boolean cartContainsProductWithName(String productName) {
        List<WebElement> rows = findAll(CART_ROWS);
        return rows.stream().anyMatch(row ->
                row.getText().toLowerCase().contains(productName.toLowerCase()));
    }

    public double getCartGrandTotal() {
        String raw = getText(CART_TOTAL_CELL)
                .replace("$", "").replace(",", "").trim();
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public int getCurrentQuantity() {
        String value = getAttribute(QTY_INPUT, "value");
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean isEmptyCartMessageDisplayed() {
        return isDisplayed(EMPTY_MSG);
    }

    // ── Actions ───────────────────────────────────────────────────────

    public void updateProductQuantity(int newQuantity) {
        type(QTY_INPUT, String.valueOf(newQuantity));
        click(UPDATE_BUTTON);
        wait.until(d -> {
            try {
                String val = d.findElement(QTY_INPUT).getAttribute("value");
                return String.valueOf(newQuantity).equals(val);
            } catch (StaleElementReferenceException e) {
                return false;
            }
        });
    }

    public void removeAllItems() {
        List<WebElement> buttons = findAll(REMOVE_BUTTONS);
        while (!buttons.isEmpty()) {
            int sizeBefore = buttons.size();
            jsClick(buttons.get(0));
            wait.until(d -> d.findElements(REMOVE_BUTTONS).size() < sizeBefore);
            buttons = findAll(REMOVE_BUTTONS);
        }
    }

    public void proceedToCheckout() {
        click(CHECKOUT_BTN);
    }
}