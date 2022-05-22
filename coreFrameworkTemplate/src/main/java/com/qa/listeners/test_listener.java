package com.qa.listeners;

import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.sql.SQLException;
import java.util.UUID;

import static com.qa.util.dbUtils.*;


public class test_listener implements ITestListener {

    //public static ExtentTest testReporter;
    public static ThreadLocal<ExtentTest> testReporter=new ThreadLocal<>();


    /*
    * Invoked before each Test Method. It does DB Reporting at test case level. Initializes Extent Test Node
    * and increments number of test cases executed by 1
    * @param result This test method's result object
    * */
    @Override
    public synchronized void onTestStart(ITestResult result) {

        String TCtitle = result.getMethod().getMethodName();

        try {

            //testReporter = suite_listener.suiteReporter.createNode(TCtitle);
            testReporter.set(suite_listener.suiteReporter.createNode(TCtitle));

            if(globalVariable.get("enableDB").equalsIgnoreCase("true")) {
                createTCEID(TCtitle);
            }
            else {
                //globalVariable.put("testcaseexecutionid", UUID.randomUUID().toString());
                TCExecID.set(UUID.randomUUID().toString());
            }
        }
        catch (Exception e) {
            System.out.println("Some issue with creating testcase ExecutionID or initializing Extent Test Node for " + TCtitle + ". Please contact Automation Team.");
            e.printStackTrace();
        }
        finally {
            TCExecuted = TCExecuted+1;
        }
    }


    /*
    * creates unique testcase executionid if test case is already inserted in the test case table otherwise it
    * first inserts the test case in test case table and then creates testcase executionid
    * @param TCTitle test case title
    * */
    private void createTCEID(String TCtitle) throws SQLException {

        String sql_getTCID = "Select * from testcase_tbl where TCTitle='"+TCtitle+"' and suiteid='" + globalVariable.get("suiteid") + "'";
        String sql_insertTC = "INSERT INTO testcase_tbl(suiteID, TCtitle) VALUES (" + globalVariable.get("suiteid") + ", '" + TCtitle + "')";

        //inserting test case in test case table
        String TCID = execute_statement(sql_getTCID,"testcaseid");

        //checking if test case is present in test case table
        //if not present, then test case is inserted in test case table
        if(TCID == null || TCID.isEmpty()) {
            TCID = insert_statement(sql_insertTC, sql_getTCID,"testcaseid");
        }

        //storing testcaseid in global variable
        globalVariable.put("testcaseid",TCID);

        //inserting a new role in testcaseexecution table and extracting testcaseexecutionid
        String sql_insertTCE = "INSERT INTO testcaseexecution_tbl " +
                "(testCaseid, executionID, TCstarttime, TCendtime, TCsnapshotpath, TCstatus, TCExecutiontime, TCPARAM)" +
                "VALUES (" + TCID + "," + globalVariable.get("executionID") + ", NOW(), NULL, '', '', '', '')";

        String sql_getTCExecutionID = "Select * from testcaseexecution_tbl where testcaseid="+TCID + " Order By testcaseExecID Desc LIMIT 1";
        String TCExecutionID = insert_statement(sql_insertTCE, sql_getTCExecutionID,"testcaseexecid");

        //storing testcaseexecutionid in global variable
        //globalVariable.put("testcaseexecutionid",TCExecutionID);
        TCExecID.set(TCExecutionID);
    }


    /*
    * Invoked on Test Method Success. It updates DB Reporting at test level and increments Test Case Passed by 1
    * @param result This Test Method's result object
    * */
    @Override
    public synchronized void onTestSuccess(ITestResult result) {

        String TCTitle = null;

        try {

            TCTitle = result.getMethod().getMethodName();

//            if(globalVariable.get("enableDB").equalsIgnoreCase("true")) {
//                String sql_updateTCE = "UPDATE testcaseexecution_tbl " +
//                        "SET TCendtime = NOW(), TCstatus = \"\\\"PASS\\\"\"" +
//                        ", TCExecutiontime = TIMEDIFF(NOW(),TCstarttime) WHERE testcaseExecID =" + globalVariable.get("testcaseexecutionid");

            if(globalVariable.get("enableDB").equalsIgnoreCase("true")) {
                String sql_updateTCE = "UPDATE testcaseexecution_tbl " +
                        "SET TCendtime = NOW(), TCstatus = \"\\\"PASS\\\"\"" +
                        ", TCExecutiontime = TIMEDIFF(NOW(),TCstarttime) WHERE testcaseExecID =" + TCExecID.get();

//                String sql_getTCExecutionID = "Select * from testcaseexecution_tbl where testcaseExecID="+globalVariable.get("testcaseexecutionid") + " Order By ExecutionID Desc LIMIT 1";
                String sql_getTCExecutionID = "Select * from testcaseexecution_tbl where testcaseExecID="+TCExecID.get() + " Order By ExecutionID Desc LIMIT 1";
                String TCExecutionID = insert_statement(sql_updateTCE, sql_getTCExecutionID,"testcaseexecid");
            }
        }
        catch (Exception e) {
            System.out.print("Some issue with updating DB at test level for " + TCTitle + ". Please contact Automation Team.");
            e.printStackTrace();
        }
        finally {
            TCPassed=TCPassed+1;
        }
    }


