package org.example.core.ui.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.steps.ScenarioSteps;
import net.thucydides.core.util.EnvironmentVariables;
import org.example.core.ui.pages.FormAuthenticationPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UIBaseSteps extends ScenarioSteps {

    private EnvironmentVariables environmentVariables;
    private String url =  null;

    private FormAuthenticationPage formAuthenticationPage;

    @Before
    public void setUp(){

        this.url =  EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("ui.base.url");

    }

    @Given("^\\[ui] form-authentication page is opened$")
    public void ui_form_authentication_page_opened() {
        formAuthenticationPage.openHomePage(this.url);
    }

    @And("^\\[ui] set username to '(.*)'$")
    public void ui_set_username(String userName) {
        formAuthenticationPage.enterUsername(userName);
    }

    @And("^\\[ui] set password to '(.*)'$")
    public void ui_set_password(String password) {
        formAuthenticationPage.enterPassword(password);
    }

    @And("^\\[ui] click on login button$")
    public void ui_click_login() {
        formAuthenticationPage.clickLoginButton();
    }

    @And("^\\[ui] click on logout button$")
    public void ui_click_logout() {
        formAuthenticationPage.clickLogOutButton();
    }

    @And("^\\[ui] verify '(.*)' message:'(.*)'$")
    public void ui_veirfy_flash_message(String extraString,String expectedMessage) {
        String actualMessage = formAuthenticationPage.getFlashMessage();
        actualMessage = actualMessage.trim();

        if(actualMessage.contains(expectedMessage)){
            assertTrue(true);
        }else{
            assertTrue(false);
        }
    }

    @And("^\\[ui] verify '(.*)' url contains:'(.*)'$")
    public void ui_veirfy_page_urle(String extraString,String expectedUrl) {
        String actualUrl = formAuthenticationPage.getCurrentUrl();
        actualUrl = actualUrl.trim();

        if(actualUrl.contains(expectedUrl)){
            assertTrue(true);
        }else{
            assertTrue(false);
        }
    }
}
