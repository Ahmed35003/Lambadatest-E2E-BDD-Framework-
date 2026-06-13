package org.example.stepDefs;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.example.pages.P04_Cart;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Ecommerce Platform — LambdaTest Playground")
@Feature("Shopping Cart")
public class D03_CartStepDef {

    private final ScenarioContext ctx;
    private P04_Cart cartPage;

    public D03_CartStepDef(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @Before(order = 1)
    public void initPages() {
        cartPage = new P04_Cart(ctx.getDriver());
    }

    @And("the user adds the product to the cart")
    public void addProductToCart() {
        cartPage.addToCart();
    }

    @Then("the add-to-cart success notification should be displayed")
    public void verifyAddToCartSuccess() {
        assertThat(cartPage.isAddToCartSuccessDisplayed())
                .as("Expected a success alert after clicking 'Add to Cart'")
                .isTrue();
    }

    // ── Navigation ────────────────────────────────────────────────────

    @And("the user navigates to the cart page")
    public void navigateToCartPage() {
        cartPage.open();
    }

    // ── Cart Assertions ───────────────────────────────────────────────

    @Then("the cart should contain a product matching the stored product name")
    public void verifyCartContainsSavedProduct() {
        assertThat(ctx.getProductName())
                .as("productName must be stored in ScenarioContext before this assertion")
                .isNotNull().isNotEmpty();
        assertThat(cartPage.cartContainsProductWithName(ctx.getProductName()))
                .as("Cart should contain a row matching: " + ctx.getProductName())
                .isTrue();
    }

    @And("the cart grand total should be greater than zero")
    public void verifyCartTotalGreaterThanZero() {
        double total = cartPage.getCartGrandTotal();
        ctx.setCartTotal(total);
        assertThat(total)
                .as("Cart grand total must be > 0.0")
                .isGreaterThan(0.0);
    }

    // ── Quantity Steps ────────────────────────────────────────────────

    @And("the user updates the product quantity to {int}")
    public void updateProductQuantity(int quantity) {
        ctx.setProductQuantity(quantity);
        cartPage.updateProductQuantity(quantity);
    }

    @Then("the cart should display a quantity of {int}")
    public void verifyCartQuantity(int expectedQuantity) {
        assertThat(cartPage.getCurrentQuantity())
                .as("Expected cart quantity to be " + expectedQuantity)
                .isEqualTo(expectedQuantity);
    }

    // ── Remove Steps ──────────────────────────────────────────────────

    @And("the user removes all items from the cart")
    public void removeAllItemsFromCart() {
        cartPage.removeAllItems();
    }

    @Then("the empty cart message should be displayed")
    public void verifyEmptyCartMessage() {
        assertThat(cartPage.isEmptyCartMessageDisplayed())
                .as("Expected 'Your shopping cart is empty!' message after removing all items")
                .isTrue();
    }
}