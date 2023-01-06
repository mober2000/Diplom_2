package user.loginuser;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class LoginUser extends RestClient {

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(LoginUserData loginUserData) {
        return reqSpec
                .body(loginUserData)
                .when()
                .post("auth/login")
                .then().log().all();
    }

}
