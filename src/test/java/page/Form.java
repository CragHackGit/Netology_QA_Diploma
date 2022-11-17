package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static page.MainPage.errorNotification;
import static page.MainPage.successNotification;

public abstract class Form {

    public SelenideElement cardNumberField = $x("//*[@id=\"root\"]/div/form/fieldset/div[1]/span/span/span[2]/input");
    public SelenideElement monthField = $x("//*[@id=\"root\"]/div/form/fieldset/div[2]/span/span[1]/span/span/span[2]/input");
    public SelenideElement yearField = $x("//*[@id=\"root\"]/div/form/fieldset/div[2]/span/span[2]/span/span/span[2]/input");
    public SelenideElement holderField = $x("//*[@id=\"root\"]/div/form/fieldset/div[3]/span/span[1]/span/span/span[2]/input");
    public SelenideElement cvvField = $x("//*[@id=\"root\"]/div/form/fieldset/div[3]/span/span[2]/span/span/span[2]/input");
    public SelenideElement continueButton = $x("//*[@id=\"root\"]/div/form/fieldset/div[4]/button/span/span");

    public void checkSuccessNotificationPresent() {
        successNotification.should(Condition.visible, Duration.ofSeconds(15));
        successNotification.$x("./div[@class='notification__content']").should(Condition.text("Операция одобрена Банком."));
    }

    public void checkSuccessNotificationAbsent() {
        successNotification.shouldNot(Condition.visible);
    }

    public void checkErrorNotificationPresent() {
        errorNotification.should(Condition.visible, Duration.ofSeconds(15));
        errorNotification.$x("./div[@class='notification__content']")
                .should(Condition.text("Ошибка! Банк отказал в проведении операции."));
    }

    public void checkErrorNotificationAbsent() {
        errorNotification.shouldNot(Condition.visible);
    }

    public void invalidDateError(String field) {
        $x("//span[text()='" + field + "']/..//span[@class='input__sub']")
                .shouldBe(visible, text("Неверно указан срок действия карты"));
    }

    public void expiredDateError(String field) {
        $x("//span[text()='" + field + "']/..//span[@class='input__sub']")
                .shouldBe(visible, text("Истёк срок действия карты"));
    }
}