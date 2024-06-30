import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

public class Ingredients {
    @SerializedName(value = "_id")
    private String id;
    private String name;

    public String getId() {
        return id;
    }


    public static List<Ingredients> getIngredientsList() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("https://stellarburgers.nomoreparties.site/api/ingredients");


        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response.body().asString(), JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");

        List<Ingredients> ingredients = new ArrayList<>();
        for (JsonElement element : dataArray) {
            Ingredients ingredient = gson.fromJson(element, Ingredients.class);
            ingredients.add(ingredient);
        }
        return ingredients;
    }

}