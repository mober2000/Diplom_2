package ordertests;

import io.restassured.response.ValidatableResponse;
import order.CreateOrder;
import order.ingridientdata.IngredientData;
import org.junit.Test;
import user.loginuser.LoginUser;
import user.loginuser.LoginUserData;

import static org.junit.Assert.assertEquals;

public class CreateOrderTest {
    LoginUser loginUser = new LoginUser();
    CreateOrder createOrder = new CreateOrder();
    IngredientData ingredientData = new IngredientData();

    @Test
    public void CreateOrderTest(){
        ValidatableResponse loginUserRequest = loginUser.loginUser(new LoginUserData("alex1234@yandex.ru", "12345678"));

        IngredientData getIngredientRequest =  createOrder.getIngredientList();
        String idFirst = getIngredientRequest.getData().get(0).get_id();
        String idSecond = getIngredientRequest.getData().get(1).get_id();
        String idThird = getIngredientRequest.getData().get(2).get_id();



    }
}
