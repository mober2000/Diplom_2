package ordertests;

import testcasessteps.*;
import org.junit.Test;
import universalclasses.RandomGenerator;

import java.util.List;

public class GetOrdersTests {
    CreateOrder createOrder = new CreateOrder();
    CreateUser createUser = new CreateUser();
    UserActions userActions = new UserActions();
    RandomGenerator randomGenerator = new RandomGenerator();
    String mail = randomGenerator.getEmailYandex();
    String password = randomGenerator.getPassword();
    String name = randomGenerator.getName();
    GetOrder getOrder = new GetOrder();

    @Test
    public void getOrderListAuthorizedUser() {
        createUser.createCorrectUser(mail, password, name);
        String bearerToken = createUser.getBearerTokenCreatedAccount();
        createOrder.addIngredients();
        List<String> hashIngredients = createOrder.getHashIngredients();
        getOrder.getOrderListAutorized(hashIngredients, bearerToken);
        userActions.deleteUser(bearerToken);
    }

    @Test
    public void getOrderListUnauthorizedUser() {
        getOrder.getOrderListUnautorized();
    }
}
