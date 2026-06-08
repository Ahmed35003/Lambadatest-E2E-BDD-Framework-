package org.example.stepDefs;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.example.pages.P03_Home_Search;
import org.example.utils.JsonDataReader;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Ecommerce Platform — LambdaTest Playground")
@Feature("Product Search")
public class D02_SearchStepDef {

    private final ScenarioContext ctx;
    private final JsonDataReader  json = JsonDataReader.getInstance();

    private P03_Home_Search searchPage;

    public D02_SearchStepDef(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @Before(order = 1)
    public void initPages() {
        searchPage = new P03_Home_Search(ctx.getDriver());
    }

    // ── Search ────────────────────────────────────────────────────────

    /** Data source: Users.json searchKeywords.validKeyword */
    @When("the user searches for the valid keyword from test data")
    public void searchForValidKeyword() {
        String keyword = json.getValidSearchKeyword();
        ctx.setSearchKeyword(keyword);
        searchPage.searchFor(keyword);
    }

    /** Data source: Users.json searchKeywords.invalidKeyword */
    @When("the user searches for the invalid keyword from test data")
    public void searchForInvalidKeyword() {
        String keyword = json.getInvalidSearchKeyword();
        ctx.setSearchKeyword(keyword);
        searchPage.searchFor(keyword);
    }

    // ── Results Assertions ────────────────────────────────────────────

    @Then("the search results page should be displayed")
    public void verifySearchResultsPageDisplayed() {
        // سيناريو البحث الناجح: لازم ولابد نلاقي كروت منتجات معروضة
        assertThat(searchPage.hasSearchResults())
                .as("Expected to see product cards on the search results page")
                .isTrue();
    }

    @And("at least one product result should be visible")
    public void verifyAtLeastOneResult() {
        assertThat(searchPage.getResultCount())
                .as("Expected at least one product card in the results")
                .isGreaterThan(0);
    }

    @And("the displayed products should relate to the search keyword")
    public void verifyResultsRelateToKeyword() {
        assertThat(searchPage.anyResultContainsText(ctx.getSearchKeyword()))
                .as("Expected at least one result to contain: " + ctx.getSearchKeyword())
                .isTrue();
    }

    @Then("the no results message should be displayed")
    public void verifyNoResultsMessage() {
        // سيناريو البحث الفاشل: لازم ولابد نلاقي رسالة "لا توجد منتجات"
        assertThat(searchPage.isNoResultsMessageDisplayed())
                .as("Expected a 'no product found' message for an unmatchable keyword")
                .isTrue();
    }

    // ── Product Detail ────────────────────────────────────────────────

    @When("the user clicks on the first search result")
    public void clickFirstSearchResult() {
        searchPage.clickFirstResult();
    }

    @Then("the product detail page should be displayed")
    public void verifyProductDetailPageDisplayed() {
        assertThat(searchPage.isProductDetailPageDisplayed())
                .as("Expected the product detail page (with Add to Cart button) to be visible")
                .isTrue();
    }

    @And("the product name should be stored in the context")
    public void storeProductNameInContext() {
        String name = searchPage.getProductDetailName();
        assertThat(name)
                .as("Product detail heading must not be blank")
                .isNotBlank();
        ctx.setProductName(name);
    }

    @And("the user stores the product name from the detail page")
    public void storeProductNameFromDetailPage() {
        ctx.setProductName(searchPage.getProductDetailName());
    }

    @And("the user adds the product to the cart")
    public void addProductToCart() {
        searchPage.addToCart();
    }

    @Then("the add-to-cart success notification should be displayed")
    public void verifyAddToCartSuccess() {
        assertThat(searchPage.isAddToCartSuccessDisplayed())
                .as("Expected a success alert after clicking 'Add to Cart'")
                .isTrue();
    }
}