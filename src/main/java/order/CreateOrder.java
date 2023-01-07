package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import order.ingridientdata.IngredientData;
import order.ingridientdata.Ingridient;
import user.loginuser.LoginUserData;

import java.util.List;

import static io.restassured.RestAssured.given;

public class CreateOrder extends RestClient{

    @Step("Получаем список ингредиентов и вносим этот список в json")
    public IngredientData getIngredientList()  {
        return reqSpec
                .get("ingredients")
                .body()
                .as(IngredientData.class);
    }

    @Step("Создаем заказ")
    public ValidatableResponse createOrder(Ingridient ingridient) {
        return reqSpec
                    .body(ingridient)
                    .when()
                    .post("orders")
                    .then().log().all();
    }
}