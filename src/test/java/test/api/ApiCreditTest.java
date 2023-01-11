package test.api;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.gson.Gson;
import data.*;
import entities.Card;
import entities.OrderEntity;
import entities.CreditRequestEntity;
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

import static data.DataBaseAssistant.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApiCreditTest {
    private final String path = "/api/v1/credit";
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
        CreditRequestEntity credit = getCreditRequestEntity();

        assertNotNull(order);
        assertNotNull(credit);

        assertEquals(order.getCredit_id(), credit.getId());
        assertEquals("declined", credit.getStatus().toLowerCase());
        assertEquals(order.getPayment_id(), credit.getBank_id());
    }
}