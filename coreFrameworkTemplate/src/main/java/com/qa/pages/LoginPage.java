package com.qa.pages;

import com.qa.base.TestBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends TestBase {

    @FindBy(xpath = "//input[@id='user-name']")
    WebElement username;

    @FindBy(xpath = "//input[@id='password']")
    WebElement password;

    @FindBy(xpath = "//input[@class='btn_action']")
    WebElement submitBtn;

    public LoginPage() {
        super();
        PageFactory.initElements(webDriver.get(),this);
    }

    public String getTitle() {
        return webDriver.get().getTitle();
    }

    public String getURL() {
        return webDriver.get().getCurrentUrl();
    }

    public HomePage Login(String uname, String pword) {
        username.sendKeys(uname);
        password.sendKeys(pword);
        submitBtn.click();
        return new HomePage();
    }

    public void navigate(String url) {
        webDriver.get().get(url);
    }
}
