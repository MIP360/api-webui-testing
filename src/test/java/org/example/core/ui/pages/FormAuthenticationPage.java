package org.example.core.ui.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.pages.PageObject;

public class FormAuthenticationPage extends PageObject {
    @FindBy(id = "username")
    WebElementFacade userNameTextField;

    @FindBy(id = "password")
    WebElementFacade passwordTextField;

    @FindBy(css = "#login > button")
    WebElementFacade loginButton;

    @FindBy(id="flash")
    WebElementFacade loginSuccessMessage;

    @FindBy(xpath= "//*[@id=\"content\"]/div/a")
    WebElementFacade logOutButton;


    public void enterUsername(String userNameStr){
        userNameTextField.clear();
        userNameTextField.type(userNameStr);
    }

    public void enterPassword(String passwordStr){
        passwordTextField.clear();
        passwordTextField.type(passwordStr);
    }

    public void clickLoginButton(){
        loginButton.click();
    }

    public String getFlashMessage(){
        return loginSuccessMessage.getText();
    }

    public void clickLogOutButton(){
        logOutButton.click();
    }

    public void openHomePage(String url) {
        openUrl(url);
    }

    public String getCurrentUrl(){
        return getDriver().getCurrentUrl();
    }
}
