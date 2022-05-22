package com.qa.reports;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import java.io.File;

public class extentReports {

    public static ExtentHtmlReporter htmlReporter;
    public static ExtentReports extent;

    public static ExtentReports getReporter(String relativePath) {
        if (extent == null) {

            htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + File.separator + relativePath);
            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);

            htmlReporter.loadXMLConfig(System.getProperty("user.dir") + File.separator + "extent-config.xml");
            extent.setAnalysisStrategy(AnalysisStrategy.SUITE);
        }

        return extent;
    }

    public static void flushExtent() {
        extent.flush();
    }


}
