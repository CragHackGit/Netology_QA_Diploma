package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.codeborne.selenide.Selenide.$x;

@Data
@NoArgsConstructor
public class MainPage {

    public static SelenideElement errorNotification = $x("//div[contains(@class, 'notification_status_error')]");
    public static SelenideElement successNotification = $x("//div[contains(@class, 'notification_status_ok')]");
    static SelenideElement payButton = $x("//div/button[1]");
    static SelenideElement creditButton = $x("//div/button[2]");
    static SelenideElement formHead = $x("//*[@id=\"root\"]/div/h3");
    static SelenideElement form = $x("//*[@id=\"root\"]/div/form");


    public PayPage getPayPage() {
        payButton.click();
        formHead.should(Condition.visible, Condition.text("Оплата по карте"));
        form.shouldBe(Condition.visible);
        successNotification.should(Condition.hidden);
        errorNotification.should(Condition.hidden);
        return new PayPage();
    }

    public CreditPage getCreditPage() {
        creditButton.click();
        formHead.should(Condition.visible, Condition.text("Кредит по данным карты"));
        form.shouldBe(Condition.visible);
        successNotification.should(Condition.hidden);
        errorNotification.should(Condition.hidden);
        return new CreditPage();
    }
}