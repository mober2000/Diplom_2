package ordertests;

import io.restassured.response.ValidatableResponse;
import order.CreateOrder;
import pojo.createdorderdata.CreatedOrderData;
import pojo.ingridientdata.IngredientData;
import pojo.ingridientdata.Ingridient;
import org.junit.Test;
import universalclasses.RandomGenerator;
import user.loginuser.LoginUser;
import pojo.loginuserdata.LoginUserData;
import user.useractions.UserActions;
import user.usercreate.CreateUser;
import pojo.createuserdata.CreateUserData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateOrderTests {
    LoginUser loginUser = new LoginUser();
    CreateOrder createOrder = new CreateOrder();
    CreateUser createUser = new CreateUser();
    UserActions userActions = new UserActions();
    CreatedOrderData createdOrderData;
    List<String> hashIngredients = new ArrayList<>();
    RandomGenerator randomGenerator = new RandomGenerator();
    String mail = randomGenerator.getEmailYandex();
    String password = randomGenerator.getPassword();
    String name = randomGenerator.getName();

    //Это окей но надо дополнить
    @Test
    public void createOrderAuthorizedUserWithAnIngredientTest(){
        ValidatableResponse createUserRequest = createUser.createUserResponse(new CreateUserData(mail, password, name));
        createUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailResponse = createUserRequest.extract().path("user.email");
        String nameResponse = createUserRequest.extract().path("user.name");
        assertEquals(mail, mailResponse);
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

        assertEquals(idFirst, getOrderDataRequest.getOrder().getIngredients().get(0).get_id());
        assertEquals(idSecond, getOrderDataRequest.getOrder().getIngredients().get(1).get_id());
        assertEquals(idThird, getOrderDataRequest.getOrder().getIngredients().get(2).get_id());
        assertEquals(mail, getOrderDataRequest.getOrder().getOwner().getEmail());
        assertEquals(name, getOrderDataRequest.getOrder().getOwner().getName());
        assertEquals("done", getOrderDataRequest.getOrder().getStatus());

        ValidatableResponse deleteUserRequest = userActions.deleteUserKeyRequest(bearerTokenResponse);
        deleteUserRequest.statusCode(202).assertThat().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }

    @Test
    public void createOrderUnauthorizedUserWithAnIngredientTest(){
        IngredientData getIngredientRequest =  createOrder.getIngredientList();
        String idFirst = getIngredientRequest.getData().get(0).get_id();
        String idSecond = getIngredientRequest.getData().get(1).get_id();
        String idThird = getIngredientRequest.getData().get(2).get_id();
        hashIngredients.add(idFirst);
        hashIngredients.add(idSecond);
        hashIngredients.add(idThird);

        ValidatableResponse createOrderRequest = createOrder.createOrder(new Ingridient(hashIngredients),"");
        createOrderRequest.statusCode(200).assertThat()
                .body("success", equalTo(true))
                .and().body("name", notNullValue())
                .and().body("order.number", notNullValue());
        int orderNumber = createOrderRequest.extract().path("order.number");
        assertTrue(orderNumber < 10000);
    }

    //Это тоже окей
    @Test
    public void createOrderAuthorizedUserNotWithAnIngredientTest(){
        ValidatableResponse loginUserRequest = loginUser.loginUserRequest(new LoginUserData("alex1234@yandex.com", "12345678"));
        loginUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String bearerTokenResponse = loginUserRequest.extract().path("accessToken");

        ValidatableResponse createOrderRequest = createOrder.createOrder(new Ingridient(hashIngredients), bearerTokenResponse);
        createOrderRequest.statusCode(400).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderUnauthorizedUserNotWithAnIngredientTest(){
        ValidatableResponse createOrderRequest = createOrder.createOrder(new Ingridient(hashIngredients), "");
        createOrderRequest.statusCode(400).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Ingredient ids must be provided"));
    }


    @Test
    public void createOrderNotCorrectedIngredientHash() {
        ValidatableResponse loginUserRequest = loginUser.loginUserRequest(new LoginUserData("alex1234@yandex.com", "12345678"));
        loginUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String bearerTokenResponse = loginUserRequest.extract().path("accessToken");

        hashIngredients.add("11111111111111111111111111111111");
        ValidatableResponse createOrderRequest = createOrder.createOrder(new Ingridient(hashIngredients), bearerTokenResponse);
        createOrderRequest.statusCode(500);
    }
}
