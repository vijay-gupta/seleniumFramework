package com.qa.test.API;

import com.qa.base.TestBase;
import com.qa.util.api.configUtils;
import com.qa.util.api.requestbuilder;
import com.qa.util.dbUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

public class sampleAPITest extends TestBase {

    @Test (enabled = true)
    public void sampleGetCall() {

        Map<String, Object> service_desc = configUtils.getServiceDescription(
                "src\\main\\java\\com\\qa\\services\\service_description\\Sample_Functionality\\sample_func_desc.yml",
                "sample_func_sub1");

        String request_url = (String)service_desc.get("target_url") + service_desc.get("endpoint");
        String request_method = (String)service_desc.get("method");
        Map <String, Object> request_headers = (Map<String, Object>) service_desc.get("headers");
        Response response = requestbuilder.send_request(request_method, request_url, request_headers, "");
        int statusCode = requestbuilder.get_response_statuscode(response);

        if(statusCode== 200) {
            dbUtils.DB_InsertStep("Verify GET Call","Call should be successful", "GET Call is successful", RESULT.PASS);
        }
        else {
            dbUtils.DB_InsertStep("Verify GET Call","Call should be successful", "GET Call returns " + statusCode, RESULT.FAIL);
        }
    }


    @Test(enabled = true)
    public void samplePostCall() {

        Map<String, Object> service_desc = configUtils.getServiceDescription(
                "src\\main\\java\\com\\qa\\services\\service_description\\Sample_Functionality\\sample_func_desc.yml",
                "sample_func_sub3");

        String request_url = (String)service_desc.get("target_url") + service_desc.get("endpoint");
        String request_method = (String)service_desc.get("method");
        Map <String, Object> request_headers = (Map<String, Object>) service_desc.get("headers");
        String payloadPath = (String)service_desc.get("payload");
        Response response = requestbuilder.send_request(request_method, request_url, request_headers, payloadPath);
        int statusCode = requestbuilder.get_response_statuscode(response);

        if(statusCode== 200) {
            dbUtils.DB_InsertStep("Verify POST Call","Call should be successful", "POST Call is successful", RESULT.PASS);
        }
        else {
            dbUtils.DB_InsertStep("Verify POST Call","Call should be successful", "POST Call returns " + statusCode, RESULT.FAIL);
        }
    }
}
