package apiTestUtils;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreate extends AbstractTest {

    @Step("Creating orders POST request send")
    public Response requestSent(String json, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .auth().oauth2(accessToken)
                .when()
                .post(Urls.apiOrders);
    }

    @Step("Success response check")
    public void responseCheck(Response response) {
        response.then().assertThat()
                .statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("order.number", notNullValue());
    }

    @Step("Error ingredients response check")
    public void responseIngredientsCheck(Response response, Integer code, Boolean success, String message) {
        response.then().assertThat()
                .statusCode(code).and()
                .body("success", equalTo(success))
                .body("message", equalTo(message));

    }
}
