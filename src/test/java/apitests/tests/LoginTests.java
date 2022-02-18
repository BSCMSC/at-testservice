package apitests.tests;

import apitests.api.BaseTests;
import apitests.models.Error.ErrResp;
import apitests.models.PostLogin.LoginResp;
import apitests.service.CustomerApi;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static apitests.helper.AssertionsHelper.equalsParameters;
import static apitests.helper.AssertionsHelper.notNullOrEmptyParameter;
import static io.qameta.allure.Allure.step;

@Story("Проверка получения токена")
public class LoginTests extends BaseTests {

    private final CustomerApi customerApi = new CustomerApi();

    @Test(description = "1. Успешный ответ. Получение токена - ADMIN", groups = {"admin"})
    public void test1PostLogin() {
        LoginResp actual = customerApi.postLogin("admin", "password", 200, LoginResp.class);
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                notNullOrEmptyParameter(actual.getToken(), "token"));
    }

    @Test(description = "2. Успешный ответ. Получение токена - USER", groups = {"user"})
    public void test2PostLogin() {
        LoginResp actual = customerApi.postLogin("user", "password", 200, LoginResp.class);
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                notNullOrEmptyParameter(actual.getToken(), "token")
        );
    }

    @Test(description = "3. Ответ с ошибкой. Отсутствует параметр password", groups = {"user"})
    public void test3PostLogin() {
        ErrResp actual = customerApi.postLogin("user", null, 400, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный логин/пароль");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage"));
    }

    @Issue("bug")
    @Test(description = "4. Ответ с ошибкой. Отсутствует параметр login", groups = {"other"})
    public void test4PostLogin() {
        ErrResp actual = customerApi.postLogin(null, "password", 400, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный логин/пароль");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage")
        );
    }

    @Issue("bug")
    @Test(description = "5. Ответ с ошибкой. Некорректный параметр login (login != (admin or user))", groups = {"other"})
    public void test5PostLogin() {
        ErrResp actual = customerApi.postLogin("Unsupported Value", "password", 400, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный логин/пароль");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage")
        );
    }

    @Test(description = "6. Ответ с ошибкой. Некорректный параметр password (password != password)", groups = {"admin"})
    public void test6PostLogin() {
        ErrResp actual = customerApi.postLogin("admin", "Unsupported Value", 400, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный логин/пароль");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage")
        );
    }

    @Issue("bug")
    @Test(description = "7. Ответ с ошибкой. Пустой параметр login (login != (admin or user))", groups = {"other"})
    public void test7PostLogin() {
        ErrResp actual = customerApi.postLogin("", "password", 400, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный логин/пароль");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage")
        );
    }

    @Test(description = "8. Ответ с ошибкой. Пустой параметр password (password != password)", groups = {"admin"})
    public void test8PostLogin() {
        ErrResp actual = customerApi.postLogin("admin", "", 400, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный логин/пароль");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage")
        );
    }

    @Issue("bug")
    @Test(description = "9. Ответ с ошибкой. Пустое тело запроса", groups = {"other"})
    public void test9PostLogin() {
        ErrResp actual = customerApi.postLogin(null, null, 400, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный логин/пароль");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage")
        );
    }

    @Issue("bug")
    @Test(description = "10. Ответ с ошибкой. Пустые параметры login и password", groups = {"other"})
    public void test10PostLogin() {
        ErrResp actual = customerApi.postLogin("", "", 400, ErrResp.class);
        ErrResp expected = new ErrResp("Не корректный логин/пароль");
        step("Проверяем значение параметра в ответе на запрос POST", () ->
                equalsParameters(expected.getErrorMessage(), actual.getErrorMessage(), "errorMessage")
        );
    }

}
