package org.example.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class ConfigReader {

    private static ConfigReader instance;
    private final JsonNode config;

    private ConfigReader() {
        try {
            InputStream stream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("testData/testConfig.json");
            if (stream == null) {
                throw new IllegalStateException(
                        "testConfig.json not found — expected at src/test/resources/testData/testConfig.json");
            }
            config = new ObjectMapper().readTree(stream);
        } catch (Exception e) {
            throw new RuntimeException("ConfigReader initialisation failed", e);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String getBaseUrl()           { return config.get("baseUrl").asText(); }
    public String getBrowser()           { return config.get("browser").asText(); }
    public boolean isHeadless()          { return config.get("headless").asBoolean(); }
    public int getImplicitWait()         { return config.get("implicitWaitSeconds").asInt(); }
    public int getExplicitWait()         { return config.get("explicitWaitSeconds").asInt(); }
    public int getPageLoadTimeout()      { return config.get("pageLoadTimeoutSeconds").asInt(); }
    public boolean isScreenshotOnFailure(){ return config.get("screenshotOnFailure").asBoolean(); }
}