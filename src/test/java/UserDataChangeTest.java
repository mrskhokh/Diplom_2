import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserDataChangeTest {
    public static final String PASSWORD = "12345678";
    public String name;
    public String email;

    @Step("User data change PATCH request send")
    public Response requestSend(String json, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .auth().oauth2(accessToken)
                .when()
                .patch("https://stellarburgers.nomoreparties.site/api/auth/user");
    }

    @Step("Get user data request send")
    public Response getRequestSend(String json, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .auth().oauth2(accessToken)
                .get("https://stellarburgers.nomoreparties.site/api/auth/user");
    }

    @Step("Compare success response")
    public void responseSuccessCompare(Response response, String newEmail, String newName) {
        response.then().assertThat().statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("user.email", equalTo(newEmail)).and()
                .body("user.name", equalTo(newName));

    }

    @Step("Compare response with auth exception")
    public void responseNoAuthCompare(Response response) {
        response.then().assertThat().statusCode(401).and()
                .body("success", equalTo(false)).and()
                .body("message", equalTo("You should be authorised"));
    }

    public void responseGetCompare(Response getResponse, String newName, String newEmail) {
        getResponse.then().assertThat().statusCode(200).and()
                .body("success", equalTo(true)).and()
                .body("user.email", equalTo(newEmail)).and()
                .body("user.name", equalTo(newName));
    }

    @Before
    public void beforeEach() {
        name = "mrskhokh" + TestUtils.generateRandomNumbers(4);
        email = name + "@ya.ru";
    }

    @Test
    public void UserDataChangeWithAuthorizationSuccessTest() {
        AuthTokens authTokens = User.create(email, PASSWORD, name);
        String accessToken = authTokens.getAccessToken();
        String refreshToken = authTokens.getRefreshToken();
        String newEmail = "new" + email;
        String newName = "new" + name;
        String newPassword = "new" + PASSWORD;
        String json = "{\n" +
                "\"email\": \"" + newEmail + "\",\n" +
                "\"password\": \"" + newPassword + "\",\n" +
                "\"name\": \"" + newName + "\"\n" +
                "}";

        Response response = requestSend(json, accessToken);
        responseSuccessCompare(response, newEmail, newName);
        User.logout(refreshToken);

        //Проверка GET что пользователь действительно сохранился
        String getJson = "{\n" +
                "\"email\": \"" + newEmail + "\",\n" +
                "\"password\": \"" + newPassword + "\"\n" +
                "}";
        AuthTokens newAuthTokens = AuthTokens.login(newEmail, newPassword);
        Assert.assertNotNull(newAuthTokens);

        String newAccessToken = newAuthTokens.getAccessToken();
        Response getResponse = getRequestSend(getJson, newAccessToken);
        responseGetCompare(getResponse, newName, newEmail);
        User.delete(newAccessToken);
    }

    @Test
    public void UserDataChangeWithoutAuthorizationTest() {
        User.create(email, PASSWORD, name);
        String newName = "new" + name;
        String newEmail = "new" + email;
        String newPassword = "new" + PASSWORD;
        String json = "{\n" +
                "\"email\": \"" + newEmail + "\",\n" +
                "\"password\": \"" + newPassword + "\",\n" +
                "\"name\": \"" + newName + "\"\n" +
                "}";
        Response response = requestSend(json, "");
        responseNoAuthCompare(response);
    }
}


