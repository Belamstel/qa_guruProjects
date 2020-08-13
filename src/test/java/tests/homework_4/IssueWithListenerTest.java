package tests.homework_4;

import Api.ApiSteps;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static Api.NamedBy.css;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static helpers.LoadCredentials.getCredentialsFromJson;

@Feature("Работа с задачами")
public class IssueWithListenerTest {
    private static final String BASE_URL = "https://github.com/";
    private static final String REPOSITORY = "belamstel/qa_guruProjects";
    private static final String LOGIN = getCredentialsFromJson("ApiTests.secret", "login");
    private static final String PASSWORD = getCredentialsFromJson("ApiTests.secret", "pass");
    private static final String ISSUE_TITLE = "Homework 4";
    private static final String ISSUE_TEXT = "test";
    private final ApiSteps apiSteps = new ApiSteps();
    private String number = "";

    @BeforeEach
    public void initLogger() {
        SelenideLogger.addListener("allure", new AllureSelenide()
                .savePageSource(true)
                .screenshots(true));
    }

    @Test
    @DisplayName("Пользователь должен иметь возможность найти Issue по номеру")
    public void withNamedBy() {
        open(BASE_URL);
        $(byText("Sign in")).click();
        $(css("#login_field").as("Логин")).setValue(LOGIN);
        $(css("#password").as("Пароль")).setValue(PASSWORD);
        $(byName("commit")).click();
        open(BASE_URL + REPOSITORY);
        $x("//span[text()='Issues']").click();
        $x("//span[text()='New issue']").click();
        $(css("#issue_title").as("Заголовок")).click();
        $(css("#issue_title").as("Заголовок")).setValue(ISSUE_TITLE);
        $(css("#issue_body").as("Комментарий")).click();
        $(css("#issue_body").as("Комментарий")).setValue(ISSUE_TEXT);
        $(byText("Submit new issue")).click();
        number = $x("//span[contains(text(),'#')]").getText()
                .replace("#", "");
        apiSteps.shouldSeeIssueWithNumber(number, ISSUE_TITLE,ISSUE_TEXT);
    }
}

