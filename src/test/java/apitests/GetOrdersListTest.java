package apitests;

import api.OrderUtils;
import api.pojo.AuthToken;
import api.step.OrderStepHolder;
import api.step.UserStepHolder;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static api.TestConstants.PASSWORD;

public class GetOrdersListTest extends AbstractTest {

    @DisplayName("Getting order list for authorized user")
    @Test
    public void getOrderListForAuthorizationUserTest() {
        AuthToken authTokens = UserStepHolder.create(email, PASSWORD, name);
        //Создаем новый заказ
        Integer orderNumber = OrderUtils.createWithAuth(authTokens.getAccessToken());
        OrderStepHolder.responseOrderNumberCheck(orderNumber, authTokens.getAccessToken());
        //Удаляем пользователя
        UserStepHolder.delete(authTokens.getAccessToken());
    }

    @DisplayName("Getting order list for NOT authorized user")
    @Test
    public void getOrderListForNoAuthorizationUserTest() {
        //Создаем новый заказ
        OrderUtils.createWithoutAuth();
        OrderStepHolder.responseCheck();
    }
}