    /*
     * Invoked on Test Method Failure. It updates DB Reporting at test level and increments Test Case Failed by 1
     * @param result This Test Method's result object
     * */
    @Override
    public synchronized void onTestFailure(ITestResult result) {

        String TCTitle = null;

        try {

            TCTitle = result.getMethod().getMethodName();

            if(globalVariable.get("enableDB").equalsIgnoreCase("true")) {
//                String sql_updateTCE = "UPDATE testcaseexecution_tbl " +
//                        "SET TCendtime = NOW(), TCstatus = \"\\\"FAIL\\\"\"" +
//                        ", TCExecutiontime = TIMEDIFF(NOW(),TCstarttime) WHERE testcaseExecID =" + globalVariable.get("testcaseexecutionid");

                String sql_updateTCE = "UPDATE testcaseexecution_tbl " +
                        "SET TCendtime = NOW(), TCstatus = \"\\\"FAIL\\\"\"" +
                        ", TCExecutiontime = TIMEDIFF(NOW(),TCstarttime) WHERE testcaseExecID =" + TCExecID.get();

//                String sql_getTCExecutionID = "Select * from testcaseexecution_tbl where testcaseExecID="+globalVariable.get("testcaseexecutionid") + " Order By ExecutionID Desc LIMIT 1";
                String sql_getTCExecutionID = "Select * from testcaseexecution_tbl where testcaseExecID="+TCExecID.get() + " Order By ExecutionID Desc LIMIT 1";
                String TCExecutionID = insert_statement(sql_updateTCE, sql_getTCExecutionID,"testcaseexecid");
            }
        }
        catch (Exception e) {
            System.out.print("Some issue with updating DB at test level for " + TCTitle + ". Please contact Automation Team.");
            e.printStackTrace();
        }
        finally {
            TCFailed=TCFailed+1;
        }
    }


    /*
     * Invoked on Test Method Skip. It updates DB Reporting at test level and increments Test Case Skipped by 1
     * @param result This Test Method's result object
     * */
    @Override
    public synchronized void onTestSkipped(ITestResult result) {

        String TCTitle = null;

        try {

            TCTitle = result.getMethod().getMethodName();

            if(globalVariable.get("enableDB").equalsIgnoreCase("true")) {
//                String sql_updateTCE = "UPDATE testcaseexecution_tbl " +
//                        "SET TCendtime = NOW(), TCstatus = \"\\\"SKIP\\\"\"" +
//                        ", TCExecutiontime = TIMEDIFF(NOW(),TCstarttime) WHERE testcaseExecID =" + globalVariable.get("testcaseexecutionid");

                String sql_updateTCE = "UPDATE testcaseexecution_tbl " +
                        "SET TCendtime = NOW(), TCstatus = \"\\\"SKIP\\\"\"" +
                        ", TCExecutiontime = TIMEDIFF(NOW(),TCstarttime) WHERE testcaseExecID =" + TCExecID.get();

//                String sql_getTCExecutionID = "Select * from testcaseexecution_tbl where testcaseExecID="+globalVariable.get("testcaseexecutionid") + " Order By ExecutionID Desc LIMIT 1";
                String sql_getTCExecutionID = "Select * from testcaseexecution_tbl where testcaseExecID="+TCExecID.get() + " Order By ExecutionID Desc LIMIT 1";
                String TCExecutionID = insert_statement(sql_updateTCE, sql_getTCExecutionID,"testcaseexecid");
            }
        }
        catch (Exception e) {
            System.out.print("Some issue with updating DB at test level for " + TCTitle + ". Please contact Automation Team.");
            e.printStackTrace();
        }
        finally {
            TCSkipped=TCSkipped+1;
        }
    }


    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }


    @Override
    public void onTestFailedWithTimeout(ITestResult result) {

    }


    @Override
    public void onStart(ITestContext context) {

    }


    @Override
    public void onFinish(ITestContext context) {

    }
}
