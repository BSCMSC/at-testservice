package apitests.tests;

import apitests.api.BaseTests;
import apitests.enums.Users;
import apitests.helper.Randomize;
import apitests.helper.RunTestAgain;
import apitests.models.Error.ErrResp;
import apitests.models.PostCustomer.CustomerResp;
import apitests.service.CustomerApi;
import apitests.steps.ScenariosSteps;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static apitests.helper.AssertionsHelper.equalsParameters;
import static apitests.helper.AssertionsHelper.notNullOrEmptyParameter;
import static io.qameta.allure.Allure.step;

@Story("Проверка создания нового кастомера")
public class PostCustomerTests extends BaseTests {

    private final CustomerApi customerApi = new CustomerApi();
    private final ScenariosSteps scenariosSteps = new ScenariosSteps();

    private final Map<String, String> additionalParameters = new HashMap<>();
    private String name;

    @BeforeMethod(groups = {"admin", "user", "other"})
    public void beforeTest() {
        name = Randomize.randomStringEng(10);
        String addressName = Randomize.randomStringEng(10);
        additionalParameters.put("Address", addressName);
    }

    @Test(description = "1. Успешный ответ. Успешное создание нового кастомера с доп параметром", groups = {"admin", "users"},
            retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test1PostCustomer(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);

        CustomerResp actual = customerApi.postCustomer(name, numberPhone, additionalParameters, authToken,
                200, CustomerResp.class);
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                notNullOrEmptyParameter(actual.getId(), "Id"));
    }

    @Issue("bug")
    @Test(description = "2. Ответ с ошибкой. Повторное создание нового кастомера с доп параметром", groups = {"admin", "users"},
            retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test2PostCustomer(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);
        scenariosSteps.getCustomerId(name, numberPhone, additionalParameters, authToken);

        ErrResp actual = customerApi.postCustomer(name, numberPhone, additionalParameters, authToken, 400, ErrResp.class);
        ErrResp expected = new ErrResp("Данный кастомер существует");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "ErrorMessage"));
    }

    @Test(description = "3. Успешный ответ. Отсутствует параметр additionalParameters", groups = {"admin", "users"},
            retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test3PostCustomer(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);

        CustomerResp actual = customerApi.postCustomer(name, numberPhone, null, authToken,
                200, CustomerResp.class);
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                notNullOrEmptyParameter(actual.getId(), "Id"));
    }

    @Test(description = "4. Ответ с ошибкой. Отсутствует параметр name", groups = {"admin"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test4PostCustomer(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);

        ErrResp actual = customerApi.postCustomer(null, numberPhone, additionalParameters, authToken,
                400, ErrResp.class);
        ErrResp expected = new ErrResp("Указаны не корректные данные для пользователя");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "ErrorMessage"));
    }

    @Test(description = "5. Ответ с ошибкой. Отсутствует параметр phone", groups = {"user"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test5PostCustomer(@Optional("USER") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));

        ErrResp actual = customerApi.postCustomer(name, null, additionalParameters, authToken,
                400, ErrResp.class);
        ErrResp expected = new ErrResp("Указаны не корректные данные для пользователя");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "ErrorMessage"));
    }

    @Issue("bug")
    @Test(description = "8. Ответ с ошибкой. Некорректный параметр phone", groups = {"admin"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test9PostCustomer(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));

        ErrResp actual = customerApi.postCustomer(name, 1L, additionalParameters, authToken,
                400, ErrResp.class);
        ErrResp expected = new ErrResp("Указаны не корректные данные для пользователя");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "ErrorMessage"));
    }

    @Issue("bug")
    @Test(description = "9. Ответ с ошибкой. Отсутствует параметр authToken", groups = {"other"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test10PostCustomer(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);

        ErrResp actual = customerApi.postCustomer(name, numberPhone, additionalParameters, null, 400, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный токен пользователя");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "ErrorMessage"));
    }

    @Test(description = "10. Ответ с ошибкой. Некорректный параметр authToken", groups = {"other"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test11PostCustomer(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);

        ErrResp actual = customerApi.postCustomer(name, numberPhone, additionalParameters, "Unsupported Value",
                401, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный токен пользователя");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "ErrorMessage"));
    }

    @Test(description = "11. Ответ с ошибкой. Пустое тело запроса", groups = {"admin"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test12PostCustomer(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        ErrResp actual = customerApi.postCustomer(null, null, null, authToken,
                400, ErrResp.class);
        ErrResp expected = new ErrResp("Указаны не корректные данные для пользователя");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "ErrorMessage"));
    }

    @Issue("bug")
    @Test(description = "12. Ответ с ошибкой. Некорректный параметр name", groups = {"admin"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test13PostCustomer(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);

        ErrResp actual = customerApi.postCustomer("", numberPhone, additionalParameters, authToken,
                400, ErrResp.class);
        ErrResp expected = new ErrResp("Указаны не корректные данные для пользователя");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "ErrorMessage"));
    }

    @Test(description = "13. Успешный ответ. Пустое значение доп параметра", groups = {"admin"},
            retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test14PostCustomer(@Optional("ADMIN") String userName) {
        additionalParameters.put("FIO", "");
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);

        CustomerResp actual = customerApi.postCustomer(name, numberPhone, additionalParameters, authToken,
                200, CustomerResp.class);
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                notNullOrEmptyParameter(actual.getId(), "Id"));
    }

}
