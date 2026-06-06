package org.example.stepDefs;

import io.cucumber.java.en.*;
import org.example.pages.P01_Register;
import org.example.pages.P02_Login;
import org.example.utils.JsonDataReader;
import org.example.utils.TestDataGenerator;

import static org.assertj.core.api.Assertions.assertThat;

public class D01_AuthStepDef {

    private final ScenarioContext ctx;
    private final JsonDataReader  json = JsonDataReader.getInstance();

    private P01_Register registerPage;
    private P02_Login    loginPage;

    public D01_AuthStepDef(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    // ── Lazy Getters  ──────────────────────────────────

    private P01_Register registerPage() {
        if (registerPage == null) {
            registerPage = new P01_Register(ctx.getDriver());
        }
        return registerPage;
    }

    private P02_Login loginPage() {
        if (loginPage == null) {
            loginPage = new P02_Login(ctx.getDriver());
        }
        return loginPage;
    }

    // ── Common Navigation ─────────────────────────────────────────────

    @Given("the user is on the home page")
    public void theUserIsOnTheHomePage() {
        loginPage().openHomePage();
    }

    @And("a new user registers and logs in")
    public void aNewUserRegistersAndLogsIn() {
        String email     = TestDataGenerator.generateEmail();
        String password  = TestDataGenerator.generatePassword();
        String firstName = TestDataGenerator.generateFirstName();
        String lastName  = TestDataGenerator.generateLastName();
        String phone     = TestDataGenerator.generatePhone();

        ctx.setTestEmail(email);
        ctx.setTestPassword(password);
        ctx.setFirstName(firstName);
        ctx.setLastName(lastName);
        ctx.setPhoneNumber(phone);

        registerPage().open();
        registerPage().fillRegistrationForm(firstName, lastName, email, phone, password);
        registerPage().acceptPrivacyPolicy();
        registerPage().submit();

        assertThat(registerPage().isRegistrationSuccessful())
                .as("Checkout background: registration must succeed before login attempt")
                .isTrue();


    }

    // ── Registration Steps ────────────────────────────────────────────

    @When("the user navigates to the registration page")
    public void navigateToRegistrationPage() {
        registerPage().open();
    }

    @And("the user fills the registration form with generated data")
    public void fillRegistrationFormWithGeneratedData() {
        String email     = TestDataGenerator.generateEmail();
        String password  = TestDataGenerator.generatePassword();
        String firstName = TestDataGenerator.generateFirstName();
        String lastName  = TestDataGenerator.generateLastName();
        String phone     = TestDataGenerator.generatePhone();

        ctx.setTestEmail(email);
        ctx.setTestPassword(password);
        ctx.setFirstName(firstName);
        ctx.setLastName(lastName);
        ctx.setPhoneNumber(phone);

        registerPage().fillRegistrationForm(firstName, lastName, email, phone, password);
    }

    @And("the user agrees to the privacy policy")
    public void agreeToPrivacyPolicy() {
        registerPage().acceptPrivacyPolicy();
    }

    @And("the user submits the registration form")
    public void submitRegistrationForm() {
        registerPage().submit();
    }

    @Then("the account creation success page should be displayed")
    public void verifyRegistrationSuccess() {
        assertThat(registerPage().isRegistrationSuccessful())
                .as("Expected the 'Your Account Has Been Created!' heading")
                .isTrue();
    }

    @And("the user navigates to the registration page again")
    public void navigateToRegistrationPageAgain() {
        //ctx.getDriver().get("https://ecommerce-playground.lambdatest.io/index.php?route=account/logout");
        registerPage().open();
    }

    @And("the user attempts to register with the same stored email")
    public void attemptRegisterWithStoredEmail() {
        assertThat(ctx.getTestEmail())
                .as("ScenarioContext must hold the email from the first registration")
                .isNotNull().isNotEmpty();

        registerPage().fillRegistrationForm(
                ctx.getFirstName(), ctx.getLastName(),
                ctx.getTestEmail(),
                ctx.getPhoneNumber(), ctx.getTestPassword()
        );
        registerPage().acceptPrivacyPolicy();
        registerPage().submit();
    }

    @Then("a registration error message should be displayed")
    public void verifyRegistrationError() {
        assertThat(registerPage().isRegistrationErrorDisplayed())
                .as("Expected a duplicate-email registration error alert")
                .isTrue();
    }

    // ── Login Steps ───────────────────────────────────────────────────

    @When("the user navigates to the login page")
    public void navigateToLoginPage() {
        ctx.getDriver().get("https://ecommerce-playground.lambdatest.io/index.php?route=account/logout");
        loginPage().open();
    }

    @And("the user logs in with the registered credentials")
    public void loginWithRegisteredCredentials() {
        assertThat(ctx.getTestEmail())
                .as("testEmail must be stored in ScenarioContext before this step")
                .isNotNull().isNotEmpty();
        loginPage().loginWith(ctx.getTestEmail(), ctx.getTestPassword());
    }

    @And("the user logs in with the registered email but wrong password from test data")
    public void loginWithWrongPassword() {
        assertThat(ctx.getTestEmail())
                .as("testEmail must be stored in ScenarioContext before this step")
                .isNotNull().isNotEmpty();
        loginPage().loginWith(ctx.getTestEmail(), json.getInvalidPassword(0));
    }

    @And("the user logs in with a malformed email from test data")
    public void loginWithMalformedEmail() {
        loginPage().loginWith(json.getInvalidEmail(1), json.getInvalidPassword(1));
    }

    @Then("the user account dashboard should be displayed")
    public void verifyAccountDashboard() {
        assertThat(loginPage().isAccountDashboardDisplayed())
                .as("Expected URL to contain 'route=account/account'")
                .isTrue();
    }

    @Then("the login error message should be displayed")
    public void verifyLoginError() {
        assertThat(loginPage().isLoginErrorDisplayed())
                .as("Expected a login error alert to be visible")
                .isTrue();
    }

    // ── Logout Steps ──────────────────────────────────────────────────

    @And("the user logs out")
    public void logout() {
        loginPage().logout();
    }

    @Then("the user should be redirected to the home page")
    public void verifyRedirectToHomePage() {
        assertThat(loginPage().isOnHomePage())
                .as("Expected the user to land on the home page after logout")
                .isTrue();
    }
}