package apitestutils;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static io.restassured.RestAssured.given;

public final class TestUtils {

    public static final String PASSWORD = "12345678";

    public static String generateEmail() {
        return Faker.instance().bothify("????##@yandex.ru");
    }

    public static String generateUserName() {
        return Faker.instance().name().firstName();
    }

    public static String generateRandomNumbers(int length) {
        return String.valueOf(Faker.instance().number().digits(length));
    }

    public static class Order {


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
}
