package ordertests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import testcasessteps.CreateOrder;
import org.junit.Test;
import universalclasses.RandomGenerator;
import testcasessteps.UserActions;
import testcasessteps.CreateUser;

public class CreateOrderTests {
    CreateOrder createOrder = new CreateOrder();
    CreateUser createUser = new CreateUser();
    UserActions userActions = new UserActions();
    RandomGenerator randomGenerator = new RandomGenerator();
    String mail = randomGenerator.getEmailYandex();
    String password = randomGenerator.getPassword();
    String name = randomGenerator.getName();

    @Test
    @DisplayName("Create Order Authorized User")
    @Description("Проверка на создание заказа авторизованным пользователем")
    public void createOrderAuthorizedUserWithAnIngredient() {
        createUser.createCorrectUser(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        createOrder.addIngredients();
        createOrder.createAuthorizedOrder(mail, name, bearerToken);
        userActions.deleteUser(bearerToken);
    }

    @Test
    @DisplayName("Create Order Unauthorized User")
    @Description("Проверка на создание заказа неавторизованным пользователем")
    public void createOrderUnauthorizedUserWithAnIngredient() {
        createOrder.addIngredients();
        createOrder.createUnauthorizedOrder();
    }

    @Test
    @DisplayName("Create Order Authorized User And Not Ingredient")
    @Description("Проверка на создание заказа авторизованным пользователем, но без указания ингредиентов")
    public void createOrderAuthorizedUserNotWithAnIngredient() {
        createUser.createCorrectUser(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        createOrder.createNotIngredient(bearerToken);
        userActions.deleteUser(bearerToken);
    }

    @Test
    @DisplayName("Create Order Unauthorized User And Not Ingredient")
    @Description("Проверка на создание заказа неавторизованным пользователем, но без указания ингредиентов")
    public void createOrderUnauthorizedUserNotWithAnIngredient() {
        createOrder.createNotIngredient("");
    }

    @Test
    @DisplayName("Create Order And Not Corrected Ingredient Hash")
    @Description("Проверка на создание заказа с ошибкой в значении хэша ингредиента")
    public void createOrderNotCorrectedIngredientHash() {
        createUser.createCorrectUser(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        createOrder.notCorrectedHashIngredient(bearerToken);
        userActions.deleteUser(bearerToken);
    }
}
