package apitests.helper;

import io.qameta.allure.Step;
import org.hamcrest.Matcher;
import org.opentest4j.MultipleFailuresError;
import org.testng.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;

public class AssertionsHelper {


    @Step("Проверяем равенство параметра {paramName} значению {expected}")
    public static void equalsParameters(Object expected, Object actual, String paramName) {
        Assert.assertEquals(actual, expected, "Значение параметра " + paramName + " не соответствует ожидаемому");
    }

    @Step("Проверяем, что значение параметра {paramName} не равно null, 0 и не пустое")
    public static void notNullOrEmptyParameter(Object value, String paramName) {
        Assert.assertNotEquals("", value, "Значение параметра " + paramName + " пустое");
        Assert.assertNotEquals(null, value, "Значение параметра " + paramName + " равно null");
        Assert.assertNotEquals("0", value.toString(), "Значение параметра " + paramName + " равно 0");
    }

    @Step("Проверяем соответствие параметра {reason} регулярному выражению")
    public static <T> void equalsParametersMatcherPattern(String reason, T actual, Matcher<? super T> matcher) {
        assertThat(reason, actual, matcher);
    }

    @Step("Проверяем соответствие параметра {paramName} значению {expected}")
    public static <T extends Enum<T>> void equalsParameters(T expected, String actual, String paramName) {
        Assert.assertEquals(expected.toString(), actual,
                "Значение параметра " + paramName + " не соответствует ожидаемому");
    }

    //softAssert аналог junit
    public static void assertAll(Runnable... runnables) {
        assertAll(null, runnables);
    }

    public static void assertAll(String heading, Runnable... runnables) {
        if (runnables == null || runnables.length == 0) {
            throw new RuntimeException("executables array must not be null or empty");
        }
        Arrays.stream(runnables).forEach((runnable) -> {
            if (runnable == null)
                throw new RuntimeException("individual executables must not be null");
        });
        List<AssertionError> failures = Arrays.stream(runnables).map((runnable) -> {
            try {
                runnable.run();
                return null;
            } catch (AssertionError fail) {
                return fail;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (!failures.isEmpty()) {
            MultipleFailuresError multipleFailuresError = new MultipleFailuresError(heading, failures);
            Objects.requireNonNull(multipleFailuresError);
            failures.forEach(multipleFailuresError::addSuppressed);
            throw multipleFailuresError;
        }
    }
}
