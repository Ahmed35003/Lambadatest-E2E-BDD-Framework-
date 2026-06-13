@cart
Feature: Shopping Cart Management
  As a customer on the ecommerce platform
  I want to manage items in my shopping cart
  So that I can control what I intend to purchase

  Background:
    Given the user is on the home page

  # ── Scenario 10 ───────────────────────────────────────────────────
  # Data source: testUsers.json searchKeywords.validKeyword
  @cart @smoke
  Scenario: Adding a product to the cart triggers a success notification
    When the user searches for the valid keyword from test data
    And  the user clicks on the first search result
    And  the user adds the product to the cart
    Then the add-to-cart success notification should be displayed

  # ── Scenario 11 ───────────────────────────────────────────────────
  # Data source: testUsers.json validKeyword; product name stored in ScenarioContext
  @cart
  Scenario: The cart page displays the added product name and a non-zero grand total
    When the user searches for the valid keyword from test data
    And  the user clicks on the first search result
    And  the user stores the product name from the detail page
    And  the user adds the product to the cart
    And  the user navigates to the cart page
    Then the cart should contain a product matching the stored product name
    And  the cart grand total should be greater than zero

  # ── Scenario 12 ───────────────────────────────────────────────────
  # Data source: testUsers.json validKeyword; quantity value is inline test data
  @cart
  Scenario: Updating the product quantity in the cart reflects the new quantity
    When the user searches for the valid keyword from test data
    And  the user clicks on the first search result
    And  the user adds the product to the cart
    And  the user navigates to the cart page
    And  the user updates the product quantity to 2
    Then the cart should display a quantity of 2

  # ── Scenario 13 ───────────────────────────────────────────────────
  # Data source: testUsers.json validKeyword
  @cart
  Scenario: Removing all products from the cart shows the empty cart message
    When the user searches for the valid keyword from test data
    And  the user clicks on the first search result
    And  the user adds the product to the cart
    And  the user navigates to the cart page
    And  the user removes all items from the cart
    Then the empty cart message should be displayed