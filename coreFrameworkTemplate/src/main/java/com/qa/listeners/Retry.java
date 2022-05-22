package com.qa.listeners;

import com.qa.base.TestBase;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry extends TestBase implements IRetryAnalyzer {

    private int count = 0;
    private static int maxTry = Integer.parseInt(globalVariable.get("failure_rerun")); //Run the failed test 2 times

    @Override
    public boolean retry(ITestResult iTestResult) {

        if (!iTestResult.isSuccess()) {                      //Check if test not succeed
            if (count < maxTry) {                            //Check if maxTry count is reached
                count++;                                     //Increase the maxTry count by 1
                iTestResult.setStatus(ITestResult.FAILURE);  //Mark test as failed and take base64Screenshot
                //takeSnapShot(iTestResult);    //ExtentReports fail operations
                return true;                                 //Tells TestNG to re-run the test
            }
        }
        else {
            iTestResult.setStatus(ITestResult.SUCCESS);      //If test passes, TestNG marks it as passed
        }
        return false;
    }

    /*public static void takeSnapShot(ITestResult iTestResult) throws Exception{

        String fileWithPath = null;

        fileWithPath =
        Object testClass = iTestResult.getInstance();
        WebDriver webDriver = ((TestBase) testClass).getDriver();

        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot =((TakesScreenshot)webDriver);

        //Call getScreenshotAs method to create image file
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);

        //Move image file to new destination
        File DestFile=new File(fileWithPath);

        //Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);

    }
*/}
