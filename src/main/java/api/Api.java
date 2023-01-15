package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.createuserdata.CreateUserData;
import pojo.ingridientdata.IngredientData;
import pojo.ingridientdata.Ingridient;
import pojo.loginuserdata.LoginUserData;

public class Api extends RestClient{

    @Step("Запрос на регистрацию нового пользователя")
    public ValidatableResponse createUserResponse(CreateUserData createUserData) {
        return reqSpec
                .body(createUserData)
                .when()
                .post("auth/register")
                .then();
    }

    @Step("Запрос на авторизация пользователя")
    public ValidatableResponse loginUserRequest(LoginUserData loginUserData) {
        return reqSpec
                .body(loginUserData)
                .when()
                .post("auth/login")
                .then();
    }

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
                .when()
                .get("orders")
                .then().log().all();
    }
}
