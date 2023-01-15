package user.useractions;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.useractiondata.UserData;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserActions extends RestClient{
    @Step("Удаление пользователя с ключом авторизации")
    public ValidatableResponse deleteUserKeyRequest(String bearer) {
        return reqSpec
                .header("Authorization", bearer)
                .when()
                .delete("auth/user")
                .then();
    }

    @Step("Удаление пользователя без ключа авторизации")
    public ValidatableResponse deleteUserNotKeyRequest() {
        return reqSpec
                .delete("auth/user")
                .then();
    }

    @Step("Внесение изменений в данные авторизованного пользователя")
    public ValidatableResponse patchDataUserRequest(UserData userData, String bearer) {
        return reqSpec
                .body(userData)
                .header("Authorization", bearer)
                .when()
                .patch("auth/user")
                .then();
    }

    @Step("Внесение изменений в данные неавторизованного пользователя")
    public ValidatableResponse patchDataUserNotKeyRequest(UserData userData) {
        return reqSpec
                .body(userData)
                .when()
                .patch("auth/user")
                .then();
    }

    public void deleteUser(String bearerToken){
        ValidatableResponse deleteUserRequest = deleteUserKeyRequest(bearerToken);
        deleteUserRequest.statusCode(202).assertThat().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }
}
