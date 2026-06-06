package org.example.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class P01_Register extends BasePage{

    // ── Form Fields ───────────────────────────────────────────────────
    @FindBy(id = "input-firstname")  private WebElement firstNameField;
    @FindBy(id = "input-lastname")   private WebElement lastNameField;
    @FindBy(id = "input-email")      private WebElement emailField;
    @FindBy(id = "input-telephone")  private WebElement telephoneField;
    @FindBy(id = "input-password")   private WebElement passwordField;
    @FindBy(id = "input-confirm")    private WebElement confirmPasswordField;

    // ── Controls ──────────────────────────────────────────────────────
    @FindBy(name = "agree")                   private WebElement privacyPolicyCheckbox;
    @FindBy(css  = "input[value='Continue']") private WebElement continueButton;

    // ── Static Locators (used with explicit wait) ──────────────────────
    private static final By SUCCESS_HEADING = By.cssSelector("#content h1");
    private static final By ERROR_ALERT     = By.cssSelector("div.alert.alert-danger");

    public P01_Register(WebDriver driver) {
        super(driver);
    }

    // ── Navigation ────────────────────────────────────────────────────

    public void open() {
        navigateTo("index.php?route=account/register");
        waitForVisibility(firstNameField);
    }

    // ── Form Actions ──────────────────────────────────────────────────

    public void fillRegistrationForm(String firstName, String lastName,
                                     String email, String telephone, String password) {
        type(firstNameField,       firstName);
        type(lastNameField,        lastName);
        type(emailField,           email);
        type(telephoneField,       telephone);
        type(passwordField,        password);
        type(confirmPasswordField, password);
    }

    public void acceptPrivacyPolicy() {
        scrollToElement(privacyPolicyCheckbox);

        if (!privacyPolicyCheckbox.isSelected()) {
            jsClick(privacyPolicyCheckbox);
        }
    }

    public void submit() {
        click(continueButton);
    }

    // ── Verifications ─────────────────────────────────────────────────

    public boolean isRegistrationSuccessful() {
        waitForVisibility(SUCCESS_HEADING);
        return getText(SUCCESS_HEADING)
                .toLowerCase()
                .contains("your account has been created");
    }

    public boolean isRegistrationErrorDisplayed() {
        return isDisplayed(ERROR_ALERT);
    }
}
