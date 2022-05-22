package com.qa.util;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.qa.base.TestBase;
import com.qa.listeners.test_listener;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;


public class dbUtils extends TestBase {

    public static int TCExecuted = 0;
    public static int TCPassed = 0;
    public static int TCFailed = 0;
    public static int TCSkipped = 0;

    public static Connection con;
    public static ThreadLocal<String> snapshotFolderPath = new ThreadLocal<>();

    public dbUtils() {
        super();
    }


    /*
    * Inserts Step Validation in Step table, initializes Extent Step Node and takes snapshot
    * @param strDescription test step description
    * @param strExpected expected result
    * @param strActual actual result
    * @param strStatus PASS, FAIL or SKIP
    * */
    public static void DB_InsertStep (String strDescription, String strExpected, String strActual, Enum strStatus) {

        test_listener testListener = new test_listener();

        try {

            String command;
            ThreadLocal<String> trimmed_desc = new ThreadLocal<>();

            if(globalVariable.get("enableDB").equalsIgnoreCase("true")) {
//                String sql_insertStep = "INSERT INTO teststepexecution_tbl" +
//                        "(testcaseExecId, TSDescription, TSExpectedResult, TSTitle, TSActualResult, TSStatus, TSSnapshotPath, testSteptype, TSSnapshot) " +
//                        "VALUES ( '" + globalVariable.get("testcaseexecutionid") + "','" + strDescription + "','" + strExpected +  "', NULL,'" + strActual + "', " +
//                        "'" + strStatus + "' , 'NULL', NULL, NULL)";

                command = "INSERT INTO teststepexecution_tbl" +
                        "(testcaseExecId, TSDescription, TSExpectedResult, TSTitle, TSActualResult, TSStatus, TSSnapshotPath, testSteptype, TSSnapshot) " +
                        "VALUES ( '" + TCExecID.get() + "','" + strDescription + "','" + strExpected +  "', NULL,'" + strActual + "', " +
                        "'" + strStatus + "' , 'NULL', NULL, NULL)";

                String sql_getStep = "SELECT * FROM teststepexecution_tbl Order By teststepExecID Desc LIMIT 1";

                //inserting test step in test step table
                //trimmed_desc = insert_statement(command, sql_getStep, "teststepExecID");
                trimmed_desc.set(insert_statement(command, sql_getStep, "teststepExecID"));
                System.out.println("trimmed_desc: " + trimmed_desc);

                //storing teststepid in global variable
                //globalVariable.put("teststepid",trimmed_desc);
                globalVariable.put("teststepid",trimmed_desc.get());
            }

            ThreadLocal<String> strSafeDescription = new ThreadLocal<>();
            strSafeDescription.set(strDescription);
            createSnapshot(strSafeDescription);

            //disconnect virtual drive
            command = "C:\\Windows\\system32\\subst N: /D";
            Process child = Runtime.getRuntime().exec(command);
//            System.out.println("Exit Value: " + child.waitFor());

            //getting parent folder path and building snapshot path
            //trimmed_desc = globalVariable.get("trimmed_step");
            File file = new File (snapshotFolderPath.get());
            String finalFolderPath = file.getParentFile().getParentFile().getParentFile().getParent();
            //String finalSnapshotPath = "N:\\" + globalVariable.get("executionID") + File.separator + TCExecID.get()
            //+ File.separator + trimmed_desc + File.separator + trimmed_desc + ".png";
            String finalSnapshotPath = "N:\\" + globalVariable.get("executionID") + File.separator + TCExecID.get()
            + File.separator + strSafeDescription.get() + File.separator + strSafeDescription.get() + ".png";
            globalVariable.put("finalFolderPath",finalFolderPath);

            /*System.out.println("Snapshot Folder Path:" + finalFolderPath);
            System.out.println("Snapshot Folder Path Length:" + finalFolderPath.length());
            System.out.println("Snapshot Path:" + finalSnapshotPath);
            System.out.println("Snapshot Path Length:" + finalSnapshotPath.length());*/

            //map virtual drive
            mapVirtualDrive(finalFolderPath,1);

            if (strStatus.equals(RESULT.PASS)) {
                //testListener.testReporter.get().createNode(strDescription).pass(strActual,MediaEntityBuilder.createScreenCaptureFromPath(snapshotFolderPath.get()).build());
                testListener.testReporter.get().createNode(strDescription).pass(strActual,MediaEntityBuilder.createScreenCaptureFromPath(finalSnapshotPath).build());
                Assert.assertTrue(true);
            }
            else {
                //testListener.testReporter.get().createNode(strDescription).fail(strActual,MediaEntityBuilder.createScreenCaptureFromPath(snapshotFolderPath.get()).build());
                testListener.testReporter.get().createNode(strDescription).fail(strActual,MediaEntityBuilder.createScreenCaptureFromPath(finalSnapshotPath).build());
                Assert.assertTrue(false);
            }

        }
        catch (Exception e) {
            System.out.println("Some issue with inserting step, creating snapshot or updating test extent node for " + strDescription + ". Please contact Automation Team.");
            e.printStackTrace();
        }
    }

