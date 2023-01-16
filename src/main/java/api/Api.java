package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.createuserdata.CreateUserData;
import pojo.ingridientdata.IngredientData;
import pojo.ingridientdata.Ingridient;
import pojo.loginuserdata.LoginUserData;
import pojo.useractiondata.UserData;

public class Api extends RestClient {
    @Step("Запрос на регистрацию нового пользователя")
    public ValidatableResponse createUserRequest(CreateUserData createUserData) {
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

    @Step("Запрос на получение списка ингредиентов и вносение тела в json")
    public IngredientData getIngredientListRequest() {
        return reqSpec
                .get("ingredients")
                .body()
                .as(IngredientData.class);
    }

    @Step("Запрос на создание заказа")
    public ValidatableResponse createOrderRequest(Ingridient ingridient, String bearerToken) {
        return reqSpec
                .header("Authorization", bearerToken)
                .body(ingridient)
                .when()
                .post("orders")
                .then().log().all();
    }

    @Step("Запрос на получение информации о заказах конкретного пользователя")
    public ValidatableResponse getCreatedOrdersRequest() {
        return reqSpec
                .when()
                .get("orders")
                .then().log().all();
    }

    @Step("Запрос на удаление пользователя с ключом авторизации")
    public ValidatableResponse deleteUserKeyRequest(String bearer) {
        return reqSpec
                .header("Authorization", bearer)
                .when()
                .delete("auth/user")
                .then();
    }

    @Step("Запрос на удаление пользователя без ключа авторизации")
    public ValidatableResponse deleteUserNotKeyRequest() {
        return reqSpec
                .delete("auth/user")
                .then();
    }

    @Step("Запрос на внесение изменений в данные авторизованного пользователя")
    public ValidatableResponse patchDataUserRequest(UserData userData, String bearer) {
        return reqSpec
                .body(userData)
                .header("Authorization", bearer)
                .when()
                .patch("auth/user")
                .then();
    }

    @Step("Запрос на внесение изменений в данные неавторизованного пользователя")
    public ValidatableResponse patchDataUserNotKeyRequest(UserData userData) {
        return reqSpec
                .body(userData)
                .when()
                .patch("auth/user")
                .then();
    }
}
