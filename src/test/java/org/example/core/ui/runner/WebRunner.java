package org.example.core.ui.runner;

import net.serenitybdd.cucumber.CucumberWithSerenity;
import net.thucydides.core.annotations.Managed;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(CucumberWithSerenity.class)
public class WebRunner {
    // chrome, firefox
    @Managed(driver = "chrome")
    WebDriver driver;

    @Test
    public void testAutothon() {

    }
}
