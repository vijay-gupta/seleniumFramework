package com.qa.test.Web;

import com.qa.base.TestBase;
import com.qa.pages.LoginPage;
import com.qa.util.dbUtils;
import org.testng.annotations.CustomAttribute;
import org.testng.annotations.Test;

public class LoginPageTest extends TestBase {

    LoginPage loginPage = new LoginPage();
    dbUtils dbUtils = new dbUtils();

    public LoginPageTest() {
        super();
    }


    @Test(enabled = false,attributes = {@CustomAttribute(name = "data-driven", values = {"testdata/excel.xlsx"})})
    public void dataDriven1(String URL) {

        loginPage.navigate(URL);
        String actualURL = loginPage.getURL();

        if (actualURL.toLowerCase().contains(URL)) {
            dbUtils.DB_InsertStep("Verify Page URL","URL should match", "URL matches. URL is: " + actualURL,RESULT.PASS);
        }
        else {
            dbUtils.DB_InsertStep("Verify Page URL","URL should match", "URL does not matches. URL is: " + actualURL,RESULT.FAIL);
        }
    }


    //@Test(attributes = {@CustomAttribute(name = "data-driven", values = {"testdata/data.xlsx"})})
    public void dataDriven2(String URL) {

        loginPage.navigate(URL);
        String actualURL = loginPage.getURL();

        if (actualURL.toLowerCase().contains(URL)) {
            dbUtils.DB_InsertStep("Verify Page URL","URL should match", "URL matches",RESULT.PASS);
        }
        else {
            dbUtils.DB_InsertStep("Verify Page URL","URL should match", "URL does not matches. URL is: " + actualURL,RESULT.FAIL);
        }
    }


    @Test (enabled = true)
    public void validateGoogleTitle() {

        loginPage.navigate("https://www.google.com");
        String title = loginPage.getTitle();

        if (title.equalsIgnoreCase("Google")) {
            dbUtils.DB_InsertStep("Verify Google Title","Title should be Google", "title matches",RESULT.PASS);
        }
        else {
            dbUtils.DB_InsertStep("Verify Google Title","Title should be Google", "title does not matches. Title is: " + title,RESULT.FAIL);
        }
    }


    @Test (enabled = true)
    public void validateBingTitle() {

        loginPage.navigate("https://www.bing.com");
        String title = loginPage.getTitle();

        if (title.equalsIgnoreCase("Bing")) {
            dbUtils.DB_InsertStep("Verify Bing Title","Title should be Bing", "title matches",RESULT.PASS);
        }
        else {
            dbUtils.DB_InsertStep("Verify Bing Title","Title should be Bing", "title does not matches. Title is: " + title,RESULT.FAIL);
        }
    }


    @Test (enabled = true)
    public void validateDuckDuckGoTitle() {

        loginPage.navigate("https://www.duckduckgo.com");
        String title = loginPage.getTitle();

        if (title.equalsIgnoreCase("DuckDuckGo — Privacy, simplified.")) {
            dbUtils.DB_InsertStep("Verify DuckDuckGo Title","Title should be DuckDuckGo — Privacy, simplified.", "title matches",RESULT.PASS);
        }
        else {
            dbUtils.DB_InsertStep("Verify DuckDuckGo Title","Title should be DuckDuckGo — Privacy, simplified.", "title does not matches. Title is: " + title,RESULT.FAIL);
        }
    }
}
