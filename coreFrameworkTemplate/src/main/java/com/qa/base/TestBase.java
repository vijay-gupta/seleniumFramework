package com.qa.base;

import com.qa.util.excelUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TestBase {

    public static HashMap<String,String> globalVariable=new HashMap<String, String>();
    public static ThreadLocal<String> excelFilePath = new ThreadLocal<>();
    public static ThreadLocal<String> TCExecID = new ThreadLocal<>();
    protected static ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
    protected static enum RESULT { PASS,FAIL,SKIPPED }
    private static WebDriver driver;
    private static Properties prop;
    private static ThreadLocal<String> packName = new ThreadLocal<>();


    public TestBase() {

        try {
            prop = new Properties();
            FileInputStream ip = new FileInputStream(
                    System.getProperty("user.dir") + File.separator + "config.properties");
            prop.load(ip);

            for (String key : prop.stringPropertyNames()) {
                String value = prop.getProperty(key);
                globalVariable.put(key, value);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeMethod
    public static synchronized void initialize(Method method) {

        String [] packageArray = (method.getDeclaringClass().getPackage().getName()).split("\\.");
        String packageName = packageArray[packageArray.length-1];
        packName.set(packageName);
        globalVariable.put("packageName",packageName);

        if (!packageName.equalsIgnoreCase("api")) {
            String implicitWait = globalVariable.get("implicitWait");
            String pageTimeout = globalVariable.get("pageTimeout");

            driver = openBrowser();
            webDriver.set(driver);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Integer.parseInt(implicitWait), TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(Integer.parseInt(pageTimeout),TimeUnit.SECONDS);
        }
    }


        @DataProvider()
        public Object [][] getDataDriven() {
            excelUtils excelUtils = new excelUtils(System.getProperty("user.dir") + File.separator
                    + excelFilePath.get());
            Object data[][] = excelUtils.getTestData("Sheet1");
            return data;
        }


    @AfterMethod
    public static synchronized void terminate(Method method) {

        if (!packName.get().equalsIgnoreCase("api")) {
            driver = webDriver.get();
            driver.quit();
        }
    }

    protected static WebDriver openBrowser() {

        String browserName = globalVariable.get("browser");
        String driverVersion = globalVariable.get("driverVersion");

        if(browserName.equalsIgnoreCase("chrome")) {

            if(driverVersion.isEmpty()) {
                WebDriverManager.chromedriver().setup();
            }
            else {
                WebDriverManager.chromedriver().version(driverVersion).setup();
            }

            driver = new ChromeDriver();
        }
        else if(browserName.equalsIgnoreCase("firefox")) {

            if (driverVersion.isEmpty()) {
                WebDriverManager.firefoxdriver().setup();
            }
            else {
                WebDriverManager.firefoxdriver().version(driverVersion).setup();
            }

            driver = new FirefoxDriver();
        }
        else if(browserName.equalsIgnoreCase("edge")) {

            if (driverVersion.isEmpty()) {
                WebDriverManager.edgedriver().setup();
            }
            else {
                WebDriverManager.edgedriver().version(driverVersion).setup();
            }

            driver = new EdgeDriver();
        }

        return driver;
    }


    protected static WebDriver getDriver() {
        return webDriver.get();
    }


    protected static void takeSnapShot(ThreadLocal<String> snapshotPath) {

        try {
            if (!packName.get().equalsIgnoreCase("api")) {

                TakesScreenshot scrShot =((TakesScreenshot)(webDriver.get()));
                File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
                File DestFile=new File(snapshotPath.get());
                FileUtils.copyFile(SrcFile, DestFile);
            }
        }
        catch (Exception e) {
            System.out.println("Thread ID:" + Thread.currentThread().getId());
            System.out.println("Error occurred while taking snapshot: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
