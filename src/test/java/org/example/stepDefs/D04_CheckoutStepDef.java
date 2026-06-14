package org.example.stepDefs;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.example.pages.P03_Home_Search;
import org.example.pages.P04_Cart;
import org.example.pages.P05_Checkout;
import org.example.utils.JsonDataReader;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Ecommerce Platform — LambdaTest Playground")
@Feature("Checkout and Order Placement")
public class D04_CheckoutStepDef {

    private final ScenarioContext ctx;
    private final JsonDataReader  json = JsonDataReader.getInstance();

    private P03_Home_Search  searchPage;
    private P04_Cart     cartPage;
    private P05_Checkout checkoutPage;

    public D04_CheckoutStepDef(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @Before(order = 1)
    public void initPages() {
        searchPage   = new P03_Home_Search(ctx.getDriver());
        cartPage     = new P04_Cart(ctx.getDriver());
        checkoutPage = new P05_Checkout(ctx.getDriver());
    }

    @And("the user has a product in the cart")
    public void theUserHasAProductInTheCart() {
        String keyword = json.getValidSearchKeyword();
        ctx.setSearchKeyword(keyword);

        searchPage.searchFor(keyword);
        searchPage.clickFirstResult();

        ctx.setProductName(searchPage.getProductDetailName());
        cartPage.addToCart();
    }

    @When("the user proceeds to checkout")
    public void proceedToCheckout() {
        cartPage.proceedToCheckout();
    }

    @And("the user fills the billing address with data from test data")
    public void fillBillingAddressFromTestData() {
        String address  = json.getCheckoutAddress1();
        String city     = json.getCheckoutCity();
        String postcode = json.getCheckoutPostcode();
        String country  = json.getCheckoutCountry();

        ctx.setShippingAddress(address);
        ctx.setCity(city);
        ctx.setPostalCode(postcode);
        ctx.setCountry(country);

        checkoutPage.fillBillingAddress(
                ctx.getFirstName(),
                ctx.getLastName(),
                address, city, postcode, country
        );
    }

    @And("the user agrees to terms and conditions")
    public void agreeToTermsAndConditions() {
        checkoutPage.agreeToTermsAndConditions();
    }

    @And("the user clicks continue to review the order")
    public void clickContinueToReviewOrder() {
        checkoutPage.clickContinueBilling();
    }

    @And("the user confirms the order from the confirmation page")
    public void confirmOrderFromConfirmationPage() {
        checkoutPage.clickFinalConfirmOrder();
    }

    @And("the user clicks continue on billing address without filling the form")
    public void clickContinueBillingWithoutFilling() {
        checkoutPage.clickContinueBilling();
    }

    @Then("billing address validation errors should be displayed")
    public void verifyBillingValidationErrors() {
        assertThat(checkoutPage.hasBillingValidationErrors())
                .as("Expected billing address validation errors after submitting an empty form")
                .isTrue();
    }

    @Then("the order success page should be displayed")
    public void verifyOrderSuccessPage() {
        assertThat(checkoutPage.isOrderSuccessPageDisplayed())
                .as("Expected to land on checkout/success with a success heading")
                .isTrue();
    }

    @And("the order confirmation number should be saved in the context")
    public void saveOrderConfirmationNumber() {
        String orderNumber = checkoutPage.extractOrderConfirmationNumber();
        assertThat(orderNumber)
                .as("Order confirmation number must not be blank")
                .isNotBlank();
        ctx.setOrderConfirmationNumber(orderNumber);
    }
}