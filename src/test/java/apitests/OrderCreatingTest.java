package apitests;

import api.TestUtils;
import api.pojo.AuthToken;
import api.pojo.Ingredient;
import api.step.OrderStepHolder;
import api.step.UserStepHolder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static api.TestConstants.PASSWORD;

public class OrderCreatingTest extends AbstractTest {
    List<Ingredient> ingredientList = Ingredient.getIngredientsList();

    @DisplayName("Positive case order creating by authorizated user")
    @Test
    public void orderCreatingWithAuthorizationSuccessTest() {
        UserStepHolder.create(email, PASSWORD, name);
        String firstIngredientID = (ingredientList.get(0).getId());
        String secondIngredientID = (ingredientList.get(1).getId());

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(firstIngredientID);
        jsonArray.put(secondIngredientID);

        JSONObject json = new JSONObject();
        json.put("ingredients", jsonArray);

        AuthToken authTokens = AuthToken.login(email, PASSWORD);
        Assert.assertNotNull(authTokens);

        Response response = OrderStepHolder.requestSent(json.toString(), authTokens.getAccessToken());
        OrderStepHolder.responseCheck(response);
        UserStepHolder.delete(authTokens.getAccessToken());
    }

    @DisplayName("Positive case order creating by NOT authorizated user")
    @Test
    public void orderCreatingWithoutAuthorizationSuccessTest() {
        String firstIngredientID = (ingredientList.get(0).getId());
        String secondIngredientID = (ingredientList.get(1).getId());

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(firstIngredientID);
        jsonArray.put(secondIngredientID);

        JSONObject json = new JSONObject();
        json.put("ingredients", jsonArray);

        Response response = OrderStepHolder.requestSent(json.toString(), "");
        OrderStepHolder.responseCheck(response);
    }

    @DisplayName("Order creating without ingredients")
    @Test
    public void orderCreatingWithoutAuthorizationAndWithoutIngredientsTest() {
        JSONArray jsonArray = new JSONArray();

        JSONObject json = new JSONObject();
        json.put("ingredients", jsonArray);
        Response response = OrderStepHolder.requestSent(json.toString(), "");
        OrderStepHolder.responseIngredientsCheck(response, 400, false, "Ingredient ids must be provided");
    }

    @DisplayName("Order creating with wrong ingredient ID")
    @Test
    public void orderCreatingWithoutAuthorizationWithWrongIngredientsIDTest() {
        JSONArray jsonArray = new JSONArray();
        String ingredient1 = TestUtils.generateRandomNumbers(24);
        String ingredient2 = TestUtils.generateRandomNumbers(24);
        jsonArray.put(ingredient1);
        jsonArray.put(ingredient2);

        JSONObject json = new JSONObject();
        json.put("ingredients", jsonArray);

        Response response = OrderStepHolder.requestSent(json.toString(), "");
        OrderStepHolder.responseIngredientsCheck(response, 400, false, "One or more ids provided are incorrect");
    }

}