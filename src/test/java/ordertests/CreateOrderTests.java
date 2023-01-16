package ordertests;

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
    public void createOrderAuthorizedUserWithAnIngredientTest() {
        createUser.createCorrectUser(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        createOrder.addIngredients();
        createOrder.createAuthorizedOrder(mail, name, bearerToken);
        userActions.deleteUser(bearerToken);
    }

    @Test
    public void createOrderUnauthorizedUserWithAnIngredientTest() {
        createOrder.addIngredients();
        createOrder.createUnauthorizedOrder();
    }

    @Test
    public void createOrderAuthorizedUserNotWithAnIngredientTest() {
        createUser.createCorrectUser(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        createOrder.createNotIngredient(bearerToken);
        userActions.deleteUser(bearerToken);
    }

    @Test
    public void createOrderUnauthorizedUserNotWithAnIngredientTest() {
        createOrder.createNotIngredient("");
    }


    @Test
    public void createOrderNotCorrectedIngredientHash() {
        createUser.createCorrectUser(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        createOrder.notCorrectedHashIngredient(bearerToken);
        userActions.deleteUser(bearerToken);
    }
}
