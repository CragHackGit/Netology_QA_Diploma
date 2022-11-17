package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.MainPage;
import page.PayPage;

import static com.codeborne.selenide.Selenide.open;
import static data.DataBaseAssistant.*;
import static data.DataGenerator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UIPayPageTest {

    static PayPage page;
    static Card card;

    @BeforeAll
    public static void setUpClass() {
        SelenideLogger.addListener(
                "allure", new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true));
    }

    @BeforeEach
    public void openPageAndCleanDB() {
        open("http://localhost:8080/");
        page = new MainPage().getPayPage();
        cleanData();
    }

    @AfterAll
    public static void setDownClass() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("testDebitPayApprovedCard")
    public void testDebitPayApprovedCard() {
        card = getValidApprovedCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationPresent();

        OrderEntity order = getOrderEntity();
        PaymentEntity payment = getPaymentEntity();

        assertNotNull(order);
        assertNotNull(payment);

        assertEquals(order.getPayment_id(), payment.getTransaction_id());
        assertEquals("approved", payment.getStatus().toLowerCase());
        assertEquals(45_000, payment.getAmount());
    }

    @Test
    @DisplayName("testDebitPayDeclinedCard")
    public void testDebitPayDeclinedCard() {
        card = getValidDeclinedCard();
        page.pay(card);
        page.checkSuccessNotificationAbsent();
        page.checkErrorNotificationPresent();

        OrderEntity order = getOrderEntity();
        PaymentEntity payment = getPaymentEntity();

        assertNotNull(order);
        assertNotNull(payment);

        assertEquals(order.getPayment_id(), payment.getTransaction_id());
        assertEquals("declined", payment.getStatus().toLowerCase());
        assertEquals(45_000, payment.getAmount());
    }

    @Test
    @DisplayName("testDebitPayCardNotInDB")
    public void testDebitPayCardNotInDB() {
        card = getCardNotInDB();
        page.pay(card);
        page.checkErrorNotificationPresent();
        page.checkSuccessNotificationAbsent();

        OrderEntity order = getOrderEntity();
        PaymentEntity payment = getPaymentEntity();

        assertNotNull(order);
        assertNotNull(payment);

        assertEquals(order.getPayment_id(), payment.getTransaction_id());
        assertEquals("declined", payment.getStatus().toLowerCase());
        assertEquals(45_000, payment.getAmount());
    }

    @Test
    @DisplayName("testDebitPayPreviousMonth")
    public void testDebitPayPreviousMonth() {
        card = getPreviousMonthCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.invalidDateError("Месяц");
    }

    @Test
    @DisplayName("testDebitPreviousYear")
    public void testDebitPreviousYear() {
        card = getPreviousYearCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.expiredDateError("Год");
    }

    @Test
    @DisplayName("testDebitFarFutureYear")
    public void testDebitFarFutureYear() {
        card = getFarFutureYearCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.invalidDateError("Год");
    }
}