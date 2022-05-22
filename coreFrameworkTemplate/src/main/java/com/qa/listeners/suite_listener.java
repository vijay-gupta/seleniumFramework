package com.qa.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.qa.reports.extentReports;
import com.qa.util.emailUtils;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import static com.qa.util.dbUtils.*;


public class suite_listener implements ISuiteListener {

    private static ExtentReports extent;
    static ExtentTest suiteReporter;


    /*
    * Invoked before each suite is invoked. It does DB Reporting at suite level. Initializes Extent Suite Node
    * and sends execution start notification mail
    * @param suite This suite's object
    * */
    public void onStart(ISuite suite) {

        String hostname=null;
        String hostusername=null;
        String suiteName=null;

        try {

            hostname = getHostName();
            globalVariable.put("hostname",hostname);
            hostusername = getHostUserName();
            suiteName = suite.getName();

            //setting parallel thread count
            suite.getXmlSuite().setParallel(XmlSuite.ParallelMode.INSTANCES);
            suite.getXmlSuite().setThreadCount(Integer.parseInt(globalVariable.get("threadCount")));

            //initializing Extent Suite Node
            createExtentSuiteNode(hostname, hostusername, suiteName);

            if(globalVariable.get("enableDB").equalsIgnoreCase("true")) {
                createExecutionID(suiteName, hostname, hostusername);
            }
            else {
                globalVariable.put("executionID", UUID.randomUUID().toString());
            }

        } catch (Exception e) {
            System.out.println("Some issue with creating ExecutionID or initializing Extent Suite Node for " + suiteName + ". Please contact Automation Team.");
            e.printStackTrace();
        }
        finally {
            if(globalVariable.get("enableMail").equalsIgnoreCase("true")) {
                sendStartUpMail(suiteName,hostname);
            }
        }
    }


    /*
    * initializes Extent Suite Node
    * @param hostName name of the host on which execution is happening
    * @param hostusername name of the user with which the host is logged in
    * @param suiteName name of the suite which is executed
    * */
    private void createExtentSuiteNode(String hostname, String hostusername, String suiteName) {

        extent = extentReports.getReporter(globalVariable.get("report_path"));
        extent.setSystemInfo("host",hostname);
        extent.setSystemInfo("user",hostusername);
        extent.setSystemInfo("suite",suiteName);
        extent.setSystemInfo("browser",globalVariable.get("browser"));
        suiteReporter = extent.createTest(suiteName);
    }


    /*
    * creates unique ExecutionID
    * @param suiteName name of the suite which is executed
    * @param hostName name of the host on which execution is happening
    * @param hostusername name of the user with which the host is logged in
    * */
    private void createExecutionID(String suiteName, String hostname, String hostusername) throws SQLException {

        String suiteID = null;

        String sql_selectSuite = "Select * from testsuite_tbl where suitename='" + suiteName + "' LIMIT 1";
        suiteID = execute_statement(sql_selectSuite, "suiteid");

        if (suiteID != "" || suiteID != null) {

            String sql_insertTE = "INSERT INTO testexecution_tbl\n" +
                    "(suiteid, environment, browser, testhost, TEstarttime, TEendtime, executeuser, buildnumber, TEexecutiontime, TCExecuted, TCPassed, TCFailed, TCInconclusive, DataType, frameworkVersion, latestFramework, ThreadCount)\n" +
                    "VALUES (" + suiteID +
                    ", '" + globalVariable.get("environment") +
                    "', '" + globalVariable.get("browser") +
                    "', '" + hostname +
                    "', NOW(), NULL" +
                    ", '" + hostusername +
                    "', '', NULL, 0, 0, 0, 0, '', 'NULL', 0, 0)";

            String sql_getExecutionID = "Select * from testexecution_tbl where suiteid=" + suiteID + " Order By ExecutionID Desc LIMIT 1";
            String executionID = insert_statement(sql_insertTE, sql_getExecutionID, "executionid");
            globalVariable.put("suiteid", suiteID);
            globalVariable.put("executionID", executionID);
        }
    }


