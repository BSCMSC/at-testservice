package apitests.api;

import io.qameta.allure.Story;
import org.testng.ITest;

public abstract class BaseTests implements ITest {

    @Override
    public String getTestName() {
        Story annotation = this.getClass().getAnnotation(Story.class);
        return annotation == null ? this.getClass().getName() : annotation.value();
    }

}