    /*
    * map folder to virtual drive
    * @param folderPath folder path to be mapped to virtual drive
    * @param timeToWait time to wait in seconds
    * */
    public static void mapVirtualDrive(String folderPath, int timeToWait) throws InterruptedException, IOException {
        String command = "C:\\Windows\\system32\\subst N: " + "\"" + folderPath + "\"";
        Process child = Runtime.getRuntime().exec(command);
        Thread.sleep(timeToWait*1000);
//            System.out.println("Exit Value: " + child.waitFor());
    }


    /*
    * creates folder structure, takes snapshot and stores snapshot in appropriate folder structure
    * @param strDesc test step description
    * */
    private static void createSnapshot(ThreadLocal<String> strDesc) {

//        String snapshotFolderPath = null;
//        snapshotFolderPath = System.getProperty("user.dir") + "\\" + globalVariable.get("snapshot_folder") + File.separator + globalVariable.get("executionID") + File.separator +
//                    globalVariable.get("testcaseexecutionid") + File.separator + strDesc;

        snapshotFolderPath.set(System.getProperty("user.dir") + File.separator + globalVariable.get("snapshot_folder") +
                File.separator + globalVariable.get("executionID") + File.separator + TCExecID.get() +
                File.separator + strDesc.get());


        //getting parent folder path and building snapshot path
        /*File file = new File (snapshotFolderPath.get()+ File.separator + strDesc + ".png");*/
        File file = new File (snapshotFolderPath.get()+ File.separator + strDesc.get() + ".png");

        String finalFolderPath = file.getParent();

        /*String finalSnapshotPath = "N:\\" + File.separator + globalVariable.get("executionID") + File.separator +
                TCExecID.get() + File.separator + strDesc + File.separator + strDesc + ".png";*/
        String finalSnapshotPath = "N:\\" + File.separator + globalVariable.get("executionID") + File.separator +
                TCExecID.get() + File.separator + strDesc.get() + File.separator + strDesc.get() + ".png";

        /*System.out.println("Snapshot Folder Path:" + finalFolderPath);
        System.out.println("Snapshot Folder Path Length:" + finalFolderPath.length());
        System.out.println("Snapshot Path:" + finalSnapshotPath);
        System.out.println("Snapshot Path Length:" + finalSnapshotPath.length());*/

        int diffChars;

        if (finalFolderPath.length() >= 260) {
            diffChars = finalFolderPath.length() - 259;
            //strDesc = strDesc.substring(0,strDesc.length()-diffChars).trim();
            strDesc.set(strDesc.get().substring(0, strDesc.get().length()-diffChars).trim());

            /*snapshotFolderPath.set(System.getProperty("user.dir") + File.separator +
                    globalVariable.get("snapshot_folder") + File.separator + globalVariable.get("executionID") +
                    File.separator + TCExecID.get() + File.separator + strDesc);*/

            snapshotFolderPath.set(System.getProperty("user.dir") + File.separator +
                    globalVariable.get("snapshot_folder") + File.separator + globalVariable.get("executionID") +
                    File.separator + TCExecID.get() + File.separator + strDesc.get());

            //getting parent folder path and updating snapshot path
            /*file = new File (snapshotFolderPath.get()+ File.separator + strDesc + ".png");*/
            file = new File (snapshotFolderPath.get()+ File.separator + strDesc.get() + ".png");
            finalFolderPath = file.getParent();
            /*finalSnapshotPath = "N:\\" + File.separator + globalVariable.get("executionID") + File.separator +
                    TCExecID.get() + File.separator + strDesc + File.separator + strDesc + ".png";*/

            finalSnapshotPath = "N:\\" + File.separator + globalVariable.get("executionID") + File.separator +
                    TCExecID.get() + File.separator + strDesc.get() + File.separator + strDesc.get() + ".png";

            /*System.out.println("Stats after trimming Test Step Name");
            System.out.println("Snapshot Folder Path:" + finalFolderPath);
            System.out.println("Snapshot Folder Path Length:" + finalFolderPath.length());
            System.out.println("Snapshot Path:" + finalSnapshotPath);
            System.out.println("Snapshot Path Length:" + finalSnapshotPath.length());*/
        }


        if (finalSnapshotPath.length() > 258) {
            diffChars = finalSnapshotPath.length() - 258;
            /*strDesc = strDesc.substring(0,strDesc.length()-4-diffChars).trim() ;*/
            strDesc.set(strDesc.get().substring(0,strDesc.get().length()-4-diffChars).trim()) ;

            /*snapshotFolderPath.set(System.getProperty("user.dir") + File.separator +
                    globalVariable.get("snapshot_folder") + File.separator + globalVariable.get("executionID") +
                    File.separator + TCExecID.get() + File.separator + strDesc);*/

            snapshotFolderPath.set(System.getProperty("user.dir") + File.separator +
                    globalVariable.get("snapshot_folder") + File.separator + globalVariable.get("executionID") +
                    File.separator + TCExecID.get() + File.separator + strDesc.get());

            //getting parent folder path and updating snapshot path
            /*file = new File (snapshotFolderPath.get()+ File.separator + strDesc + ".png");*/
            file = new File (snapshotFolderPath.get()+ File.separator + strDesc.get() + ".png");
            finalFolderPath = file.getParent();
            /*finalSnapshotPath = "N:\\" + File.separator + globalVariable.get("executionID") + File.separator +
                    TCExecID.get() + File.separator + strDesc + File.separator + strDesc + ".png";*/

            finalSnapshotPath = "N:\\" + File.separator + globalVariable.get("executionID") + File.separator +
                    TCExecID.get() + File.separator + strDesc.get() + File.separator + strDesc.get() + ".png";

            /*System.out.println("Stats after trimming further");
            System.out.println("Snapshot Folder Path:" + finalFolderPath);
            System.out.println("Snapshot Folder Path Length:" + finalFolderPath.length());
            System.out.println("Snapshot Path:" + finalSnapshotPath);
            System.out.println("Snapshot Path Length:" + finalSnapshotPath.length());*/
        }


        globalVariable.put("trimmed_step", strDesc.get());
        String snapshotFilePath = null;
        boolean isDirectoryCreated = (new File(snapshotFolderPath.get())).mkdirs();

        if(isDirectoryCreated) {
//            snapshotFilePath = snapshotFolderPath + File.separator + strDesc + ".png";
//            globalVariable.put("snapshotFilePath", snapshotFilePath);
//            takeSnapShot(snapshotFilePath);

            /*snapshotFolderPath.set(snapshotFolderPath.get() + File.separator + strDesc + ".png");*/
            snapshotFolderPath.set(snapshotFolderPath.get() + File.separator + strDesc.get() + ".png");
            globalVariable.put("snapshotFilePath", snapshotFolderPath.get());
            takeSnapShot(snapshotFolderPath);

        }
        else {
            System.out.println("Snapshot not created. Please take a look");
        }
    }


