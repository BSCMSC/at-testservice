package apitests.tests;

import apitests.api.BaseTests;
import apitests.enums.Users;
import apitests.helper.GsonHelper;
import apitests.models.Error.ErrResp;
import apitests.models.GetEmptyPhone.EmptyPhoneResp;
import apitests.service.CustomerApi;
import apitests.steps.ScenariosSteps;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static apitests.helper.AssertionsHelper.*;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.matchesPattern;

@Story("Проверка получения номеров телефонов")
public class EmptyPhoneTests extends BaseTests {

    private static final String locale = "RU_RU";
    private final CustomerApi customerApi = new CustomerApi();
    private final ScenariosSteps scenariosSteps = new ScenariosSteps();
    private Response actualEmptyPhoneResp;
    private EmptyPhoneResp emptyPhoneResp;

    @Test(description = "1. Успешный ответ. Успешное получение номеров телефонов", groups = {"admin", "users"})
    @Parameters("userName")
    public void test1GetEmptyPhone(@Optional("ADMIN") String userName) {
        int flag = 0;
        while (flag < 10) {
            String authToken = scenariosSteps.getToken(Users.valueOf(userName));
            actualEmptyPhoneResp = customerApi.getEmptyPhone(authToken);
            if (actualEmptyPhoneResp.getStatusCode() == 200) {
                emptyPhoneResp = GsonHelper.jsonParse(actualEmptyPhoneResp, EmptyPhoneResp.class);
                if (emptyPhoneResp.getPhones().size() > 0) {
                    step("Проверяем значения параметров в ответе на запрос GET", () -> {
                                for (int i = 0; i < emptyPhoneResp.getPhones().size(); i++) {
                                    int finalI = i;
                                    assertAll(
                                            () -> equalsParametersMatcherPattern("Phone " + finalI, emptyPhoneResp.getPhones().get(finalI).getPhone().toString(), matchesPattern("^(7928)+\\d{7}$")),
                                            () -> equalsParameters(locale, emptyPhoneResp.getPhones().get(finalI).getLocale(), "Locale " + finalI)
                                    );
                                }
                            }
                    );
                    flag = 10;
                }
            }
            flag++;
        }
        step("Проверяем status code и кол-во номеров телефонов на запрос GET", () -> assertAll(
                () -> equalsParameters(200, actualEmptyPhoneResp.getStatusCode(), "Status code"),
                () -> notNullOrEmptyParameter(emptyPhoneResp.getPhones().size(), "Кол-во номеров телефонов")
        ));
    }

    @Test(description = "2. Ответ с ошибкой. Некорректный токен", groups = {"other"})
    public void test2GetEmptyPhone() {
        int flag = 0;
        while (flag < 10) {
            actualEmptyPhoneResp = customerApi.getEmptyPhone("Unsupported Value");
            if (actualEmptyPhoneResp.getStatusCode() == 401) {
                ErrResp actual = GsonHelper.jsonParse(actualEmptyPhoneResp, ErrResp.class);
                ErrResp expected = new ErrResp("Не корректный токен пользователя");
                step("Проверяем значение параметра в ответе на запрос GET", () ->
                        equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage")
                );
                flag = 10;
            }
            flag++;
        }
        step("Проверяем status code на запрос GET", () ->
                equalsParameters(401, actualEmptyPhoneResp.getStatusCode(), "Status code"));
    }

    @Issue("bug")
    @Test(description = "3. Ответ с ошибкой. Отсутствует токен", groups = {"other"})
    public void test3GetEmptyPhone() {
        int flag = 0;
        while (flag < 10) {
            actualEmptyPhoneResp = customerApi.getEmptyPhone(null);
            if (actualEmptyPhoneResp.getStatusCode() == 401) {
                ErrResp actual = GsonHelper.jsonParse(actualEmptyPhoneResp, ErrResp.class);
                ErrResp expected = new ErrResp("У пользователя не хватает прав на выполнение команды");
                step("Проверяем значение параметра в ответе на запрос GET", () ->
                        equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage")
                );
                flag = 10;
            }
            flag++;
        }
        step("Проверяем status code на запрос GET", () ->
                equalsParameters(401, actualEmptyPhoneResp.getStatusCode(), "Status code"));
    }

}
