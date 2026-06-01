package org.example.testRunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Full-suite runner with Allure reporting enabled.
 *
 * Run a subset locally without modifying this file:
 *   mvn test -Dcucumber.filter.tags="@smoke"
 *   mvn test -Dcucumber.filter.tags="@auth or @cart"
 *   mvn test -Dcucumber.filter.tags="not @negative"
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features  = "src/test/resources/features",
        glue      = "org.example.stepDefs",
        plugin    = {
                "pretty",
                "html:target/cucumber-reports/report.html",
                "json:target/cucumber-reports/report.json",
                "junit:target/cucumber-reports/report.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true,
        dryRun     = false
)
public class runner {
}