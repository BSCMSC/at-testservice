package apitests.steps;

import apitests.enums.Users;
import apitests.helper.GsonHelper;
import apitests.helper.XmlParser;
import apitests.models.GetCustomerById.CustomerByIdResp;
import apitests.models.GetCustomerById.ReturnDTO;
import apitests.models.GetEmptyPhone.EmptyPhoneResp;
import apitests.models.PostCustomer.CustomerResp;
import apitests.models.PostFindByPhoneNumber.FindByPhoneNumberResp;
import apitests.models.PostLogin.LoginResp;
import apitests.service.CustomerApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.SneakyThrows;

import java.util.Map;

public class ScenariosSteps {

    private final CustomerApi customerApi = new CustomerApi();

    private Long numberPhone;

    @Step("Получение токена - {0}")
    public String getToken(String login, String password) {
        LoginResp actual = customerApi.postLogin(login, password, 200, LoginResp.class);
        return actual.getToken();
    }

    public String getToken(Users users) {
        return getToken(users.getLogin(),users.getPassword());
    }

    @Step("Получение свободного номера телефона")
    public Long getEmptyPhone(String authToken) {
        int flag = 0;
        while (flag < 10) {
            Response actualEmptyPhoneResp = customerApi.getEmptyPhone(authToken);
            if (actualEmptyPhoneResp.getStatusCode() == 200) {
                EmptyPhoneResp emptyPhoneResp = GsonHelper.jsonParse(actualEmptyPhoneResp, EmptyPhoneResp.class);
                if (emptyPhoneResp.getPhones().size() > 0) {
                    numberPhone = emptyPhoneResp.getPhones().get(0).getPhone();
                    flag = 10;
                }
            }
            flag++;
        }
        return numberPhone;
    }

    @Step("Создание кастомера - {name}")
    public String getCustomerId(String name, Long phone, Map<String, String> additionalParameters, String authToken) {
        CustomerResp actual = customerApi.postCustomer(name, phone, additionalParameters, authToken, 200,
                CustomerResp.class);
        return actual.getId();
    }

    @Step("Получение данных кастомера (статус, паспортные данные)")
    public ReturnDTO getCustomerStatusPd(String idCustomer, String authToken) {
        CustomerByIdResp actual = customerApi.getCustomerById(idCustomer, authToken, 200, CustomerByIdResp.class);
        return actual.getReturnCustomerId();
    }

    @SneakyThrows
    @Step("Получение id кастомера из старой системы")
    public String getCustomerIdOldSystem(Long numberPhone, String authToken) {
        Response actualXml = customerApi.postFindByPhoneNumber(authToken, numberPhone, 200);
        FindByPhoneNumberResp actualXmlResponse = XmlParser.getXml(actualXml.getBody().asString(), FindByPhoneNumberResp.class);
        return actualXmlResponse.getBody().getCustomerId();
    }

    @Step("Смена статуса кастомера вручную")
    public void changeCustomerStatus(String idCustomer, String status, String authToken) {
        customerApi.postChangeCustomerStatus(idCustomer, status, authToken, 200, Response.class);
    }
}
