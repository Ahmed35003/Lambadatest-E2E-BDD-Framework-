@checkout
Feature: Checkout and Order Placement
  As an authenticated customer with a product in my cart
  I want to complete the full checkout process
  So that I can place and confirm my order

  # Background runs before each scenario:
  # - Registers a new unique user (TestDataGenerator)
  # - Logs that user in
  # - Adds a product to their cart (testUsers.json validKeyword)
  Background:
    Given the user is on the home page
    And   a new user registers and logs in
    And   the user has a product in the cart

  # ── Scenario 14 ───────────────────────────────────────────────────
  # Data source: TestDataGenerator (user) + testUsers.json checkoutAddress (billing)
  @checkout @smoke
  Scenario: Authenticated user completes checkout and sees the order success page
    When the user navigates to the cart page
    And  the user proceeds to checkout
    And  the user fills the billing address with data from test data
    And  the user agrees to terms and conditions
    And  the user clicks continue to review the order
    And  the user confirms the order from the confirmation page
    Then the order success page should be displayed

  # ── Scenario 15 ───────────────────────────────────────────────────
  # Data source: TestDataGenerator (user) + testUsers.json checkoutAddress (billing)
  # Verifies that ScenarioContext captures the order reference
  @checkout
  Scenario: Order confirmation number is captured and stored in context after checkout
    When the user navigates to the cart page
    And  the user proceeds to checkout
    And  the user fills the billing address with data from test data
    And  the user agrees to terms and conditions
    And  the user clicks continue to review the order
    And  the user confirms the order from the confirmation page
    Then the order success page should be displayed
    And  the order confirmation number should be saved in the context

  # ── Scenario 16 ───────────────────────────────────────────────────
  # Negative path — no external data needed
  @checkout @negative
  Scenario: Clicking continue with an empty billing address form shows validation errors
    When the user navigates to the cart page
    And  the user proceeds to checkout
    And  the user clicks continue on billing address without filling the form
    Then billing address validation errors should be displayed