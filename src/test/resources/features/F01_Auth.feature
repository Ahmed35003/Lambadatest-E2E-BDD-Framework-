@auth
Feature: User Authentication
  As a customer of the ecommerce platform
  I want to register, log in, and log out
  So that I can access my personal account and history

  Background:
    Given the user is on the home page

  # ── Scenario 1 ────────────────────────────────────────────────────
  # Data source: TestDataGenerator (fresh unique credentials every run)
  @register @smoke
  Scenario: Successful account registration with generated credentials
    When the user navigates to the registration page
    And  the user fills the registration form with generated data
    And  the user agrees to the privacy policy
    And  the user submits the registration form
    Then the account creation success page should be displayed

  # ── Scenario 2 ────────────────────────────────────────────────────
  # Data source: TestDataGenerator for initial registration;
  #              ScenarioContext re-uses the stored email for the duplicate attempt
  @register @negative
  Scenario: Registration with an already-registered email shows an error
    When the user navigates to the registration page
    And  the user fills the registration form with generated data
    And  the user agrees to the privacy policy
    And  the user submits the registration form
    And  the user logs out
    And  the user navigates to the registration page again
    And  the user attempts to register with the same stored email
    Then a registration error message should be displayed

  # ── Scenario 3 ────────────────────────────────────────────────────
  # Data source: TestDataGenerator → credentials stored in ScenarioContext
  #              Login step reads them directly from context
  @login @smoke
  Scenario: Registered user can log in with their generated credentials
    Given a new user registers and logs in
    And  the user logs out
    When the user navigates to the login page
    And  the user logs in with the registered credentials
    Then the user account dashboard should be displayed

  # ── Scenario 4 ────────────────────────────────────────────────────
  # Data source: TestDataGenerator for registration email (context);
  #              Users.json invalidCredentials[0].password for the wrong password
  @login @negative
  Scenario: Login with a correct email but wrong password shows an error
    Given a new user registers and logs in
    And  the user logs out
    When the user navigates to the login page
    And  the user logs in with the registered email but wrong password from test data
    Then the login error message should be displayed

  # ── Scenario 5 ────────────────────────────────────────────────────
  # Data source: Users.json invalidCredentials[1] (malformed email)
  @login @negative
  Scenario: Login with a malformed email address shows an error
    When the user navigates to the login page
    And  the user logs in with a malformed email from test data
    Then the login error message should be displayed

  # ── Scenario 6 ────────────────────────────────────────────────────
  # Data source: TestDataGenerator → ScenarioContext
  @logout @smoke
  Scenario: Authenticated user can log out and is redirected to the home page
    Given a new user registers and logs in
    And  the user logs out
    When the user is on the home page
    Then the user should be redirected to the home page