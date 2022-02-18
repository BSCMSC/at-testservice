package apitests.tests;

import apitests.api.BaseTests;
import apitests.enums.Users;
import apitests.helper.Randomize;
import apitests.helper.RunTestAgain;
import apitests.models.Error.ErrResp;
import apitests.service.CustomerApi;
import apitests.steps.ScenariosSteps;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static apitests.helper.AssertionsHelper.equalsParameters;
import static io.qameta.allure.Allure.step;

@Story("Проверка смены статуса кастомера вручную")
public class ChangeCustomerStatusTests extends BaseTests {

    private static final String statusActive = "ACTIVE";
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

    @Test(description = "1. Успешный ответ. Успешная смена статуса кастомера", groups = {"admin"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test1PostChangeCustomerStatus(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);
        String idCustomer = scenariosSteps.getCustomerId(name, numberPhone, additionalParameters, authToken);
        customerApi.postChangeCustomerStatus(idCustomer, statusActive, authToken, 200, Response.class);
    }

    @Test(description = "2. Ответ с ошибкой. У пользователя не хватает прав", groups = {"user"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test2PostChangeCustomerStatus(@Optional("USER") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);
        String idCustomer = scenariosSteps.getCustomerId(name, numberPhone, additionalParameters, authToken);
        ErrResp actual = customerApi.postChangeCustomerStatus(idCustomer, statusActive, authToken, 401, ErrResp.class);
        ErrResp expected = new ErrResp("У пользователя не хватает прав на выполнение команды");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage"));
    }

    @Issue("bug")
    @Test(description = "3. Ответ с ошибкой. Отсутствует параметр status", groups = {"admin"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test3PostChangeCustomerStatus(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);
        String idCustomer = scenariosSteps.getCustomerId(name, numberPhone, additionalParameters, authToken);
        ErrResp actual = customerApi.postChangeCustomerStatus(idCustomer, null, authToken, 400, ErrResp.class);
        ErrResp expected = new ErrResp("Не указан статус");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage")
        );
    }

    @Issue("bug")
    @Test(description = "4. Ответ с ошибкой. Некорректный параметр status", groups = {"admin"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test4PostChangeCustomerStatus(@Optional("ADMIN") String userName) {
        String status = "";
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);
        String idCustomer = scenariosSteps.getCustomerId(name, numberPhone, additionalParameters, authToken);
        ErrResp actual = customerApi.postChangeCustomerStatus(idCustomer, status, authToken, 400, ErrResp.class);
        ErrResp expected = new ErrResp("Указан некорректный status");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage")
        );
    }

    @Issue("bug")
    @Test(description = "5. Ответ с ошибкой. Некорректный параметр customerId", groups = {"admin"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test5PostChangeCustomerStatus(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        ErrResp actual = customerApi.postChangeCustomerStatus("Unsupported Value", statusActive, authToken, 400, ErrResp.class);
        ErrResp expected = new ErrResp("Указан некорректный customerId");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage")
        );
    }

    @Issue("bug")
    @Test(description = "6. Ответ с ошибкой. Отсутствует параметр customerId", groups = {"admin"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test6PostChangeCustomerStatus(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        ErrResp actual = customerApi.postChangeCustomerStatus(null, statusActive, authToken, 405, ErrResp.class);
        ErrResp expected = new ErrResp("Некорректный url");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage"));
    }

    @Test(description = "7. Ответ с ошибкой. Некорректный токен", groups = {"other"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test7PostChangeCustomerStatus(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);
        String idCustomer = scenariosSteps.getCustomerId(name, numberPhone, additionalParameters, authToken);
        ErrResp actual = customerApi.postChangeCustomerStatus(idCustomer, statusActive, "Unsupported Value", 401, ErrResp.class);
        ErrResp expected = new ErrResp("У пользователя не хватает прав на выполнение команды");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage"));
    }

    @Issue("bug")
    @Test(description = "8. Ответ с ошибкой. Отсутствует токен", groups = {"other"}, retryAnalyzer = RunTestAgain.class)
    @Parameters("userName")
    public void test8PostChangeCustomerStatus(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);
        String idCustomer = scenariosSteps.getCustomerId(name, numberPhone, additionalParameters, authToken);
        ErrResp actual = customerApi.postChangeCustomerStatus(idCustomer, statusActive, null, 401, ErrResp.class);
        ErrResp expected = new ErrResp("У пользователя не хватает прав на выполнение команды");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage"));
    }


}
