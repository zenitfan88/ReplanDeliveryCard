package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("span[data-test-id='city'] input").val(validUser.getCity());
        $x("//*[@data-test-id='date']//input[@class='input__control']")
                .sendKeys(Keys.CONTROL + "A", Keys.BACK_SPACE);
        $x("//*[@data-test-id='date']//input[@class='input__control']").val(firstMeetingDate);
        $x("//*[@data-test-id='name']//input[@class='input__control']").val(validUser.getName());
        $x("//input[@name='phone']").val(validUser.getPhone());
        $x("//label[@data-test-id='agreement']").click();
        $x("//*[text()='Запланировать']").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на  " + firstMeetingDate), Duration.ofSeconds(15))
                .shouldBe(visible);

        $x("//*[contains(@class, 'notification__closer')]").click();
        $x("//*[@data-test-id='date']//input[@class='input__control']")
                .sendKeys(Keys.CONTROL + "A", Keys.BACK_SPACE);
        $x("//*[@data-test-id='date']//input[@class='input__control']").val(secondMeetingDate);
        $x("//*[text()='Запланировать']").click();
        $x("//*[text()='Перепланировать']").click();
        $x("//div[@data-test-id='success-notification']").should(visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на  " + firstMeetingDate), Duration.ofSeconds(15))
                .shouldBe(visible);
    }
}

