package test.api;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.*;
import entities.OrderEntity;
import entities.PaymentEntity;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.*;

import static data.DataBaseAssistant.getOrderEntity;
import static data.DataBaseAssistant.getPaymentEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApiPayTest {

    private final String path = "/api/v1/pay";

    @BeforeAll
    public static void setUpClass() {
        SelenideLogger.addListener("allure", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
    }

    @BeforeEach
    public void prepare() {
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                new AllureRestAssured());
        DataBaseAssistant.cleanData();
    }

    @AfterAll
    public static void setDownClass() {
        SelenideLogger.removeListener("allure");
    }

    @DisplayName("checkPaymentValidCard")
    @Test
    public void checkPaymentValidCard() {
        var info = DataGenerator.getValidApprovedCard();
        var actual = ApiHelper.sentForm(info, path);
        assertEquals("approved", actual.toLowerCase());

        OrderEntity order = getOrderEntity();
        PaymentEntity payment = getPaymentEntity();

        assertNotNull(order);
        assertNotNull(payment);

        assertEquals(order.getPayment_id(), payment.getTransaction_id());
        assertEquals("approved", payment.getStatus().toLowerCase());
        assertEquals(45_000, payment.getAmount());
    }

    @DisplayName("checkPaymentDeclinedCard")
    @Test
    public void checkPaymentDeclinedCard() {
        var info = DataGenerator.getValidDeclinedCard();
        var actual = ApiHelper.sentForm(info, path);
        assertEquals("declined", actual.toLowerCase());

        OrderEntity order = getOrderEntity();
        PaymentEntity payment = getPaymentEntity();

        assertNotNull(order);
        assertNotNull(payment);

        assertEquals(order.getPayment_id(), payment.getTransaction_id());
        assertEquals("declined", payment.getStatus().toLowerCase());
        assertEquals(45_000, payment.getAmount());
    }
}