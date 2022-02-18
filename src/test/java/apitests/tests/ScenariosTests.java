package apitests.tests;

import apitests.api.BaseTests;
import apitests.enums.Users;
import apitests.helper.GsonHelper;
import apitests.helper.Randomize;
import apitests.models.GetCustomerById.ReturnDTO;
import apitests.models.PassportDTO;
import apitests.steps.ScenariosSteps;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.opentest4j.AssertionFailedError;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import static apitests.helper.AssertionsHelper.*;
import static io.qameta.allure.Allure.step;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.matchesPattern;

@Story("Бизнес-сценарий «Активация абонента»")
public class ScenariosTests extends BaseTests {

    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String STATUS_TEST = "STATUS-TEST";
    private final ScenariosSteps scenariosSteps = new ScenariosSteps();
    private final Map<String, String> additionalParameters = new HashMap<>();
    private String name;

    @BeforeMethod(groups = {"admin", "user"})
    public void beforeTest() {
        name = Randomize.randomStringEng(10);
        String fio = Randomize.randomStringEng(10);
        String addressName = Randomize.randomStringEng(10);
        additionalParameters.put("Address", addressName);
        additionalParameters.put("fio", fio);
    }

    @Test(description = "1. Успешный ответ. Активация абонента прошла успешно", groups = {"admin", "users"})
    @Parameters("userName")
    public void scenariosTest1(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);
        String idCustomer = scenariosSteps.getCustomerId(name, numberPhone, additionalParameters, authToken);
        ReturnDTO actualStatusPd = getStatusCustomer(idCustomer, authToken);
        String status = actualStatusPd.getStatus();
        String passport = actualStatusPd.getPd();
        PassportDTO actualPd = GsonHelper.jsonParse(passport, PassportDTO.class);
        step("Проверяем смену статуса на Active и корректность паспортных данных", () -> assertAll(
                () -> equalsParameters(STATUS_ACTIVE, status, "Status"),
                () -> equalsParametersMatcherPattern("PassportNumber", actualPd.getPassportNumber(), matchesPattern("\\d{6}")),
                () -> equalsParametersMatcherPattern("PassportSeries", actualPd.getPassportSeries(), matchesPattern("\\d{4}")),
                () -> equalsParameters(additionalParameters, actualStatusPd.getAdditionalParameters(), "AdditionalParameters")
        ));

        String idCustomerOldSystem = scenariosSteps.getCustomerIdOldSystem(numberPhone, authToken);
        step("Проверяем, что кастомер сохранился в старой системе", () ->
                notNullOrEmptyParameter(idCustomerOldSystem, "Id кастомера"));
    }

    @Test(description = "2. Успешный ответ. Успешная смена статуса кастомера вручную", groups = {"admin"})
    @Parameters("userName")
    public void scenariosTest2(@Optional("ADMIN") String userName) {
        String authToken = scenariosSteps.getToken(Users.valueOf(userName));
        Long numberPhone = scenariosSteps.getEmptyPhone(authToken);
        String idCustomer = scenariosSteps.getCustomerId(name, numberPhone, additionalParameters, authToken);

        ReturnDTO actualStatusPd = getStatusCustomer(idCustomer, authToken);
        String status = actualStatusPd.getStatus();
        String passport = actualStatusPd.getPd();
        PassportDTO actualPd = GsonHelper.jsonParse(passport, PassportDTO.class);
        step("Проверяем смену статуса на Active и корректность паспортных данных", () -> assertAll(
                () -> equalsParameters(STATUS_ACTIVE, status, "Status"),
                () -> equalsParametersMatcherPattern("PassportNumber", actualPd.getPassportNumber(), matchesPattern("\\d{6}")),
                () -> equalsParametersMatcherPattern("PassportSeries", actualPd.getPassportSeries(), matchesPattern("\\d{4}")),
                () -> equalsParameters(additionalParameters, actualStatusPd.getAdditionalParameters(), "AdditionalParameters")
        ));

        String idCustomerOldSystem = scenariosSteps.getCustomerIdOldSystem(numberPhone, authToken);
        step("Проверяем, что кастомер сохранился в старой системе", () ->
                notNullOrEmptyParameter(idCustomerOldSystem, "Id кастомера"));

        scenariosSteps.changeCustomerStatus(idCustomer, STATUS_TEST, authToken);
        ReturnDTO actualStatus = scenariosSteps.getCustomerStatusPd(idCustomer, authToken);
        step("Проверяем смену статуса на " + STATUS_TEST, () ->
                equalsParameters(STATUS_TEST, actualStatus.getStatus(), "Status"));
    }

    @Step("Получение данных кастомера")
    private ReturnDTO getStatusCustomer(String idCustomer, String authToken) {
        Queue<ReturnDTO> queue = new LinkedList<>();
        try {
            await("Ожидаем, пока сменится статус на Active")
                    .atMost(2, TimeUnit.MINUTES)
                    .pollInterval(10, TimeUnit.SECONDS)
                    .until(() -> {
                        ReturnDTO actualStatusPd = scenariosSteps.getCustomerStatusPd(idCustomer, authToken);
                        String status = actualStatusPd.getStatus();
                        queue.add(actualStatusPd);
                        return status.equals(STATUS_ACTIVE);
                    });
        } catch (Exception ex) {
            throw new AssertionFailedError("Статус не изменился на Active");
        }
        return queue.element();
    }

}
