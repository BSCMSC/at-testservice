package apitests.api;

import apitests.helper.GsonHelper;
import io.qameta.allure.Attachment;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;

public class MethodsAPI {
    private Response resp;
    private ByteArrayOutputStream reqVar;
    private ByteArrayOutputStream resVar;

    @Attachment(value = "{name}", type = "text/plain", fileExtension = "txt")
    private static byte[] attachLog(String name, ByteArrayOutputStream out) {
        return out.toByteArray();
    }

    private RequestSpecification req(String contentType) {
        reqVar = new ByteArrayOutputStream();
        resVar = new ByteArrayOutputStream();
        PrintStream requestOut = new PrintStream(reqVar, true);
        PrintStream responseOut = new PrintStream(resVar, true);
        return given()
                .filters(new RequestLoggingFilter(requestOut), new ResponseLoggingFilter(responseOut))
                .log().all().contentType(contentType);
    }

    public <T> T post(String reqBody, String url, Integer statusCode, Class<T> respClass) {
        resp = req("application/json").body(reqBody).post(url);
        statusCodeValidation(statusCode);
        return GsonHelper.jsonParse(resp, respClass);
    }

    public Response post(String reqBody, String url, String contentType, Integer statusCode) {
        resp = req(contentType).body(reqBody).post(url);
        statusCodeValidation(statusCode);
        return resp;
    }

    public <T> T post(String reqBody, HashMap<String, Object> pathParams, Headers headers, String url, Integer statusCode, Class<T> respClass) {
        resp = req("application/json").pathParams(pathParams).headers(headers).body(reqBody).post(url);
        statusCodeValidation(statusCode);
        return GsonHelper.jsonParse(resp, respClass);
    }

    public <T> T post(String reqBody, Headers headers, String url, Integer statusCode, Class<T> respClass) {
        resp = req("application/json").headers(headers).body(reqBody).post(url);
        statusCodeValidation(statusCode);
        return GsonHelper.jsonParse(resp, respClass);
    }

    public <T> T get(HashMap<String, Object> queryParams, Headers headers, String url, Integer statusCode, Class<T> respClass) {
        resp = req("application/json").queryParams(queryParams).headers(headers).get(url);
        statusCodeValidation(statusCode);
        return GsonHelper.jsonParse(resp, respClass);
    }

    public Response get(Headers headers, String url) {
        resp = req("application/json").headers(headers).get(url);
        statusCodeValidation();
        return resp;
    }

    public Headers createHeaders(Header... headers) {
        List<Header> list = new ArrayList<>();
        for (Header header : headers) {
            if (header.getValue() != null) {
                list.add(header);
            }
        }
        return new Headers(list);
    }

    public HashMap<String, Object> createPathParams(Param... pathParams) {
        HashMap<String, Object> map = new HashMap<>();
        for (Param pathParam : pathParams) {
            if (pathParam.value() != null) {
                map.put(pathParam.key(), pathParam.value());
            } else {
                map.put(pathParam.key(), "");
            }
        }
        return map;
    }

    public HashMap<String, Object> createQueryParams(Param... queryParams) {
        HashMap<String, Object> map = new HashMap<>();
        for (Param queryParam : queryParams) {
            if (queryParam.value() != null) {
                map.put(queryParam.key(), queryParam.value());
            }
        }
        return map;
    }

    private void statusCodeValidation(Integer statusCode) {
        attachLogHelper();
        resp.then().log().all().statusCode(statusCode);
    }

    private void statusCodeValidation() {
        attachLogHelper();
        resp.then().log().all();
    }

    private void attachLogHelper() {
        attachLog("request", reqVar);
        attachLog("response", resVar);
        resVar.reset();
        reqVar.reset();
    }
}
