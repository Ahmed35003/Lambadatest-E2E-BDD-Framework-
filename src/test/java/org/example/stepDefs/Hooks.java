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
import org.openqa.selenium.PageLoadStrategy;

import java.time.Duration;

public class Hooks {

    private final ScenarioContext ctx;
    private final ConfigReader    config = ConfigReader.getInstance();

    public Hooks(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @Before(order = 0)
    public void setUp() {
        WebDriver driver = buildDriver();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));

        ctx.setDriver(driver);
    }

    @After(order = 0)
    public void tearDown(Scenario scenario) {
        WebDriver driver = ctx.getDriver();
        if (driver == null) return;

        try {
            if (scenario.isFailed() && config.isScreenshotOnFailure()) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure Screenshot — " + scenario.getName());
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to take screenshot: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }

    private WebDriver buildDriver() {
        String  browser  = config.getBrowser().toLowerCase().trim();
        boolean headless = config.isHeadless();

        return switch (browser) {
            case "firefox" -> {
                // WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) opts.addArguments("--headless");
                yield new FirefoxDriver(opts);
            }
            case "edge" -> {
                EdgeOptions opts = new EdgeOptions();
                opts.addArguments("--remote-allow-origins=*");
                opts.addArguments("--no-sandbox");
                opts.addArguments("--disable-dev-shm-usage");
                opts.addArguments("--disable-gpu");

                if (headless) opts.addArguments("--headless");
                yield new EdgeDriver(opts);
            }
            default -> {
                // WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();

                opts.addArguments("--remote-allow-origins=*");
                opts.addArguments("--no-sandbox");
                opts.addArguments("--disable-dev-shm-usage");
                opts.addArguments("--disable-gpu");
                opts.addArguments("--disable-extensions");
                opts.addArguments("--disable-software-rasterizer");
                opts.addArguments("--start-maximized");

                opts.addArguments("--window-size=1920,1080");

                // opts.setPageLoadStrategy(PageLoadStrategy.EAGER);

                if (headless) {
                    opts.addArguments("--headless=new");
                }
                yield new ChromeDriver(opts);
            }
        };
    }
}