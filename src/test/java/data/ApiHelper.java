package data;

import entities.Card;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

import static io.restassured.RestAssured.given;

@UtilityClass
public class ApiHelper {
    private static final RequestSpecification specification = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(8080)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public String sentForm(Card card, String path) {
        Response response =
                given()
                        .spec(specification)
                        .body(card)
                        .when()
                        .post(path)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();
        return response.path("status");
    }
}