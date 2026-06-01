package org.example.stepDefs;

import org.openqa.selenium.WebDriver;


public class ScenarioContext {

    // ── WebDriver (lifecycle owned by Hooks) ──────────────────────────
    private WebDriver driver;

    // ── Registered / Active User ──────────────────────────────────────
    private String testEmail;
    private String testPassword;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    // ── Search & Product ──────────────────────────────────────────────
    private String searchKeyword;
    private String productName;
    private String productPrice;
    private int    productQuantity;

    // ── Cart ──────────────────────────────────────────────────────────
    private double cartTotal;

    // ── Checkout & Order ──────────────────────────────────────────────
    private String shippingAddress;
    private String city;
    private String postalCode;
    private String country;
    private String paymentMethod;
    private String orderConfirmationNumber;

    // ═════════════════════════════════════════════════════════════════
    // Getters & Setters
    // ═════════════════════════════════════════════════════════════════

    public WebDriver getDriver()                          { return driver; }
    public void setDriver(WebDriver driver)               { this.driver = driver; }

    public String getTestEmail()                          { return testEmail; }
    public void setTestEmail(String testEmail)            { this.testEmail = testEmail; }

    public String getTestPassword()                       { return testPassword; }
    public void setTestPassword(String testPassword)      { this.testPassword = testPassword; }

    public String getFirstName()                          { return firstName; }
    public void setFirstName(String firstName)            { this.firstName = firstName; }

    public String getLastName()                           { return lastName; }
    public void setLastName(String lastName)              { this.lastName = lastName; }

    public String getPhoneNumber()                        { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber)        { this.phoneNumber = phoneNumber; }

    public String getSearchKeyword()                      { return searchKeyword; }
    public void setSearchKeyword(String searchKeyword)    { this.searchKeyword = searchKeyword; }

    public String getProductName()                        { return productName; }
    public void setProductName(String productName)        { this.productName = productName; }

    public String getProductPrice()                       { return productPrice; }
    public void setProductPrice(String productPrice)      { this.productPrice = productPrice; }

    public int getProductQuantity()                       { return productQuantity; }
    public void setProductQuantity(int productQuantity)   { this.productQuantity = productQuantity; }

    public double getCartTotal()                          { return cartTotal; }
    public void setCartTotal(double cartTotal)            { this.cartTotal = cartTotal; }

    public String getShippingAddress()                    { return shippingAddress; }
    public void setShippingAddress(String shippingAddress){ this.shippingAddress = shippingAddress; }

    public String getCity()                               { return city; }
    public void setCity(String city)                      { this.city = city; }

    public String getPostalCode()                         { return postalCode; }
    public void setPostalCode(String postalCode)          { this.postalCode = postalCode; }

    public String getCountry()                            { return country; }
    public void setCountry(String country)                { this.country = country; }

    public String getPaymentMethod()                      { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod)    { this.paymentMethod = paymentMethod; }

    public String getOrderConfirmationNumber()            { return orderConfirmationNumber; }
    public void setOrderConfirmationNumber(String num)    { this.orderConfirmationNumber = num; }
}