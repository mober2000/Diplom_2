package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.ingridientdata.IngredientData;
import pojo.ingridientdata.Ingridient;

public class CreateOrder extends RestClient{

    @Step("Получаем список ингредиентов и вносим этот список в json")
    public IngredientData getIngredientList()  {
        return reqSpec
                .get("ingredients")
                .body()
                .as(IngredientData.class);
    }

    @Step("Создаем заказ")
    public ValidatableResponse createOrder(Ingridient ingridient, String bearerToken) {
        return reqSpec
                    .header("Authorization", bearerToken)
                    .body(ingridient)
                    .when()
                    .post("orders")
                    .then().log().all();
    }

    @Step("Получем информацию о заказах конкретного пользователя")
    public ValidatableResponse getCreatedOrders() {
        return reqSpec
//                .header("Authorization", bearerToken)
                .when()
                .get("orders")
                .then().log().all();
    }
}