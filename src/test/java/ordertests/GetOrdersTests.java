package ordertests;

import io.restassured.response.ValidatableResponse;
import order.CreateOrder;
import order.pojo.createdorderdata.CreatedOrderData;
import order.pojo.ingridientdata.IngredientData;
import order.pojo.ingridientdata.Ingridient;
import org.junit.Test;
import universalclasses.RandomGenerator;
import user.loginuser.LoginUser;
import user.useractions.GetUpdateAndDeleteUser;
import user.usercreate.CreateUser;
import user.usercreate.CreateUserData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class GetOrdersTests {
    LoginUser loginUser = new LoginUser();
    CreateOrder createOrder = new CreateOrder();
    CreateUser createUser = new CreateUser();
    GetUpdateAndDeleteUser getUpdateAndDeleteUser = new GetUpdateAndDeleteUser();
    CreatedOrderData createdOrderData;
    List<String> hashIngredients = new ArrayList<>();
    RandomGenerator randomGenerator = new RandomGenerator();
    private final String emailYandex = randomGenerator.randomEmail() + "@yandex.ru";
    private final String password = randomGenerator.randomPassword();
    private final String name = randomGenerator.randomName();


    @Test
    public void getOrderListAuthorizedUser() {
        ValidatableResponse createUserRequest = createUser.createUser(new CreateUserData(emailYandex, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailResponse = createUserRequest.extract().path("user.email");
        String nameResponse = createUserRequest.extract().path("user.name");
        assertEquals(emailYandex, mailResponse);
        assertEquals(name, nameResponse);
        String bearerTokenResponse = createUserRequest.extract().path("accessToken");

        IngredientData getIngredientRequest =  createOrder.getIngredientList();
        String idFirst = getIngredientRequest.getData().get(0).get_id();
        String idSecond = getIngredientRequest.getData().get(1).get_id();
        String idThird = getIngredientRequest.getData().get(2).get_id();
        hashIngredients.add(idFirst);
        hashIngredients.add(idSecond);
        hashIngredients.add(idThird);

        ValidatableResponse createOrderRequest = createOrder.createOrder(new Ingridient(hashIngredients),bearerTokenResponse);
        createOrderRequest.statusCode(200).assertThat().body("success", equalTo(true));
        CreatedOrderData getOrderDataRequest = createOrderRequest.extract().as(CreatedOrderData.class);

        String idOrder = getOrderDataRequest.getOrder().get_id();

        ValidatableResponse getUserOrdersRequest = createOrder.getCreatedOrders();
        getUserOrdersRequest.statusCode(200).assertThat().body("success", equalTo(true));

    }
}
