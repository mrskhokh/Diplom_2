package apitestutils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetOrderList extends AbstractTest {
    @Step("Send request")
    public Response sendRequest(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .when()
                .get(Urls.apiOrders);
    }

    @Step("Order number check")
    public void responseOrderNumberCheck(Integer orderNumber, String accessToken) {
        Response response = sendRequest(accessToken);
        response.then().assertThat()
                .statusCode(200).and()
                .body("success", equalTo(true))
                //Проверяем, что заказ, который создали есть в списке заказов
                .and().body("orders[0].number", equalTo(orderNumber));
    }

    @Step("Response check")
    public void responseCheck() {
        Response response = sendRequest("");
        response.then().assertThat()
                .statusCode(401).and()
                .body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }

}
