import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreatingTest {
    public static final String PASSWORD = "12345678";
    public String name;
    public String email;

    List<Ingredients> ingredientList = Ingredients.getIngredientsList();

    @Step("Creating orders POST request send")
    public Response requestSent(String json, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .auth().oauth2(accessToken)
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/orders");
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

    @Before
    public void beforeEach() {
        name = "mrskhokh" + TestUtils.generateRandomNumbers(4);
        email = name + "@ya.ru";
    }

    @Test
    public void OrderCreatingWithAuthorizationSuccessTest() {
        User.create(email, PASSWORD, name);
        String firstIngredientID = (ingredientList.get(0).getId());
        String secondIngredientID = (ingredientList.get(1).getId());
        String json = "{\n" +
                "\"ingredients\": [\"" + firstIngredientID + "\",\"" + secondIngredientID + "\"]\n" +
                "}";
        AuthTokens authTokens = AuthTokens.login(email, PASSWORD);
        Assert.assertNotNull(authTokens);

        Response response = requestSent(json, authTokens.getAccessToken());
        responseCheck(response);
        User.delete(authTokens.getAccessToken());
    }

    @Test
    public void OrderCreatingWithoutAuthorizationSuccessTest() {
        String firstIngredientID = (ingredientList.get(0).getId());
        String secondIngredientID = (ingredientList.get(1).getId());
        String json = "{\n" +
                "\"ingredients\": [\"" + firstIngredientID + "\",\"" + secondIngredientID + "\"]\n" +
                "}";
        Response response = requestSent(json, "");
        responseCheck(response);
    }

    @Test
    public void OrderCreatingWithoutAuthorizationAndWithoutIngredientsTest() {
        String json = "{\n" +
                "\"ingredients\": []\n" +
                "}";
        Response response = requestSent(json, "");
        responseIngredientsCheck(response, 400, false, "Ingredient ids must be provided");
    }

    @Test
    public void OrderCreatingWithoutAuthorizationWithWrongIngredientsIDTest() {
        String json = "{\n" +
                "\"ingredients\": [\"" + TestUtils.generateRandomNumbers(24) + "\"," +
                "\"" + TestUtils.generateRandomNumbers(24) + "\"]\n" +
                "}";
        Response response = requestSent(json, "");
        responseIngredientsCheck(response, 400, false, "One or more ids provided are incorrect");
    }

}