package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.createuserdata.CreateUserData;
import pojo.ingridientdata.IngredientData;
import pojo.ingridientdata.Ingridient;
import pojo.loginuserdata.LoginUserData;
import pojo.useractiondata.UserData;

public class Api extends RestClient {
    String REGISTER_API = "auth/register";
    String LOGIN_API = "auth/login";
    String INGREDIENTS_API = "ingredients";
    String ORDERS_API = "orders";
    String AUTHORIZATION_USER_API =  "auth/user";

    @Step("Запрос на регистрацию нового пользователя")
    public ValidatableResponse createUserRequest(CreateUserData createUserData) {
        return reqSpec
                .body(createUserData)
                .when()
                .post(REGISTER_API)
                .then();
    }

    @Step("Запрос на авторизацию пользователя")
    public ValidatableResponse loginUserRequest(LoginUserData loginUserData) {
        return reqSpec
                .body(loginUserData)
                .when()
                .post(LOGIN_API)
                .then();
    }

    @Step("Запрос на получение списка ингредиентов и вносение тела в json")
    public IngredientData getIngredientListRequest() {
        return reqSpec
                .get(INGREDIENTS_API)
                .body()
                .as(IngredientData.class);
    }

    @Step("Запрос на создание заказа")
    public ValidatableResponse createOrderRequest(Ingridient ingridient, String bearerToken) {
        return reqSpec
                .header("Authorization", bearerToken)
                .body(ingridient)
                .when()
                .post(ORDERS_API)
                .then().log().all();
    }

    @Step("Запрос на получение информации о заказах конкретного пользователя")
    public ValidatableResponse getCreatedOrdersRequest() {
        return reqSpec
                .when()
                .get(ORDERS_API)
                .then().log().all();
    }

    @Step("Запрос на удаление пользователя с ключом авторизации")
    public ValidatableResponse deleteUserKeyRequest(String bearer) {
        return reqSpec
                .header("Authorization", bearer)
                .when()
                .delete(AUTHORIZATION_USER_API)
                .then();
    }

    @Step("Запрос на удаление пользователя без ключа авторизации")
    public ValidatableResponse deleteUserNotKeyRequest() {
        return reqSpec
                .delete(AUTHORIZATION_USER_API)
                .then();
    }

    @Step("Запрос на внесение изменений в данные авторизованного пользователя")
    public ValidatableResponse patchDataUserRequest(UserData userData, String bearer) {
        return reqSpec
                .body(userData)
                .header("Authorization", bearer)
                .when()
                .patch(AUTHORIZATION_USER_API)
                .then();
    }

    @Step("Запрос на внесение изменений в данные неавторизованного пользователя")
    public ValidatableResponse patchDataUserNotKeyRequest(UserData userData) {
        return reqSpec
                .body(userData)
                .when()
                .patch(AUTHORIZATION_USER_API)
                .then();
    }
}
