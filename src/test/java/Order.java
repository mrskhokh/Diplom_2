import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class Order {


    static List<Ingredients> ingredientList = Ingredients.getIngredientsList();

    //создаем заказ в ответе возвращаем номер
    public static Integer createWithAuth(String accessCode) {

        String firstIngredientID = (ingredientList.get(0).getId());
        String secondIngredientID = (ingredientList.get(1).getId());
        String json = "{\n" +
                "\"ingredients\": [\"" + firstIngredientID + "\",\"" + secondIngredientID + "\"]\n" +
                "}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .auth().oauth2(accessCode)
                        .when()
                        .post("https://stellarburgers.nomoreparties.site/api/orders");

        return response.getBody().jsonPath().get("order.number");
    }

    public static void createWithoutAuth() {
        String firstIngredientID = (ingredientList.get(0).getId());
        String secondIngredientID = (ingredientList.get(1).getId());
        String json = "{\n" +
                "\"ingredients\": [\"" + firstIngredientID + "\",\"" + secondIngredientID + "\"]\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/orders");
    }
}
