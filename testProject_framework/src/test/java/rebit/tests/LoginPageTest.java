package rebit.tests;

import com.qa.base.TestBase;
import com.qa.pages.HomePage;
import com.qa.util.dbUtils;
import org.testng.annotations.Test;
import rebit.pages.LoginPage;

public class LoginPageTest extends TestBase {

    LoginPage loginPage = new LoginPage();
    HomePage homePage;
    dbUtils dbUtils = new dbUtils();

    public LoginPageTest() {
        super();
    }

    @Test(priority = 1, enabled=true)
    public void validateGoogleTitle() {

        try {
            loginPage.navigate("https://www.google.com");
            String title = loginPage.getTitle();

            if (title.equalsIgnoreCase("Google")) {
                dbUtils.DB_InsertStep("Verify Google Title","Title should be Google", "title matches",RESULT.PASS);
            }
            else {
                dbUtils.DB_InsertStep("Verify Google Title","Title should be Google", "title does not matches. Title is: " + title,RESULT.FAIL);
            }

            if (loginPage.validateElement("Google")==true) {
                dbUtils.DB_InsertStep("Verify Google Search Box","Search Box should be present", "Search Box present",RESULT.PASS);
            }
            else {
                dbUtils.DB_InsertStep("Verify Google Search Box","Search Box should be present", "Search Box not present",RESULT.FAIL);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test(enabled = true)
    public void validateBingTitle() {

        loginPage.navigate("https://www.bing.com");
        String title = loginPage.getTitle();

        if (title.equalsIgnoreCase("Bing")) {
            dbUtils.DB_InsertStep("Verify Bing Title","Title should be Bing", "title matches",RESULT.PASS);
        }
        else {
            dbUtils.DB_InsertStep("Verify Bing Title","Title should be Bing", "title does not matches. Title is: " + title,RESULT.FAIL);
        }

        if (loginPage.validateElement("Bing")==true) {
            dbUtils.DB_InsertStep("Verify Bing Search Box","Search Box should be present", "Search Box present",RESULT.PASS);
        }
        else {
            dbUtils.DB_InsertStep("Verify Bing Search Box","Search Box should be present", "Search Box not present",RESULT.FAIL);
        }
    }


    @Test(enabled = true)
    public void validateDuckDuckGoTitle() {

        loginPage.navigate("https://www.duckduckgo.com");
        String title = loginPage.getTitle();

        if (title.equalsIgnoreCase("DuckDuckGo — Privacy, simplified.")) {
            dbUtils.DB_InsertStep("Verify DuckDuckGo Title","Title should be DuckDuckGo — Privacy, simplified.", "title matches",RESULT.PASS);
        }
        else {
            dbUtils.DB_InsertStep("Verify DuckDuckGo Title","Title should be DuckDuckGo — Privacy, simplified.", "title does not matches. Title is: " + title,RESULT.FAIL);
        }

        if (loginPage.validateElement("DuckDuckGo")==true) {
            dbUtils.DB_InsertStep("Verify DuckDuckGo Search Box","Search Box should be present", "Search Box present",RESULT.PASS);
        }
        else {
            dbUtils.DB_InsertStep("Verify DuckDuckGo Search Box","Search Box should be present", "Search Box not present",RESULT.FAIL);
        }
    }
}
