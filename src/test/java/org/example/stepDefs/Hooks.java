package org.example.stepDefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.utils.ConfigReader;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class Hooks {

    private final ScenarioContext ctx;
    private final ConfigReader    config = ConfigReader.getInstance();

    public Hooks(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Order 0 — runs first among all @Before hooks.
     * all step definitions receive the same driver instance.
     */
    @Before(order = 0)
    public void setUp() {
        WebDriver driver = buildDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        driver.manage().window().maximize();
        ctx.setDriver(driver);
    }

    /**
     * Order 0 — runs last among all @After hooks.
     * Attaches a failure screenshot to the Allure/Cucumber report before quitting.
     */
    @After(order = 0)
    public void tearDown(Scenario scenario) {
        WebDriver driver = ctx.getDriver();
        if (driver == null) return;

        if (scenario.isFailed() && config.isScreenshotOnFailure()) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png",
                    "Failure Screenshot — " + scenario.getName());
        }
        driver.quit();
    }

    // ── Driver Factory — Java 22 switch expression ────────────────────

    private WebDriver buildDriver() {
        String  browser  = config.getBrowser().toLowerCase().trim();
        boolean headless = config.isHeadless();

        return switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) opts.addArguments("--headless");
                yield new FirefoxDriver(opts);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions opts = new EdgeOptions();
                if (headless) opts.addArguments("--headless");
                yield new EdgeDriver(opts);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
                if (headless) {
                    opts.addArguments(
                            "--headless=new",
                            "--no-sandbox",
                            "--disable-dev-shm-usage",
                            "--window-size=1920,1080"
                    );
                }
                yield new ChromeDriver(opts);
            }
        };
    }
}