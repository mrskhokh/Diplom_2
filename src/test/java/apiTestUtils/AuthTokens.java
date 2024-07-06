package apiTestUtils;

import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class AuthTokens {
    private final String accessToken;
    private final String refreshToken;

    public AuthTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static AuthTokens login(String email, String password) {
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("password", password);
        try {
            Response response =
                    given()
                            .header("Content-type", "application/json")
                            .body(json.toString())
                            .when()
                            .post(Urls.apiLogin);

            if (response.getStatusCode() == 200) {
                String accessToken = response.getBody().jsonPath().getString("accessToken").
                        replace("Bearer", "").trim();
                String refreshToken = response.getBody().jsonPath().getString("refreshToken");
                return new AuthTokens(accessToken, refreshToken);
            } else {
                // Обработка ошибки, если статус ответа не равен 200
                return null;
            }
        } catch (Exception e) {
            // Обработка исключения
            e.printStackTrace();
            return null;
        }


    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
