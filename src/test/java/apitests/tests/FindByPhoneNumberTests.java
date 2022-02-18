package apitests.tests;

import apitests.api.BaseTests;
import apitests.enums.Users;
import apitests.helper.*;
import apitests.models.Error.ErrResp;
import apitests.models.PostFindByPhoneNumber.FindByPhoneNumberResp;
import apitests.service.CustomerApi;
import apitests.steps.ScenariosSteps;
import io.qameta.allure.Allure;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static apitests.helper.AssertionsHelper.equalsParameters;
import static apitests.helper.AssertionsHelper.notNullOrEmptyParameter;

@Story("Проверка получения ID кастомера из старой системы")
public class FindByPhoneNumberTests extends BaseTests {

    private final ScenariosSteps scenariosSteps = new ScenariosSteps();
    private final CustomerApi customerApi = new CustomerApi();

    private final Map<String, String> additionalParameters = new HashMap<>();
    private String name;

    @BeforeMethod(groups = {"admin", "other", "user"})
    public void beforeTest() {
        name = Randomize.randomStringEng(10);
        String addressName = Randomize.randomStringEng(10);
        additionalParameters.put("Address", addressName);
    }

    @SneakyThrows
    @Test(description = "1. Успешный ответ. Успешное получение ID кастомера из старой системы", groups = {"admin", "users"},
            retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test1GetCustomerByIdOldSystem(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);
        scenariosSteps.getCustomerId(name, numberPhone, additionalParameters, authToken);

        Response actual = customerApi.postFindByPhoneNumber(authToken, numberPhone, 200);
        FindByPhoneNumberResp actualResponse = XmlParser.getXml(actual.getBody().asString(), FindByPhoneNumberResp.class);
        Allure.step("Проверяем значение параметра в ответе на запрос POST", () ->
                notNullOrEmptyParameter(actualResponse.getBody().getCustomerId(), "CustomerId"));
    }

    @Issue("bug")
    @Test(description = "2. Успешный ответ. Несуществующий phoneNumber", groups = {"admin"})
    @Parameters("userName")
    public void test2GetCustomerByIdOldSystem(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));

        Response actual = customerApi.postFindByPhoneNumber(authToken, 99999999999L, 400);
        ErrResp actualErr = GsonHelper.jsonParse(actual, ErrResp.class);
        ErrResp expected = new ErrResp("Кастомер с данным phoneNumber не найден");
        Allure.step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actualErr.getErrorMessage(), "ErrorMessage"));
    }

    @Issue("bug")
    @Test(description = "3. Успешный ответ. Отсутствует параметр phoneNumber", groups = {"admin"})
    @Parameters("userName")
    public void test3GetCustomerByIdOldSystem(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));

        String requestXml = FileHelper.getContent("requests/requestNotFoundNumberPhone.xml").replaceAll("String", authToken);
        Response actual = customerApi.postFindByPhoneNumber(requestXml, 400);
        ErrResp actualErr = GsonHelper.jsonParse(actual, ErrResp.class);
        ErrResp expected = new ErrResp("Отсутствует обязательный параметр phoneNumber");
        Allure.step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actualErr.getErrorMessage(), "ErrorMessage"));
    }

    @Test(description = "4. Ответ с ошибкой. Некорректный параметр authToken", groups = {"other"})
    @Parameters("userName")
    public void test4GetCustomerByIdOldSystem(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);

        Response actual = customerApi.postFindByPhoneNumber("Unsupported Value", numberPhone, 401);
        ErrResp actualErr = GsonHelper.jsonParse(actual, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный токен пользователя");
        Allure.step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actualErr.getErrorMessage(), "errorMessage"));
    }

    @Test(description = "5. Ответ с ошибкой. Отсутствует токен", groups = {"other"})
    @Parameters("userName")
    public void test5GetCustomerByIdOldSystem(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);

        String requestXml = FileHelper.getContent("requests/requestNotFoundAuthToken.xml").replaceAll("Long", String.valueOf(numberPhone));
        Response actual = customerApi.postFindByPhoneNumber(requestXml, 401);
        ErrResp actualErr = GsonHelper.jsonParse(actual, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный токен пользователя");
        Allure.step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actualErr.getErrorMessage(), "errorMessage"));
    }

    @Test(description = "6. Ответ с ошибкой. Некорректная структура запроса", groups = {"other"})
    public void test6GetCustomerByIdOldSystem() {
        String requestXml = FileHelper.getContent("requests/requestNoCorrect.xml");
        Response actual = customerApi.postFindByPhoneNumber(requestXml, 500);
        ErrResp actualErr = GsonHelper.jsonParse(actual, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный формат тела запроса");
        Allure.step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actualErr.getErrorMessage(), "errorMessage"));
    }

    @Issue("bug")
    @Test(description = "7. Ответ с ошибкой. Пустое тело запроса", groups = {"other"})
    public void test7GetCustomerByIdOldSystem() {
        String requestXml = FileHelper.getContent("requests/requestNotAuthTokenAndNumberPhone.xml");
        Response actual = customerApi.postFindByPhoneNumber(requestXml, 400);
        ErrResp actualErr = GsonHelper.jsonParse(actual, ErrResp.class);
        ErrResp expected = new ErrResp("Отсутствуют обязательные параметры phoneNumber и authToken");
        Allure.step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actualErr.getErrorMessage(), "errorMessage"));
    }

    @Issue("bug")
    @Test(description = "8. Ответ с ошибкой. Некорректный параметр phoneNumber", groups = {"admin"})
    @Parameters("userName")
    public void test8GetCustomerByIdOldSystem(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));

        Response actual = customerApi.postFindByPhoneNumber(authToken, -1L, 400);
        ErrResp actualErr = GsonHelper.jsonParse(actual, ErrResp.class);
        ErrResp expected = new ErrResp("Некорректный параметр phoneNumber");
        Allure.step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actualErr.getErrorMessage(), "ErrorMessage"));
    }
}
