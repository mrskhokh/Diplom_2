import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class User {
    @Step("User creating")
    public static AuthTokens create(String email, String password, String name) {

        String json = "{\n" +
                "\"email\": \"" + email + "\",\n" +
                "\"password\": \"" + password + "\",\n" +
                "\"name\": \"" + name + "\"\n" +
                "}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("https://stellarburgers.nomoreparties.site/api/auth/register");

        String accessToken = response.getBody()
                .jsonPath()
                .getString("accessToken")
                .replace("Bearer", "")
                .trim();

        String refreshToken = response.getBody().jsonPath().getString("refreshToken");

        return new AuthTokens(accessToken, refreshToken);
    }

    @Step("Logout")
    public static void logout(String refreshCode) {
        String json = "{\n" +
                "    \"token\":\"" + refreshCode + "\"\n" +
                "}";
        given()
                .body(json)
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/auth/logout");
    }

    @Step("User deleting")
    public static void delete(String accessCode) {
        given()
                .auth().oauth2(accessCode)
                .when()
                .delete("https://stellarburgers.nomoreparties.site/api/auth/user");

    }
}

