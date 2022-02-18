package apitests.tests;

import apitests.api.BaseTests;
import apitests.enums.Users;
import apitests.helper.Randomize;
import apitests.helper.RunTestAgain;
import apitests.models.Error.ErrResp;
import apitests.models.GetCustomerById.CustomerByIdResp;
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

import static apitests.helper.AssertionsHelper.*;
import static io.qameta.allure.Allure.step;

@Story("Проверка получения кастомера по ID")
public class GetCustomerByIdTests extends BaseTests {

    private static final String STATUS = "NEW";
    private final ScenariosSteps scenariosSteps = new ScenariosSteps();
    private final CustomerApi customerApi = new CustomerApi();
    private final Map<String, String> additionalParameters = new HashMap<>();
    private String name;

    @BeforeMethod(groups = {"admin", "user", "other"})
    public void beforeTest() {
        name = Randomize.randomStringEng(10);
        String addressName = Randomize.randomStringEng(10);
        additionalParameters.put("Address", addressName);
    }

    @Test(description = "1. Успешный ответ. Успешное получение кастомера по ID", groups = {"admin", "users"},
            retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test1GetCustomerById(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);
        String idCustomer = scenariosSteps.getCustomerId(name, numberPhone, additionalParameters, authToken);

        CustomerByIdResp actual = customerApi.getCustomerById(idCustomer, authToken, 200, CustomerByIdResp.class);
        step("Проверяем значение параметров в ответе на запрос GET", () -> assertAll(
                () -> equalsParameters(idCustomer, actual.getReturnCustomerId().getCustomerId(), "CustomerId"),
                () -> equalsParameters(name, actual.getReturnCustomerId().getName(), "Name"),
                () -> equalsParameters(STATUS, actual.getReturnCustomerId().getStatus(), "Status"),
                () -> equalsParameters(numberPhone, actual.getReturnCustomerId().getPhone(), "Phone"),
                () -> equalsParameters(additionalParameters, actual.getReturnCustomerId().getAdditionalParameters(), "AdditionalParameters"),
                () -> notNullOrEmptyParameter(actual.getReturnCustomerId().getPd(), "Pd")
        ));
    }

    @Issue("bug")
    @Test(description = "2. Ответ с ошибкой. Некорректный параметр CustomerId", groups = {"admin"})
    @Parameters("userName")
    public void test2GetCustomerById(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));

        ErrResp actual = customerApi.getCustomerById("Unsupported Value", authToken, 400, ErrResp.class);
        ErrResp expected = new ErrResp("Некорректный идентификатор пользователя");
        step("Проверяем значение параметра в ответе на запрос GET", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "ErrorMessage"));
    }

    @Issue("bug")
    @Test(description = "3. Ответ с ошибкой. Отсутствует параметр CustomerId", groups = {"admin"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test3GetCustomerById(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));

        ErrResp actual = customerApi.getCustomerById(null, authToken, 400, ErrResp.class);
        ErrResp expected = new ErrResp("Отсутствует обязательный параметр CustomerId");
        step("Проверяем значение параметра в ответе на запрос GET", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "ErrorMessage"));
    }

    @Test(description = "4. Ответ с ошибкой. Некорректный токен", groups = {"other"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test4GetCustomerById(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);
        String idCustomer = scenariosSteps.getCustomerId(name, numberPhone, additionalParameters, authToken);

        ErrResp actual = customerApi.getCustomerById(idCustomer, "Unsupported Value", 401, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный токен пользователя");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "ErrorMessage"));
    }

    @Issue("bug")
    @Test(description = "5. Ответ с ошибкой. Отсутствует токен", groups = {"other"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test5GetCustomerById(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);
        String idCustomer = scenariosSteps.getCustomerId(name, numberPhone, additionalParameters, authToken);

        ErrResp actual = customerApi.getCustomerById(idCustomer, null, 401, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный токен пользователя");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "ErrorMessage"));
    }
}
