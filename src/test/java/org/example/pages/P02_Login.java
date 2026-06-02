package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.assertj.core.api.InstanceOfAssertFactories.type;

public class P02_Login extends BasePage{

    // ── Login Form ────────────────────────────────────────────────────
    @FindBy(id  = "input-email")        private WebElement emailField;
    @FindBy(id  = "input-password")     private WebElement passwordField;
    @FindBy(css = "input[value='Login']") private WebElement loginButton;

    // ── Static Locators ───────────────────────────────────────────────
    private static final By LOGIN_ERROR_ALERT = By.xpath("//div[contains(@class,'alert-danger')]");

    private static final By ACCOUNT_NAV_TOGGLE = By.xpath("//header//a[contains(@class,'dropdown-toggle') and .//span[contains(text(),'My Account')]]");

    private static final By LOGOUT_LINK = By.xpath("//a[contains(@class,'list-group-item') and contains(.,'Logout')]");
    public P02_Login(WebDriver driver) {
        super(driver);
    }

    // ── Navigation ────────────────────────────────────────────────────

    public void openHomePage() {
        navigateToBaseUrl();
    }

    public void open() {
        navigateTo("index.php?route=account/login");
        waitForVisibility(emailField);
    }

    // ── Login ─────────────────────────────────────────────────────────

    public void loginWith(String email, String password) {
        type(emailField,   email);
        type(passwordField, password);
        click(loginButton);
    }

    // ── Verifications ─────────────────────────────────────────────────

    public boolean isLoginSuccessful() {
        return getCurrentUrl().contains("route=account/account");
    }

    public boolean isLoginErrorDisplayed() {
        return isDisplayed(LOGIN_ERROR_ALERT);
    }

    public boolean isAccountDashboardDisplayed() {
        return getCurrentUrl().contains("route=account/account");
    }

    public boolean isOnHomePage() {
        String url  = getCurrentUrl();
        String base = getBaseUrl();
        return url.equals(base)
                || url.equals(base + "index.php")
                || url.contains("route=common/home");
    }

    // ── Logout ────────────────────────────────────────────────────────

    public void logout() {
        click(LOGOUT_LINK);
    }

}