    /*
    * updates test step description with markup in Extent Report
    * @param strStatus status of the test step PASS, FAIL or SKIP
    * @param strText test step description
    * */
    public static Markup getMarkUp(Enum strStatus, String strText) {

        Markup objMark;

        if (strStatus.equals(RESULT.PASS)) {
            objMark = MarkupHelper.createLabel(strText, ExtentColor.GREEN);
        }
        else {
            objMark = MarkupHelper.createLabel(strText, ExtentColor.RED);
        }

        return objMark;
    }


    /*
    * executes sql statement passed to it and fetches the value of the passed column from the result
    * @param sqlStatement sql statement to be executed
    * @param colName column name
    * @return value of colName in the result
    * */
    public static String execute_statement(String sqlStatement, String colName) throws SQLException {

        Statement stmt = null;
        ResultSet rs = null;
        String data = null;

        stmt = con.createStatement();
        rs=stmt.executeQuery(sqlStatement);

        if (rs.next() == false) {
            System.out.println("ResultSet in empty in Java");
        }
        else {
            do {
                data = rs.getString(colName);
            } while (rs.next());
        }

        return data;
    }


    /*
    * executes sql update statement passed to it and fetches the value of the passed column from the result after
    * executing select statement passed to it
    * @param sqlUpdateStatement sql update statement to be executed
    * @param sqlSelectStatement sql select statement to be executed
    * @param colName column name
    * @return value of column colName in the result
    *
    * */
    public static String insert_statement(String sqlUpdateStatement, String sqlSelectStatement, String colName) throws  SQLException {

        Statement stmt;
        String colValue = null;

        stmt = con.createStatement();
        int rowsChanged=stmt.executeUpdate(sqlUpdateStatement);

        if(rowsChanged!=0) {
            colValue = execute_statement(sqlSelectStatement,colName);
        }
        else {
            System.out.println("0 rows are inserted/updated");
        }

        return colValue;
    }


    /*
    * fetches hostname
    * @return hostname
    * */
    public static String getHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }


    /*
     * fetches hostusername
     * @return hostusername
     * */
    public static String getHostUserName() {
        return System.getProperty("user.name");
    }
}
