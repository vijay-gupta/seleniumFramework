package com.qa.listeners;

import java.sql.*;

import com.qa.util.dbUtils;
import org.testng.IExecutionListener;
import static com.qa.util.dbUtils.*;

public class execute_listener implements IExecutionListener {

    private Connection con;

    /*
    * Creates connection with mySQL DB
    * */
    public void onExecutionStart() {

        try {

            if(globalVariable.get("enableDB").equalsIgnoreCase("true")) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(
                        "jdbc:mysql://127.0.0.1:3306/rebit_executionstatus", "auto_admin", "Welcome@123");

                dbUtils.con = con;
                String statement_projectgroup = "Select * from projectgroup_tbl where projectgroupname='"+globalVariable.get("projectgroup")+"' LIMIT 1";
                String statement_project = "Select * from project_tbl where projectname='"+globalVariable.get("project")+"' LIMIT 1";

                String projectGroupID = execute_statement(statement_projectgroup,"projectgroupid");
                String projectID = execute_statement(statement_project,"projectid");

                globalVariable.put("projectgroupid", projectGroupID);
                globalVariable.put("projectid",projectID);
                System.out.println("Connection Created");
            }
        } catch (Exception e) {
            System.out.println("Connection Not Created");
            e.printStackTrace();
        }
        finally {
        }
    }


    /*
    * closes DB connection
    * */
    public void onExecutionFinish() {

        try {
            if(globalVariable.get("enableDB").equalsIgnoreCase("true")) {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        }
    }
}
