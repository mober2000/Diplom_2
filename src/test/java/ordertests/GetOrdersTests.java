package ordertests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import testcases.*;
import org.junit.Test;
import universalclasses.RandomGenerator;

import java.util.List;

public class GetOrdersTests {
    CreateOrder createOrder = new CreateOrder();
    CreateUser createUser = new CreateUser();
    UserActions userActions = new UserActions();
    GetOrder getOrder = new GetOrder();
    RandomGenerator randomGenerator = new RandomGenerator();
    String mail = randomGenerator.getEmailYandex();
    String password = randomGenerator.getPassword();
    String name = randomGenerator.getName();

    @Test
    @DisplayName("Get Order List Authorized User")
    @Description("Проверка на получение корректного списка заказов авторизованного пользователя")
    public void getOrderListAuthorizedUser() {
        createUser.createCorrectUser(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        createOrder.addIngredients();
        List<String> hashIngredients = createOrder.getHashIngredients();
        getOrder.getOrderListAuthorization(hashIngredients, bearerToken);
        userActions.deleteUser(bearerToken);
    }

    @Test
    @DisplayName("Get Order List Unauthorized User")
    @Description("Проверка на получение списка заказов неавторизованного пользователя")
    public void getOrderListUnauthorizedUser() {
        getOrder.getOrderListUnauthorized();
    }
}
