package test.api;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.*;
import entities.OrderEntity;
import entities.CreditRequestEntity;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import org.junit.jupiter.api.*;

import static data.DataBaseAssistant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApiCreditTest {
    private final String path = "/api/v1/credit";


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
        CreditRequestEntity credit = getCreditRequestEntity();

        assertNotNull(order);
        assertNotNull(credit);

        assertEquals(order.getCredit_id(), credit.getId());
        assertEquals("approved", credit.getStatus().toLowerCase());
        assertEquals(order.getPayment_id(), credit.getBank_id());
    }

    @DisplayName("checkPaymentDeclinedCard")
    @Test
    public void checkPaymentDeclinedCard() {
        var info = DataGenerator.getValidDeclinedCard();
        var actual = ApiHelper.sentForm(info, path);
        assertEquals("declined", actual.toLowerCase());

        OrderEntity order = getOrderEntity();
        CreditRequestEntity credit = getCreditRequestEntity();

        assertNotNull(order);
        assertNotNull(credit);

        assertEquals(order.getCredit_id(), credit.getId());
        assertEquals("declined", credit.getStatus().toLowerCase());
        assertEquals(order.getPayment_id(), credit.getBank_id());
    }
}