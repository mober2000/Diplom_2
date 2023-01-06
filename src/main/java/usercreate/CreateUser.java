package usercreate;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class CreateUser extends RestClient{

    @Step("Регистрация нового пользователя")
    public ValidatableResponse createUser(CreateUserData createUserData) {
        return reqSpec
                .body(createUserData)
                .when()
                .post("auth/register")
                .then().log().all();
    }
}
