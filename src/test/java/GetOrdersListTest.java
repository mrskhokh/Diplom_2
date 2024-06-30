import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetOrdersListTest {
    public static final String PASSWORD = "12345678";
    public String name;
    public String email;

    @Step("Send request")
    public Response sendRequest(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .when()
                .get("https://stellarburgers.nomoreparties.site/api/orders");
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

    @Before
    public void before() {
        name = "mrskhokh" + TestUtils.generateRandomNumbers(4);
        email = name + "@ya.ru";
    }

    @Test
    public void GetOrderListForAuthorizationUserTest() {
        AuthTokens authTokens = User.create(email, PASSWORD, name);
        //Создаем новый заказ
        Integer orderNumber = Order.createWithAuth(authTokens.getAccessToken());
        responseOrderNumberCheck(orderNumber, authTokens.getAccessToken());
        //Удаляем пользователя
        User.delete(authTokens.getAccessToken());
    }

    @Test
    public void GetOrderListForNoAuthorizationUserTest() {
        //Создаем новый заказ
        Order.createWithoutAuth();
        responseCheck();
    }
}