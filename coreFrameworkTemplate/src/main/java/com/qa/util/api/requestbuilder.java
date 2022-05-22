package com.qa.util.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.File;
import java.util.Map;

public class requestbuilder {

    public static Response send_request(String method, String request_url, Map headers, String payloadPath) {

        Response res = null;
        File payload = null;

        if (!payloadPath.isEmpty()) {
            payload = new File(payloadPath);
        }
        else {
            payload = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\qa\\services\\payloads\\"
                    + payloadPath);
        }

        System.out.println("-------------------------------------------------");
        if(method.toLowerCase() != "post") {
            res = RestAssured.given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .headers(headers)
                    .when()
                    .request(method, request_url);
        }
        else {
            res = RestAssured.given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .headers(headers)
                    .body(payloadPath)
                    .when()
                    .request(method, request_url);
        }
        System.out.println("-------------------------------------------------");
        System.out.println("");
        System.out.println("");
        log_response(res);
        return res;
    }

    private static void log_response(Response response) {
        System.out.println("-------------------------------------------------");
        response.then().log().all();
        System.out.println("-------------------------------------------------");
    }

    public static int get_response_statuscode(Response res) {
        return res.getStatusCode();
    }
}
