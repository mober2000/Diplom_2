package ordertests;

import io.restassured.response.ValidatableResponse;
import order.CreateOrder;
import order.ingridientdata.IngredientData;
import order.ingridientdata.Ingridient;
import org.junit.Test;
import user.loginuser.LoginUser;
import user.loginuser.LoginUserData;
import user.useractions.GetUpdateAndDeleteUser;
import user.useractions.RefreshToken;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;

public class CreateOrderTests {
    LoginUser loginUser = new LoginUser();
    CreateOrder createOrder = new CreateOrder();
    GetUpdateAndDeleteUser getUpdateAndDeleteUser = new GetUpdateAndDeleteUser();
    List<String> hashIngredients = new ArrayList<>();

    //Почему этот тест проходит без авторизации?
    @Test
    public void createOrderUnauthorizedUserWithAnIngredientTest(){
        ValidatableResponse loginUserRequest = loginUser.loginUser(new LoginUserData("alex1234@yandex.com", "12345678"));
        loginUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String refreshTokenLoginResponse = loginUserRequest.extract().path("refreshToken");

        ValidatableResponse logoutSystemRequest = getUpdateAndDeleteUser.exitOnSystem(new RefreshToken(refreshTokenLoginResponse));
        logoutSystemRequest.statusCode(200).assertThat().body("success", equalTo(true));

        IngredientData getIngredientRequest =  createOrder.getIngredientList();
        String idFirst = getIngredientRequest.getData().get(0).get_id();
        String idSecond = getIngredientRequest.getData().get(1).get_id();
        String idThird = getIngredientRequest.getData().get(2).get_id();
        hashIngredients.add(idFirst);
        hashIngredients.add(idSecond);
        hashIngredients.add(idThird);

        ValidatableResponse createOrderRequest = createOrder.createOrder(new Ingridient(hashIngredients));
        createOrderRequest.statusCode(200).assertThat().body("success", equalTo(true)).and().body("name", notNullValue()).and().body("order.number", notNullValue());
    }

    @Test
    public void createOrderAuthorizedUserNotWithAnIngredientTest(){
        ValidatableResponse loginUserRequest = loginUser.loginUser(new LoginUserData("alex1234@yandex.com", "12345678"));
        loginUserRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String bearerTokenResponse = loginUserRequest.extract().path("accessToken");
        ValidatableResponse GetUserRequest = getUpdateAndDeleteUser.getDataUser(bearerTokenResponse);
        GetUserRequest.statusCode(200).assertThat().body("success", equalTo(true));

        ValidatableResponse createOrderRequest = createOrder.createOrder(new Ingridient(hashIngredients));
        createOrderRequest.statusCode(400).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderNotCorrectedIngredientHash() {
        hashIngredients.add("11111111111111111111111111111111");
        ValidatableResponse createOrderRequest = createOrder.createOrder(new Ingridient(hashIngredients));
        createOrderRequest.statusCode(500);
    }
}
