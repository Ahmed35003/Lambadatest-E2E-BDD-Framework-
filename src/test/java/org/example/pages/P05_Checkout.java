package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class P05_Checkout extends BasePage {

    // ── Billing Address Form ──────────────────────────────────────────
    @FindBy(id = "input-payment-firstname") private WebElement billingFirstName;
    @FindBy(id = "input-payment-lastname")  private WebElement billingLastName;
    @FindBy(id = "input-payment-address-1") private WebElement billingAddress1;
    @FindBy(id = "input-payment-city")      private WebElement billingCity;
    @FindBy(id = "input-payment-postcode")  private WebElement billingPostcode;
    @FindBy(id = "input-payment-country")   private WebElement billingCountrySelect;
    @FindBy(id = "input-payment-zone")      private WebElement billingZoneSelect;

    // ── Step Continue Buttons ─────────────────────────────────────────
    @FindBy(id = "button-save") private WebElement continueBillingBtn;
    @FindBy(id = "button-confirm") private WebElement confirmOrderBtn;

    // ── Static Locators ───────────────────────────────────────────────
    private static final By BILLING_FIRST_NAME    = By.id("input-payment-firstname");
    private static final By BILLING_ZONE_SELECT   = By.id("input-payment-zone");
    private static final By TERMS_LABEL = By.cssSelector("label[for='input-agree']");
    private static final By TERMS_INPUT = By.id("input-agree");
    private static final By SUCCESS_HEADING       = By.cssSelector("#content h1");
    private static final By ORDER_LINK            = By.xpath("//a[contains(@href,'account/order/info')]");
    private static final By VALIDATION_ERRORS     = By.cssSelector(".text-danger");
    private static final By ALERT_DANGER          = By.cssSelector("div.alert.alert-danger");

    public P05_Checkout(WebDriver driver) {
        super(driver);
    }

    // ── Step 1: Billing Address ───────────────────────────────────────

    public void fillBillingAddress(String firstName, String lastName,
                                   String address, String city,
                                   String postcode, String country) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(BILLING_FIRST_NAME));

        type(billingFirstName,  firstName);
        type(billingLastName,   lastName);
        type(billingAddress1,   address);
        type(billingCity,       city);
        type(billingPostcode,   postcode);

        new Select(wait.until(ExpectedConditions.elementToBeClickable(billingCountrySelect))).selectByVisibleText(country);

        wait.until(d -> {
            Select zone = new Select(d.findElement(BILLING_ZONE_SELECT));
            return zone.getOptions().size() > 1;
        });

        new Select(wait.until(ExpectedConditions.elementToBeClickable(billingZoneSelect))).selectByIndex(1);
    }

    public void agreeToTermsAndConditions() {
        WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(TERMS_LABEL));
        scrollToElement(label);

        WebElement checkboxInput = driver.findElement(TERMS_INPUT);

        if (!checkboxInput.isSelected()) {
            jsClick(label);
        }
    }

    public void clickContinueBilling() {
        scrollToElement(continueBillingBtn);
        jsClick(continueBillingBtn);
    }


    public boolean hasBillingValidationErrors() {
        return isDisplayed(VALIDATION_ERRORS) || isDisplayed(ALERT_DANGER);
    }

    // ── Step 2: Confirm Order ─────────────────────────────────────────

    public void clickFinalConfirmOrder() {
        WebElement finalBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmOrderBtn));
        scrollToElement(finalBtn);
        jsClick(finalBtn);
    }

    // ── Success Page ──────────────────────────────────────────────────

    public boolean isOrderSuccessPageDisplayed() {
        try {
            wait.until(ExpectedConditions.urlContains("route=checkout/success"));

            wait.until(ExpectedConditions.visibilityOfElementLocated(SUCCESS_HEADING));

            return true;
        } catch (TimeoutException e) {
            return false;
        }    }

    public String extractOrderConfirmationNumber() {
        try {
            WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(ORDER_LINK));
            String href = link.getAttribute("href");
            if (href != null && href.contains("order_id=")) {
                return href.substring(href.lastIndexOf("order_id=") + 9);
            }
        } catch (Exception ignored) {
        }
        return "ORDER_CONFIRMED";
    }
}