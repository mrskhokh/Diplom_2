package api.step;

import api.Urls;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public final class OrderStepHolder {

    @Step("Creating orders POST request send")
    public static Response requestSent(String json, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .auth().oauth2(accessToken)
                .when()
                .post(Urls.API_ORDERS);
    }

    @Step("Success response check")
    public static void responseCheck(Response response) {
        response.then().assertThat()
                .statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("order.number", notNullValue());
    }

    @Step("Error ingredients response check")
    public static void responseIngredientsCheck(Response response, Integer code, Boolean success, String message) {
        response.then().assertThat()
                .statusCode(code).and()
                .body("success", equalTo(success))
                .body("message", equalTo(message));

    }

    @Step("Send request")
    public static Response sendRequest(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .when()
                .get(Urls.API_ORDERS);
    }

    @Step("Order number check")
    public static void responseOrderNumberCheck(Integer orderNumber, String accessToken) {
        Response response = sendRequest(accessToken);
        response.then().assertThat()
                .statusCode(200).and()
                .body("success", equalTo(true))
                //Проверяем, что заказ, который создали есть в списке заказов
                .and().body("orders[0].number", equalTo(orderNumber));
    }

    @Step("Response check")
    public static void responseCheck() {
        Response response = sendRequest("");
        response.then().assertThat()
                .statusCode(401).and()
                .body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }
}
