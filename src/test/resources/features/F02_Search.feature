@search
Feature: Product Search
  As a customer on the ecommerce platform
  I want to search for products by keyword
  So that I can find items I intend to purchase

  Background:
    Given the user is on the home page

  # ── Scenario 7 ────────────────────────────────────────────────────
  # Data source: testUser.json searchKeywords.validKeyword
  @search @smoke
  Scenario: Searching for a known product keyword returns relevant results
    When the user searches for the valid keyword from test data
    Then the search results page should be displayed
    And  at least one product result should be visible
    And  the displayed products should relate to the search keyword

  # ── Scenario 8 ────────────────────────────────────────────────────
  # Data source: testUser.json  searchKeywords.invalidKeyword
  @search @negative
  Scenario: Searching for a non-existent term shows the no-results message
    When the user searches for the invalid keyword from test data
    Then the no results message should be displayed

  # ── Scenario 9 ────────────────────────────────────────────────────
  # Data source: testUser.json  searchKeywords.validKeyword
  @search
  Scenario: Clicking a search result navigates to the product detail page
    When the user searches for the valid keyword from test data
    Then the search results page should be displayed
    When the user clicks on the first search result
    Then the product detail page should be displayed
    And  the product name should be stored in the context