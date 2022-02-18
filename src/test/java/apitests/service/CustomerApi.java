package apitests.service;

import apitests.api.MethodsAPI;
import apitests.api.Param;
import apitests.config.ConfigVars;
import apitests.helper.FileHelper;
import apitests.helper.GsonHelper;
import apitests.models.PostChangeCustomerStatus.ChangeCustomerStatusReq;
import apitests.models.PostCustomer.CustomerReq;
import apitests.models.PostLogin.LoginReq;
import io.qameta.allure.Step;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.aeonbits.owner.ConfigFactory;

import java.util.HashMap;
import java.util.Map;

public class CustomerApi {
    private static final ConfigVars CONFIG = ConfigFactory.create(ConfigVars.class);
    private final MethodsAPI methodsAPI = new MethodsAPI();

    @Step("Вызов метода POST /customer/{customerId}/changeCustomerStatus")
    public <T> T postChangeCustomerStatus(String customerId, String status, String authToken, Integer statusCode, Class<T> respClass) {
        String path = "/customer/{customerId}/changeCustomerStatus";
        String url = CONFIG.host() + path;
        ChangeCustomerStatusReq changeCustomerStatusReq = ChangeCustomerStatusReq.builder()
                .status(status)
                .build();
        String req = GsonHelper.toJsonString(changeCustomerStatusReq);
        HashMap<String, Object> pathParams = methodsAPI.createPathParams(new Param("customerId", customerId));
        Headers headers = methodsAPI.createHeaders(new Header("authToken", authToken));
        return methodsAPI.post(req, pathParams, headers, url, statusCode, respClass);
    }

    @Step("Вызов метода GET /simcards/getEmptyPhone")
    public Response getEmptyPhone(String authToken) {
        String path = "/simcards/getEmptyPhone";
        String url = CONFIG.host() + path;
        Headers headers = methodsAPI.createHeaders(new Header("authToken", authToken));
        return methodsAPI.get(headers, url);
    }

    @Step("Вызов метода POST /customer/findByPhoneNumber")
    public Response postFindByPhoneNumber(String authToken, Long numberPhone, Integer statusCode) {
        String path = "/customer/findByPhoneNumber";
        String requestXmlFile = "requests/request.xml";
        String url = CONFIG.host() + path;
        String requestXml = FileHelper.getContent(requestXmlFile);
        String req = requestXml.replace("String", authToken).replace("Long", String.valueOf(numberPhone));
        return methodsAPI.post(req, url, "application/xml", statusCode);
    }

    @Step("Вызов метода POST /customer/findByPhoneNumber")
    public Response postFindByPhoneNumber(String requestXml, Integer statusCode) {
        String path = "/customer/findByPhoneNumber";
        String url = CONFIG.host() + path;
        return methodsAPI.post(requestXml, url, "application/xml", statusCode);
    }

    @Step("Вызов метода GET /customer/getCustomerById")
    public <T> T getCustomerById(String customerId, String authToken, Integer statusCode, Class<T> respClass) {
        String path = "/customer/getCustomerById";
        String url = CONFIG.host() + path;
        HashMap<String, Object> queryParams = methodsAPI.createQueryParams(new Param("customerId", customerId));
        Headers headers = methodsAPI.createHeaders(new Header("authToken", authToken));
        return methodsAPI.get(queryParams, headers, url, statusCode, respClass);
    }

    @Step("Вызов метода POST /login")
    public <T> T postLogin(String login, String password, Integer statusCode, Class<T> respClass) {
        String path = "/login";
        String url = CONFIG.host() + path;
        LoginReq loginReq = LoginReq.builder()
                .login(login)
                .password(password)
                .build();
        String req = GsonHelper.toJsonString(loginReq);
        return methodsAPI.post(req, url, statusCode, respClass);
    }

    @Step("Вызов метода POST /customer/postCustomer")
    public <T> T postCustomer(String name, Long phone, Map<String, String> additionalParameters, String authToken,
                                          Integer statusCode, Class<T> respClass) {
        String path = "/customer/postCustomer";
        String url = CONFIG.host() + path;
        CustomerReq customerReq = CustomerReq.builder()
                .name(name)
                .phone(phone)
                .additionalParameters(additionalParameters)
                .build();
        String req = GsonHelper.toJsonString(customerReq);
        Headers headers = methodsAPI.createHeaders(new Header("authToken", authToken));
        return methodsAPI.post(req, headers, url, statusCode, respClass);
    }

}
