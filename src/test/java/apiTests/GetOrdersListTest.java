package apiTests;

import apiTestUtils.*;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static apiTestUtils.TestUtils.PASSWORD;
public class GetOrdersListTest extends GetOrderList {


    @Before
    public void beforeEach() {
        super.setUp();
    }

    @DisplayName("Getting order list for authorized user")
    @Test
    public void getOrderListForAuthorizationUserTest() {
        AuthTokens authTokens = User.create(email, PASSWORD, name);
        //Создаем новый заказ
        Integer orderNumber = TestUtils.Order.createWithAuth(authTokens.getAccessToken());
        responseOrderNumberCheck(orderNumber, authTokens.getAccessToken());
        //Удаляем пользователя
        User.delete(authTokens.getAccessToken());
    }

    @DisplayName("Getting order list for NOT authorized user")
    @Test
    public void getOrderListForNoAuthorizationUserTest() {
        //Создаем новый заказ
        TestUtils.Order.createWithoutAuth();
        responseCheck();
    }
}