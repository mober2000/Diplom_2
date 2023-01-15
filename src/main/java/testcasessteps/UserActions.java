package testcasessteps;

import api.RestClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.useractiondata.UserData;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class UserActions extends RestClient {
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

    @Step("Внесение изменений в данные неавторизованного пользователя")
    public ValidatableResponse patchDataUserNotKeyRequest(UserData userData) {
        return reqSpec
                .body(userData)
                .when()
                .patch("auth/user")
                .then();
    }

    @Step("Удаление пользователя с предварительной авторизацией")
    public void deleteUser(String bearerToken){
        ValidatableResponse deleteUserRequest = deleteUserKeyRequest(bearerToken);
        deleteUserRequest.statusCode(202).assertThat().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }

    @Step("Удаление пользователя без предварительной авторизации")
    public void deleteUserNotKey(){
        ValidatableResponse deleteUserRequest = deleteUserNotKeyRequest();
        deleteUserRequest.statusCode(202).assertThat().body("success", equalTo(true)).and().body("message", equalTo("User successfully removed"));
    }

    @Step("Изменение данных авторизованного пользователя")
    public void patchUserData(String mail, String name, String bearerToken){
        ValidatableResponse patchUserDataRequest = patchDataUserRequest(new UserData("ya23" + mail,"Man" + name), bearerToken);
        patchUserDataRequest.statusCode(200).assertThat().body("success", equalTo(true));
        String mailPatchResponse = patchUserDataRequest.extract().path("user.email");
        String namePatchResponse = patchUserDataRequest.extract().path("user.name");
        assertEquals("ya23" + mail, mailPatchResponse);
        assertEquals("Man" + name, namePatchResponse);
    }

    @Step("Изменение данных неавторизованного пользователя")
    public void patchDataNotAuthorizationUser(String mail, String name){
        ValidatableResponse patchUserDataRequest = patchDataUserNotKeyRequest(new UserData("ya23" + mail,"Man " + name));
        patchUserDataRequest.statusCode(401).assertThat().body("success", equalTo(false));
    }
}
