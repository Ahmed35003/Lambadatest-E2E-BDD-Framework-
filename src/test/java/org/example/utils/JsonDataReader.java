package org.example.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class JsonDataReader {

    private static JsonDataReader instance;
    private final JsonNode root;

    private JsonDataReader() {
        try {

            InputStream stream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("testData/testUsers.json");

            if (stream == null) {
                throw new IllegalStateException(
                        "testUsers.json not found — expected at src/test/resources/testData/testUsers.json");
            }
            root = new ObjectMapper().readTree(stream);
        } catch (Exception e) {
            throw new RuntimeException("JsonDataReader initialisation failed", e);
        }
    }

    public static synchronized JsonDataReader getInstance() {
        if (instance == null) {
            instance = new JsonDataReader();
        }
        return instance;
    }

    // ── Invalid Credentials ───────────────────────────────────────────
    // index 0 → valid email format, wrong password
    // index 1 → malformed email format
    public String getInvalidEmail(int index) {
        return root.get("invalidCredentials").get(index).get("email").asText();
    }

    public String getInvalidPassword(int index) {
        return root.get("invalidCredentials").get(index).get("password").asText();
    }

    // ── Checkout Address ──────────────────────────────────────────────
    public String getCheckoutAddress1() { return root.get("checkoutAddress").get("address1").asText(); }
    public String getCheckoutCity()     { return root.get("checkoutAddress").get("city").asText(); }
    public String getCheckoutPostcode() { return root.get("checkoutAddress").get("postcode").asText(); }
    public String getCheckoutCountry()  { return root.get("checkoutAddress").get("country").asText(); }

    // ── Search Keywords ───────────────────────────────────────────────
    public String getValidSearchKeyword()   { return root.get("searchKeywords").get("validKeyword").asText(); }
    public String getAnotherSearchKeyword() { return root.get("searchKeywords").get("anotherKeyword").asText(); }
    public String getInvalidSearchKeyword() { return root.get("searchKeywords").get("invalidKeyword").asText(); }
}