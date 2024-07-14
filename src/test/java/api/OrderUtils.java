package api;

import api.pojo.Ingredient;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static io.restassured.RestAssured.given;

public final class OrderUtils {

    static List<Ingredient> ingredientList = Ingredient.getIngredientsList();

    //создаем заказ в ответе возвращаем номер
    public static Integer createWithAuth(String accessCode) {

        String firstIngredientID = (ingredientList.get(0).getId());
        String secondIngredientID = (ingredientList.get(1).getId());

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(firstIngredientID);
        jsonArray.put(secondIngredientID);

        JSONObject json = new JSONObject();
        json.put("ingredients", jsonArray);

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json.toString())
                        .auth().oauth2(accessCode)
                        .when()
                        .post(Urls.API_ORDERS);

        return response.getBody().jsonPath().get("order.number");
    }

    public static void createWithoutAuth() {
        String firstIngredientID = (ingredientList.get(0).getId());
        String secondIngredientID = (ingredientList.get(1).getId());
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(firstIngredientID);
        jsonArray.put(secondIngredientID);

        JSONObject json = new JSONObject();
        json.put("ingredients", jsonArray);
        given()
                .header("Content-type", "application/json")
                .body(json.toString())
                .when()
                .post(Urls.API_ORDERS);
    }
}