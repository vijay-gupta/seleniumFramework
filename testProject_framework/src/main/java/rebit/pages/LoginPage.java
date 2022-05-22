package rebit.pages;


import com.qa.base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends TestBase {
//    @FindBy(
//            xpath = "//input[@id='user-name']"
//    )
//    WebElement username;
//    @FindBy(
//            xpath = "//input[@id='password']"
//    )
//    WebElement password;
//    @FindBy(
//            xpath = "//input[@class='btn_action']"
//    )
//    WebElement submitBtn;

    @FindBy(
            xpath = "//input[@name='q']"
    )
    WebElement googleSearchBox;

    @FindBy(
            xpath = "//input[@name='q']"
    )
    WebElement bingSearchBox;

    @FindBy(
            xpath = "//input[@name='q']"
    )
    WebElement duckSearchBox;

    public LoginPage() {
        PageFactory.initElements(webDriver.get(), this);
    }

    public String getTitle() {
        return webDriver.get().getTitle();
    }

//    public HomePage Login(String uname, String pword) {
//        this.username.sendKeys(new CharSequence[]{uname});
//        this.password.sendKeys(new CharSequence[]{pword});
//        this.submitBtn.click();
//        return new HomePage();
//    }

    public void navigate(String url) {
        webDriver.get().get(url);
    }

    public boolean validateElement(String searchPage) {

        boolean elementExist = false;

        if (webDriver.get().findElement(By.xpath("//input[@name='q']")).isDisplayed()) {
            elementExist = true;
        }
        else {
            elementExist = false;
        }

        return elementExist;
    }
}