    /*
    * Sends startup mail
    * @param suiteName name of the suite which is executed
    * @param hostname name of the host on which execution is happening
    * return SUCCESS or FAILURE
    * */
    private String sendStartUpMail(String suiteName, String hostname) {

        String status = "FAILURE";

        try {
            status = emailUtils.startMail(hostname, globalVariable.get("smtpServer"),globalVariable.get("emailTo"),globalVariable.get("emailCC"),
                    suiteName,globalVariable.get("environment"));
        }
        catch (Exception e) {
            System.out.println("Start up Email not sent. Some Error Occurred: " + e.getMessage());
            e.printStackTrace();
        }

        return status;

    }

    /*
    * Invoked after suite is finished. It updates DB Reporting at suite level, flushes Extent Report and
    * sends Execution Complete Notification mail
    * @param suite This suite's object
    * */
    public void onFinish(ISuite suite) {

        String executionTime = null;

        try {

            //map virtual drive
            mapVirtualDrive(globalVariable.get("finalFolderPath"),10);

            //flush Extent Report
            extentReports.flushExtent();

            if(globalVariable.get("enableDB").equalsIgnoreCase("true")) {
                executionTime = updateTE();
            }
        }
        catch (Exception e) {
            System.out.println("Some issue with updating Execution in DB or flushing Extent Report. Please contact Automation Team.");
            e.printStackTrace();
        }
        finally {
            if(globalVariable.get("enableMail").equalsIgnoreCase("true")) {
                sendTeardownMail(suite.getName(),globalVariable.get("hostname"),executionTime);
            }
        }
    }


    /*
    * update testExecution table
    * */
    private String updateTE() throws SQLException {
        String executionTime = "0";
        String sql_updateTE = "UPDATE testexecution_tbl " +
            "SET TEendtime = NOW(), TEexecutiontime = TIMEDIFF(NOW(),TEstarttime),TCExecuted=" + TCExecuted + ", TCPassed=" + TCPassed +
            ", TCFailed=" + TCFailed + ", TCInconclusive=" + TCSkipped +
            " WHERE executionid =" + globalVariable.get("executionID") ;

        String sql_getExecutionID = "Select TIME_TO_SEC(TEexecutiontime) from testexecution_tbl where suiteid="+globalVariable.get("suiteid")+ " Order By ExecutionID Desc LIMIT 1";
        executionTime = insert_statement(sql_updateTE, sql_getExecutionID,"TIME_TO_SEC(TEexecutiontime)");
        return executionTime;
    }


    /*
    * Sends TearDown mail
    * @param suiteName name of the suite which is executed
    * @param hostname name of the host on which execution is happening
    * @param executionTime execution time of the suite in minutes
    * @return SUCCESS or FAILURE
    * */
    private String sendTeardownMail(String suiteName, String hostname, String executionTime) {

        String status = "FAILURE";

        try {
            if(globalVariable.get("enableDB").equalsIgnoreCase("true")) {
                status=emailUtils.endMail(hostname, globalVariable.get("smtpServer"),globalVariable.get("emailTo"),globalVariable.get("emailCC"),
                        suiteName, globalVariable.get("environment"), TCExecuted, TCPassed, TCFailed, TCSkipped, Integer.parseInt(executionTime),System.getProperty("user.dir") + File.separator + globalVariable.get("report_path"));
                return status;
            }
            else {
                status=emailUtils.endMail(hostname, globalVariable.get("smtpServer"),globalVariable.get("emailTo"),globalVariable.get("emailCC"),
                        suiteName, globalVariable.get("environment"), TCExecuted, TCPassed, TCFailed, TCSkipped, Integer.parseInt("1"),System.getProperty("user.dir") + File.separator + globalVariable.get("report_path"));
                return status;
            }
        }
        catch (Exception e) {
            System.out.println("Teardown Email not sent. Some Error Occurred: " + e.getMessage());
            e.printStackTrace();
        }

        return status;

    }
}
