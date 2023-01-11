package test.api;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.gson.Gson;
import data.*;
import entities.Card;
import entities.OrderEntity;
import entities.PaymentEntity;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import static data.DataBaseAssistant.getOrderEntity;
import static data.DataBaseAssistant.getPaymentEntity;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApiPayTest {

    private final String path = "/api/v1/pay";
    private static final Gson gson = new Gson();
    private static final RequestSpecification specification = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(8080)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL).build();

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
        Card card = DataGenerator.getValidApprovedCard();
        var body = gson.toJson(card);
        given()
                .spec(specification)
                .body(body)
                .when()
                .post(path)
                .then()
                .statusCode(200);

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
        Card card = DataGenerator.getValidDeclinedCard();
        var body = gson.toJson(card);
        given()
                .spec(specification)
                .body(body)
                .when()
                .post(path)
                .then()
                .statusCode(200);

        OrderEntity order = getOrderEntity();
        PaymentEntity payment = getPaymentEntity();

        assertNotNull(order);
        assertNotNull(payment);

        assertEquals(order.getPayment_id(), payment.getTransaction_id());
        assertEquals("declined", payment.getStatus().toLowerCase());
        assertEquals(45_000, payment.getAmount());
    }
}