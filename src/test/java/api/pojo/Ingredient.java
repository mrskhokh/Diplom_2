package api.pojo;

import api.Urls;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Ingredient {
    @SerializedName(value = "_id")
    private String id;
    private String name;

    public static List<Ingredient> getIngredientsList() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get(Urls.API_GET_INGREDIENTS);

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response.body().asString(), JsonObject.class);
        JsonArray dataArray = jsonObject.getAsJsonArray("data");

        List<Ingredient> ingredients = new ArrayList<>();
        for (JsonElement element : dataArray) {
            Ingredient ingredient = gson.fromJson(element, Ingredient.class);
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    public String getId() {
        return id;
    }
}