package test.ui;

import com.codeborne.selenide.logevents.SelenideLogger;
import entities.Card;
import entities.OrderEntity;
import entities.PaymentEntity;
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
        assertEquals(45_000_00, payment.getAmount());
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
        assertEquals(45_000_00, payment.getAmount());
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
        assertEquals(45_000_00, payment.getAmount());
    }

    @Test
    @DisplayName("testDebitPayPreviousMonth")
    public void testDebitPayPreviousMonth() {
        card = getPreviousMonthCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.expiredDateError("Год");
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

    @Test
    @DisplayName("testDebitPayEmptyCardNumber")
    public void testDebitPayEmptyCardNumber() {
        card = getEmptyCardNumberCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.fieldIsEmptyError("Номер карты");
    }
    @Test
    @DisplayName("testDebitPayOneDigitCardNumber")
    public void testDebitPayOneDigitCardNumber() {
        card = getOneDigitCardNumberCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.wrongFormatError("Номер карты");
    }

    @Test
    @DisplayName("testDebitPayFifteenDigitsCardNumber")
    public void testDebitPayFifteenDigitsCardNumber() {
        card = getFifteenDigitsCardNumberCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.wrongFormatError("Номер карты");
    }
    @Test
    @DisplayName("testDebitPayEmptyMonth")
    public void testDebitPayEmptyMonth() {
        card = getEmptyMonthCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.fieldIsEmptyError("Месяц");
    }

    @Test
    @DisplayName("testDebitPayMonthValueIsGreaterTwelve")
    public void testDebitPayMonthValueIsGreaterTwelve() {
        card = getMonthGreaterTwelveCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.invalidDateError("Месяц");
    }

    @Test
    @DisplayName("testDebitPayMonthValueIsLessOne")
    public void testDebitPayMonthValueIsLessOne() {
        card = getMonthLessOneCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.invalidDateError("Месяц");
    }
    @Test
    @DisplayName("testDebitPayEmptyYear")
    public void testDebitPayEmptyYear() {
        card = getEmptyYearCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.fieldIsEmptyError("Год");
    }

    @Test
    @DisplayName("testDebitPayYearOfZeros")
    public void testDebitPayYearOfZeros() {
        card = getYearOfZerosCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.expiredDateError("Год");
    }
    @Test
    @DisplayName("testDebitEmptyCvv")
    public void testDebitEmptyCvv() {
        card = getEmptyCvvCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.fieldIsEmptyError("CVC/CVV");
    }

    @Test
    @DisplayName("testDebitOneDigitCvv")
    public void testDebitOneDigitCvv() {
        card = getOneDigitCvvCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.wrongFormatError("CVC/CVV");
    }

    @Test
    @DisplayName("testDebitTwoDigitCvv")
    public void testDebitTwoDigitCvv() {
        card = getTwoDigitsCvvCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.wrongFormatError("CVC/CVV");
    }

    @Test
    @DisplayName("testDebitCvvOfZeros")
    public void testDebitCvvOfZeros() {
        card = getCvvOfZerosCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationPresent();
    }
    @Test
    @DisplayName("testDebitCapitalOwner")
    public void testDebitCapitalOwner() {
        card = getCapitalOwnerCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationPresent();
    }

    @Test
    @DisplayName("testDebitOwnerWithDash")
    public void testDebitOwnerWithDash() {
        card = getOwnerWithDashCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationPresent();
    }

    @Test
    @DisplayName("testDebitEmptyOwner")
    public void testDebitEmptyOwner() {
        card = getEmptyOwnerCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.fieldIsEmptyError("Владелец");
    }

    @Test
    @DisplayName("testDebitOneWordOwner")
    public void testDebitOneWordOwner() {
        card = getOneWordOwnerCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.wrongFormatError("Владелец");
    }

    @Test
    @DisplayName("testDebitCyrillicOwner")
    public void testDebitCyrillicOwner() {
        card = getCyrillicOwnerCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.wrongFormatError("Владелец");
    }

    @Test
    @DisplayName("testDebitSymbolsOwner")
    public void testDebitSymbolsOwner() {
        card = getSymbolsOwnerCard();
        page.pay(card);
        page.checkErrorNotificationAbsent();
        page.checkSuccessNotificationAbsent();
        page.wrongFormatError("Владелец");
    }
}