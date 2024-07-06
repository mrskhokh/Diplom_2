package apiTests;

import apiTestUtils.AuthTokens;
import apiTestUtils.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static apiTestUtils.TestUtils.PASSWORD;

public class UserDataChangeTest extends User {

    @Before
    public void beforeEach() {
        super.setUp();
    }

    @DisplayName("User data change test for auth user")
    @Test
    public void userDataChangeWithAuthorizationSuccessTest() {
        AuthTokens authTokens = User.create(email, PASSWORD, name);
        String accessToken = authTokens.getAccessToken();
        String refreshToken = authTokens.getRefreshToken();

        String newEmail = "new" + email;
        String newName = "new" + name;
        String newPassword = "new" + PASSWORD;


        JSONObject json = new JSONObject();
        json.put("email", newEmail);
        json.put("name", newName);
        json.put("password", newPassword);

        Response response = requestRenameSend(json.toString(), accessToken);
        responseSuccessCompare(response, newEmail, newName);
        User.logout(refreshToken);

        //Проверка GET, что пользователь действительно сохранился
        JSONObject getJson = new JSONObject();
        getJson.put("email", newEmail);
        getJson.put("name", newName);
        getJson.put("password", newPassword);

        AuthTokens newAuthTokens = AuthTokens.login(newEmail, newPassword);


        Assert.assertNotNull(newAuthTokens);
        Assert.assertNotNull(newAuthTokens.getAccessToken());

        String newAccessToken = newAuthTokens.getAccessToken();
        Response getResponse = getRequestSend(getJson.toString(), newAccessToken);
        responseGetCompare(getResponse, newName, newEmail);
        User.delete(newAccessToken);
    }

    @DisplayName("User data change test for NO auth user")
    @Test
    public void userDataChangeWithoutAuthorizationTest() {
        User.create(email, PASSWORD, name);
        String newName = "new" + name;
        String newEmail = "new" + email;
        String newPassword = "new" + PASSWORD;

        JSONObject json = new JSONObject();
        json.put("email", newEmail);
        json.put("name", newName);
        json.put("password", newPassword);

        Response response = requestRenameSend(json.toString(), "");
        responseNoAuthCompare(response);
    }
}


